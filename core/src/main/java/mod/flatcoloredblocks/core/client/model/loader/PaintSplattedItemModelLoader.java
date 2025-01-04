package mod.flatcoloredblocks.core.client.model.loader;

import com.communi.suggestu.scena.core.client.models.CombiningModel;
import com.communi.suggestu.scena.core.client.models.RenderTypeGroup;
import com.communi.suggestu.scena.core.client.models.UnbakedGeometryHelper;
import com.communi.suggestu.scena.core.client.models.loaders.IModelSpecification;
import com.communi.suggestu.scena.core.client.models.loaders.IModelSpecificationLoader;
import com.communi.suggestu.scena.core.client.models.loaders.context.IModelBakingContext;
import com.communi.suggestu.scena.core.client.models.loaders.context.SimpleModelBakingContext;
import com.communi.suggestu.scena.core.client.models.state.SimpleModelState;
import com.communi.suggestu.scena.core.fluid.FluidInformation;
import com.communi.suggestu.scena.core.fluid.IFluidManager;
import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import mod.flatcoloredblocks.core.registrars.Fluids;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import static com.communi.suggestu.scena.core.util.TransformationUtils.quatFromXYZ;

public final class PaintSplattedItemModelLoader implements IModelSpecificationLoader<PaintSplattedItemModelLoader.PaintSplattedItemModel>
{
    private static final PaintSplattedItemModelLoader INSTANCE = new PaintSplattedItemModelLoader();

    public static PaintSplattedItemModelLoader getInstance()
    {
        return INSTANCE;
    }

    private PaintSplattedItemModelLoader()
    {
    }

    @Override
    public PaintSplattedItemModel read(final JsonDeserializationContext jsonDeserializationContext, final JsonObject jsonObject)
    {
        return new PaintSplattedItemModel();
    }

    public static final class PaintSplattedItemModel implements IModelSpecification<PaintSplattedItemModel> {

        // Depth offsets to prevent Z-fighting
        private static final Transformation FLUID_TRANSFORM = getMatrix(0, 0, 0, 0, 0, 0, 1.005f);

        private static Transformation getMatrix(
                final float transX,
                final float transY,
                final float transZ,
                final float rotX,
                final float rotY,
                final float rotZ,
                final float scaleXYZ )
        {
            final Vector3f translation = new Vector3f( transX, transY, transZ );
            final Vector3f scale = new Vector3f( scaleXYZ, scaleXYZ, scaleXYZ );
            final Quaternionf rotation = quatFromXYZ(new Vector3f(rotX, rotY, rotZ), true);

            return new Transformation(translation, rotation, scale, null);
        }

        public static RenderTypeGroup getLayerRenderTypes()
        {
            return new RenderTypeGroup(RenderType.translucent(), RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS));
        }

        @NotNull
        private static Material getFluidMaterial()
        {
            final ResourceLocation stillFluidTexture = Fluids.PAINT.variantHandler().get()
                                                               .getStillTexture(new FluidInformation(Fluids.PAINT.fluid().get())).orElseThrow();

            return new Material(TextureAtlas.LOCATION_BLOCKS, stillFluidTexture);
        }

        @Override
        public BakedModel bake(IModelBakingContext iModelBakingContext, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
            if (iModelBakingContext.getMaterial("base").isEmpty())
            {
                throw new IllegalArgumentException("Missing base material");
            }

            if (iModelBakingContext.getMaterial("paint").isEmpty())
            {
                throw new IllegalArgumentException("Missing paint material");
            }

            final Material baseMaterial = iModelBakingContext.getMaterial("base").get();
            final Material fluidMaskMaterial = iModelBakingContext.getMaterial("paint").get();
            Material fluidMaterial = getFluidMaterial();

            TextureAtlasSprite baseSprite = spriteGetter.apply(baseMaterial);
            TextureAtlasSprite fluidSprite = spriteGetter.apply(fluidMaterial);

            // We need to disable GUI 3D and block lighting for this to render properly
            var itemContext = SimpleModelBakingContext.SimpleModelBakingContextBuilder.builder().withIsGui3d(false).withUseBlockLight(false).build();
            var modelBuilder = CombiningModel.Baked.builder(itemContext, fluidSprite, iModelBakingContext.getItemOverrides(modelBaker), iModelBakingContext.getTransforms());

            var renderTypes = getLayerRenderTypes();

            if (baseSprite != null)
            {
                // Base texture
                var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, baseSprite.contents());
                UnbakedGeometryHelper.bakeElements(modelBuilder, unbaked, name -> baseSprite, modelState, renderTypes);
            }

            if (fluidSprite != null)
            {
                TextureAtlasSprite templateSprite = spriteGetter.apply(fluidMaskMaterial);
                if (templateSprite != null)
                {
                    // Fluid layer
                    var transformedState = new SimpleModelState(modelState.getRotation().compose(FLUID_TRANSFORM), modelState.isUvLocked());
                    var unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(1, templateSprite); // Use template as mask
                    UnbakedGeometryHelper.bakeElements(modelBuilder, unbaked, $ -> fluidSprite, transformedState, renderTypes); // Bake with fluid texture
                }
            }

            modelBuilder.setParticle(fluidSprite);

            return modelBuilder.build();
        }
    }


}
