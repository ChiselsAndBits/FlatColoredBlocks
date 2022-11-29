package mod.flatcoloredblocks.core.client.model.loader;

import com.communi.suggestu.scena.core.client.models.IModelManager;
import com.communi.suggestu.scena.core.client.models.baked.IDataAwareBakedModel;
import com.communi.suggestu.scena.core.client.models.baked.base.BaseDelegatingSmartModel;
import com.communi.suggestu.scena.core.client.models.baked.simple.NullBakedModel;
import com.communi.suggestu.scena.core.client.models.data.IBlockModelData;
import com.communi.suggestu.scena.core.client.models.loaders.IModelSpecification;
import com.communi.suggestu.scena.core.client.models.loaders.IModelSpecificationLoader;
import com.communi.suggestu.scena.core.client.models.loaders.context.IModelBakingContext;
import com.communi.suggestu.scena.core.client.rendering.type.IRenderTypeManager;
import com.communi.suggestu.scena.core.client.utils.LightUtil;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import mod.flatcoloredblocks.core.block.ColoredBlock;
import mod.flatcoloredblocks.core.client.model.baked.BakedQuadAdapter;
import mod.flatcoloredblocks.core.registrars.ModelDataKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ColoredBlockModelLoader implements IModelSpecificationLoader<ColoredBlockModelLoader.ModelSpecification>
{
    private static final ColoredBlockModelLoader INSTANCE = new ColoredBlockModelLoader();

    public static ColoredBlockModelLoader getInstance()
    {
        return INSTANCE;
    }

    private ColoredBlockModelLoader()
    {
    }

    @Override
    public ModelSpecification read(final JsonDeserializationContext jsonDeserializationContext, final JsonObject jsonObject)
    {
        if (!jsonObject.has("parent"))
            throw new IllegalStateException("Missing parent");

        final ResourceLocation parent = new ResourceLocation(jsonObject.get("parent").getAsString());

        return new ModelSpecification(parent);
    }

    public static class ModelSpecification implements IModelSpecification<ModelSpecification> {

        private final ResourceLocation parentModel;

        public ModelSpecification(final ResourceLocation parentModel) {this.parentModel = parentModel;}

        @Override
        public BakedModel bake(final IModelBakingContext iModelBakingContext, final ModelBakery modelBakery, final Function<Material, TextureAtlasSprite> function, final ModelState modelState, final ResourceLocation resourceLocation)
        {
            final UnbakedModel unbakedModel = modelBakery.getModel(parentModel);
            final BakedModel parentBakedModel = IModelManager.getInstance().adaptToPlatform(unbakedModel.bake(modelBakery, function, modelState, resourceLocation));

            return new Baked(parentBakedModel);
        }

        @Override
        public Collection<Material> getTextures(final IModelBakingContext iModelBakingContext, final Function<ResourceLocation, UnbakedModel> function, final Set<Pair<String, String>> set)
        {
            final UnbakedModel parentModel = function.apply(this.parentModel);

            return parentModel.getMaterials(function, set);
        }
    }

    public static class Baked extends BaseDelegatingSmartModel
    {
        private enum QuadCullingDirection {
            NONE(null, SimpleBakedModel.Builder::addUnculledFace),
            NORTH(Direction.NORTH, (b, q) -> b.addCulledFace(Direction.NORTH, q)),
            SOUTH(Direction.SOUTH, (b, q) -> b.addCulledFace(Direction.SOUTH, q)),
            EAST(Direction.EAST, (b, q) -> b.addCulledFace(Direction.EAST, q)),
            WEST(Direction.WEST, (b, q) -> b.addCulledFace(Direction.WEST, q)),
            UP(Direction.UP, (b, q) -> b.addCulledFace(Direction.UP, q)),
            DOWN(Direction.DOWN, (b, q) -> b.addCulledFace(Direction.DOWN, q));

            private static QuadCullingDirection getFor(final Direction direction) {
                if (direction == null)
                    return NONE;

                return switch (direction)
                               {
                                   case NORTH -> NORTH;
                                   case SOUTH -> SOUTH;
                                   case EAST -> EAST;
                                   case WEST -> WEST;
                                   case UP -> UP;
                                   case DOWN -> DOWN;
                               };
            }

            private final Direction direction;
            private final BiConsumer<SimpleBakedModel.Builder, BakedQuad> quadRegistrar;

            QuadCullingDirection(final Direction direction, final BiConsumer<SimpleBakedModel.Builder, BakedQuad> quadRegistrar)
            {
                this.direction = direction;
                this.quadRegistrar = quadRegistrar;
            }

            public Direction getDirection()
            {
                return direction;
            }

            public void registerQuad(final SimpleBakedModel.Builder builder, final BakedQuad quad)
            {
                quadRegistrar.accept(builder, quad);
            }
        }

        private record QuadRenderData(RenderType renderType, QuadCullingDirection cullingDirection) {}
        private record RecoloredQuadRenderData(QuadRenderData quadRenderData, int color) {}

        private final Map<QuadRenderData, List<BakedQuad>> parentQuads = new ConcurrentHashMap<>();
        private final Map<RecoloredQuadRenderData, List<BakedQuad>> coloredQuads = new ConcurrentHashMap<>();
        private final Map<Integer, BakedModel> stackModels = new ConcurrentHashMap<>();

        public Baked(final BakedModel delegate)
        {
            super(delegate);
        }

        @Override
        public @NotNull Collection<RenderType> getSupportedRenderTypes(final BlockState blockState, final RandomSource randomSource, final IBlockModelData iBlockModelData)
        {
            if (getDelegate() instanceof IDataAwareBakedModel dataAwareBakedModel) {
                return dataAwareBakedModel.getSupportedRenderTypes(blockState, randomSource, iBlockModelData);
            }

            return IRenderTypeManager.getInstance().getRenderTypesFor(this, blockState, randomSource, iBlockModelData);
        }

        @Override
        public @NotNull Collection<RenderType> getSupportedRenderTypes(final ItemStack stack, final boolean fabulous)
        {
            if (getDelegate() instanceof IDataAwareBakedModel dataAwareBakedModel) {
                return dataAwareBakedModel.getSupportedRenderTypes(stack, fabulous);
            }

            if (stack.getItem() instanceof BlockItem blockItem)
            {
                var renderTypes = getSupportedRenderTypes(blockItem.getBlock().defaultBlockState(), RandomSource.create(42), IBlockModelData.empty());
                if (renderTypes.contains(RenderType.translucent()))
                {
                    return Collections.singletonList(fabulous || !Minecraft.useShaderTransparency() ? Sheets.translucentCullBlockSheet() : Sheets.translucentItemSheet());
                }

                return Collections.singletonList(Sheets.cutoutBlockSheet());
            }
            return Collections.singletonList(fabulous ? Sheets.translucentCullBlockSheet() : Sheets.translucentItemSheet());
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable final BlockState state, @Nullable final Direction side, @NotNull final RandomSource rand, @NotNull final IBlockModelData extraData, @Nullable final RenderType renderType)
        {
            if (!getSupportedRenderTypes(state, rand, extraData).contains(renderType))
                return Collections.emptyList();

            final QuadRenderData quadRenderData = new QuadRenderData(renderType, QuadCullingDirection.getFor(side));
            if (!parentQuads.containsKey(quadRenderData)) {
                parentQuads.put(quadRenderData, buildParentQuadData(state, side, rand, extraData, renderType));
            }

            final List<BakedQuad> parentQuads = this.parentQuads.get(quadRenderData);
            if (!extraData.hasProperty(ModelDataKeys.COLOR))
                return parentQuads;

            final Integer color = extraData.getData(ModelDataKeys.COLOR);
            if (color == null)
                return parentQuads;

            final RecoloredQuadRenderData recoloredQuadRenderData = new RecoloredQuadRenderData(quadRenderData, color);
            if (!coloredQuads.containsKey(recoloredQuadRenderData)) {
                coloredQuads.put(recoloredQuadRenderData, buildRecoloredQuadData(parentQuads, color));
            }

            return coloredQuads.get(recoloredQuadRenderData);
        }

        @Override
        public BakedModel resolve(final BakedModel originalModel, final ItemStack stack, final Level world, final LivingEntity entity)
        {
            if (stack.isEmpty())
                return NullBakedModel.instance;

            if (!(stack.getItem() instanceof BlockItem blockItem))
                return NullBakedModel.instance;

            if (!(blockItem.getBlock() instanceof ColoredBlock block))
                return NullBakedModel.instance;

            final int color = block.getColor(stack);

            if (!stackModels.containsKey(color)) {
                stackModels.put(color, buildStackModel(stack, color));
            }

            return stackModels.get(color);
        }

        private List<BakedQuad> buildParentQuadData(final BlockState state, final Direction side, final RandomSource rand, final IBlockModelData extraData, final RenderType renderType)
        {
            if (getDelegate() instanceof IDataAwareBakedModel dataAwareBakedModel) {
                return dataAwareBakedModel.getQuads(state, side, rand, extraData, renderType);
            }

            return getDelegate().getQuads(state, side, rand);
        }

        private List<BakedQuad> buildRecoloredQuadData(final List<BakedQuad> parentQuads, final Integer color)
        {
            final List<BakedQuad> coloredQuads = new ArrayList<>(parentQuads.size());
            for (final BakedQuad parentQuad : parentQuads) {
                final BakedQuadAdapter adapter = new BakedQuadAdapter(color);
                LightUtil.put(adapter, parentQuad);
                coloredQuads.add(adapter.build());
            }

            return coloredQuads;
        }

        private BakedModel buildStackModel(final ItemStack stack, final int color)
        {
            final SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(
                    useAmbientOcclusion(),
                    usesBlockLight(),
                    isGui3d(),
                    getDelegate().getTransforms(),
                    new ItemOverrides()
            );

            for (final QuadCullingDirection value : QuadCullingDirection.values())
            {
                final Direction direction = value.getDirection();
                final QuadRenderData quadRenderData = new QuadRenderData(ItemBlockRenderTypes.getRenderType(stack, true), value);
                if (!parentQuads.containsKey(quadRenderData)) {
                    parentQuads.put(quadRenderData, buildParentQuadData(null, direction, RandomSource.create(), IBlockModelData.empty(), ItemBlockRenderTypes.getRenderType(stack, true)));
                }

                final List<BakedQuad> parentQuads = this.parentQuads.get(quadRenderData);
                final RecoloredQuadRenderData recoloredQuadRenderData = new RecoloredQuadRenderData(quadRenderData, color);
                if (!coloredQuads.containsKey(recoloredQuadRenderData)) {
                    coloredQuads.put(recoloredQuadRenderData, buildRecoloredQuadData(parentQuads, color));
                }

                final List<BakedQuad> coloredQuads = this.coloredQuads.get(recoloredQuadRenderData);
                for (final BakedQuad coloredQuad : coloredQuads) {
                    value.registerQuad(builder, coloredQuad);
                }
            }

            builder.particle(getDelegate().getParticleIcon());

            return builder.build();
        }
    }
}
