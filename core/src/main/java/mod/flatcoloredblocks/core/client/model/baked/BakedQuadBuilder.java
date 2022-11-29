package mod.flatcoloredblocks.core.client.model.baked;

import com.communi.suggestu.scena.core.client.models.vertices.IVertexConsumer;
import com.communi.suggestu.scena.core.client.utils.LightUtil;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class BakedQuadBuilder implements IVertexConsumer {
    private static final int SIZE = DefaultVertexFormat.BLOCK.getElements().size();

    private final float[][][] unpackedData = new float[4][SIZE][4];
    private int tint = -1;
    private Direction orientation;
    private TextureAtlasSprite texture;
    private boolean applyDiffuseLighting = true;

    private int vertices = 0;
    private int elements = 0;
    private boolean full = false;

    public BakedQuadBuilder(TextureAtlasSprite texture) {
        this.texture = texture;
    }

    protected BakedQuadBuilder() {
    }

    @Override
    public VertexFormat getVertexFormat() {
        return DefaultVertexFormat.BLOCK;
    }

    @Override
    public void setQuadTint(int tint) {
        this.tint = tint;
    }

    @Override
    public void setQuadOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    @Override
    public void setTexture(TextureAtlasSprite texture) {
        this.texture = texture;
    }

    @Override
    public void setApplyDiffuseLighting(boolean diffuse) {
        this.applyDiffuseLighting = diffuse;
    }

    @Override
    public void put(int vertexIndex, int element, float... data) {
        for (int i = 0; i < 4; i++) {
            if (i < data.length) {
                unpackedData[vertices][element][i] = data[i];
            } else {
                unpackedData[vertices][element][i] = 0;
            }
        }
        elements++;
        if (elements == SIZE) {
            vertices++;
            elements = 0;
        }
        if (vertices == 4) {
            full = true;
        }
    }

    public BakedQuad build() {
        if (!full) {
            throw new IllegalStateException("not enough data");
        }
        if (texture == null) {
            throw new IllegalStateException("texture not set");
        }
        int[] packed = new int[DefaultVertexFormat.BLOCK.getIntegerSize() * 4];
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < SIZE; e++) {
                LightUtil.pack(unpackedData[v][e], packed, DefaultVertexFormat.BLOCK, v, e);
            }
        }
        return new BakedQuad(packed, tint, orientation, texture, applyDiffuseLighting);
    }
}
