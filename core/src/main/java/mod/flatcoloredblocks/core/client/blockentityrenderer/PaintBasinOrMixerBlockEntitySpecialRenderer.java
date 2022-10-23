package mod.flatcoloredblocks.core.client.blockentityrenderer;

import com.communi.suggestu.scena.core.client.rendering.type.IRenderTypeManager;
import com.communi.suggestu.scena.core.client.utils.FluidCuboidUtils;
import com.communi.suggestu.scena.core.fluid.FluidInformation;
import com.communi.suggestu.scena.core.fluid.IFluidManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import mod.flatcoloredblocks.core.block.entity.PaintContainingBlockEntity;
import mod.flatcoloredblocks.core.fluid.FluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public class PaintBasinOrMixerBlockEntitySpecialRenderer<Z extends PaintContainingBlockEntity> implements BlockEntityRenderer<Z>
{

    private boolean useMaximumRelativeAmounts = true;

    public PaintBasinOrMixerBlockEntitySpecialRenderer()
    {
    }

    public PaintBasinOrMixerBlockEntitySpecialRenderer(final boolean useMaximumRelativeAmounts)
    {
        this.useMaximumRelativeAmounts = useMaximumRelativeAmounts;
    }

    @Override
    public void render(final @NotNull Z pBlockEntity, final float pPartialTick, final @NotNull PoseStack pPoseStack, final @NotNull MultiBufferSource pBufferSource, final int pPackedLight, final int pPackedOverlay)
    {
        final Collection<FluidTank> tanks = pBlockEntity.getTanks();
        int tankIndex = 0;

        final int maximalTankSize = (int) tanks.stream().mapToLong(FluidTank::getMaximalAmount).max().orElse(1);

        for (final FluidTank tank : tanks)
        {
            final Optional<FluidInformation> primaryTankFluid = tank.getContents();
            if (primaryTankFluid.isPresent())
            {
                final FluidInformation fluidStack = primaryTankFluid.get();
                for (RenderType renderType : RenderType.chunkBufferLayers())
                {
                    if (!IRenderTypeManager.getInstance().canRenderInType(fluidStack.fluid().defaultFluidState(), renderType))
                        continue;

                    if (renderType == RenderType.translucent() && Minecraft.useShaderTransparency())
                        renderType = Sheets.translucentCullBlockSheet();

                    final VertexConsumer builder = pBufferSource.getBuffer(renderType);

                    final int maxAmount = useMaximumRelativeAmounts ? (int) tank.getMaximalAmount() : (int) maximalTankSize;
                    final float fullness = (float) fluidStack.amount() / (float) maxAmount;

                    final float heightFactor = Math.min(Math.max(0, fullness), 1f);

                    final int xOffset = determineDirectionalOffset(Direction.Axis.X, pBlockEntity, tankIndex, tanks.size());
                    final int zOffset = determineDirectionalOffset(Direction.Axis.Z, pBlockEntity, tankIndex, tanks.size());
                    final int xWidth = determineAxisWidth(Direction.Axis.X, pBlockEntity, tankIndex, tanks.size());
                    final int zWidth = determineAxisWidth(Direction.Axis.Z, pBlockEntity, tankIndex, tanks.size());

                    final int xEnd = xOffset + xWidth;
                    final int zEnd = zOffset + zWidth;

                    FluidCuboidUtils.renderScaledFluidCuboid(
                            fluidStack,
                            pPoseStack,
                            builder,
                            pPackedLight,
                            pPackedOverlay,
                            xOffset, 6, zOffset,
                            xEnd, 6 + 8 * heightFactor, zEnd
                    );
                }

                tankIndex++;
            }
        }

        final ItemStack stack = pBlockEntity.getItem(0);
        if (!stack.isEmpty()) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.75, 15/16f, 0.75);
            pPoseStack.mulPose(Quaternion.fromXYZDegrees(new Vector3f(45, 45, 0)));
            pPoseStack.scale(0.33f, 0.33f, 0.33f);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
            pPoseStack.popPose();
        }
    }

    private static int determineAxisWidth(final Direction.Axis renderingAxis, final BlockEntity blockEntity, final int tankIndex, final int tankCount)
    {
        if (tankCount == 1 || !blockEntity.getBlockState().hasProperty(HorizontalDirectionalBlock.FACING))
            return 12;

        final int minimalOffset = (int) (tankIndex * (12d / tankCount));
        final Direction.Axis facingAxis = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getAxis();
        if (facingAxis == renderingAxis)
            return 12;

        if (tankIndex == (tankCount - 1))
            return 12 - minimalOffset;
        else
            return (int) (12d / tankCount);
    }

    private static int determineDirectionalOffset(final Direction.Axis renderingAxis, final BlockEntity blockEntity, final int tankIndex, final int tankCount) {
        if (tankCount == 1 || !blockEntity.getBlockState().hasProperty(HorizontalDirectionalBlock.FACING))
            return 2;

        final int minimalOffset = (int) (tankIndex * (12d / tankCount));
        final Direction.Axis facingAxis = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getAxis();
        if (facingAxis != renderingAxis)
            return 2 + minimalOffset;

        return 2;
    }
}
