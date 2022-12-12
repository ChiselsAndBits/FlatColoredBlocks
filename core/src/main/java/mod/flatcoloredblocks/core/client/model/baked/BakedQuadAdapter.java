package mod.flatcoloredblocks.core.client.model.baked;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BakedQuadAdapter extends BakedQuadBuilder {

    private final int colorOverride;

    public BakedQuadAdapter(int colorOverride) {
        super();
        this.colorOverride = colorOverride;
    }

    @Override
    public void put(final int vertexIndex,
                    final int elementIndex,
                    final float @NotNull ... data) {
        final VertexFormat format = getVertexFormat();
        final VertexFormatElement element = format.getElements().get(elementIndex);

        if (element.getUsage() == VertexFormatElement.Usage.COLOR) {
            final float[] colorData = new float[4];
            colorData[0] = ((this.colorOverride >> 16) & 0xFF) / 255.0F;
            colorData[1] = ((this.colorOverride >> 8) & 0xFF) / 255.0F;
            colorData[2] = (this.colorOverride & 0xFF) / 255.0F;
            colorData[3] = 1f;
            super.put(vertexIndex, elementIndex, colorData);
        } else {
            super.put(vertexIndex, elementIndex, data);
        }
    }
}
