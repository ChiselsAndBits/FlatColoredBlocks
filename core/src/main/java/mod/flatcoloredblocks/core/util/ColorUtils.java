package mod.flatcoloredblocks.core.util;

public final class ColorUtils {

    private ColorUtils() {
        throw new IllegalStateException("Can not instantiate an instance of: ColorUtils. This is a utility class");
    }

    public static float[] HSVToRGB(final float h, final float s, final float v) {
        float hueIndex = (h % 360) / 60f;
        int i = (int) hueIndex;
        float diff = hueIndex - i;
        float p = v * (1.0f - s);
        float q = v * (1.0f - (s * diff));
        float t = v * (1.0f - (s * (1.0f - diff)));

        return switch (i) {
            case 0 -> toRGBArray(v, t, p);
            case 1 -> toRGBArray(q, v, p);
            case 2 -> toRGBArray(p, v, t);
            case 3 -> toRGBArray(p, q, v);
            case 4 -> toRGBArray(t, p, v);
            default -> toRGBArray(v, p, q);
        };
    }

    public static float[] RGBToHSV(final float r, final float g, final float b) {
        float min = MathUtils.min(r, g, b);
        float max = MathUtils.max(r, g, b);
        float delta = max - min;
        float[] ret = new float[3];
        if (delta == 0) {
            ret[0] = 0;
        } else if (max == r) {
            ret[0] = ((g - b) / delta) % 6;
        } else if (max == g) {
            ret[0] = ((b - r) / delta) + 2;
        } else {
            ret[0] = ((r - g) / delta) + 4;
        }
        ret[0] *= 60;
        if (ret[0] < 0) {
            ret[0] += 360;
        }
        ret[1] = max == 0 ? 0 : delta / max;
        ret[2] = max;
        return ret;
    }

    public static float[] toRGBArray(final int color) {
        return new float[] { ((color >> 16) & 0xFF) / 255.0f, ((color >> 8) & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, ((color >> 24) & 0xFF) / 255.0f };
    }

    public static float[] toRGBArray(final float r, final float g, final float b) {
        return new float[] { r, g, b, 1.0f };
    }

    public static int fromRGBArray(final float[] rgb) {
        return fromRGB(rgb[0], rgb[1], rgb[2]);
    }

    public static int fromRGB(final float r, final float g, final float b) {
        final int red = (((int) (r * 255)) & 0xFF) << 16;
        final int green = (((int) (g * 255)) & 0xFF) << 8;
        final int blue = (((int) (b * 255)) & 0xFF);
        final int alpha = (0xFF) << 24;

        return alpha | red | green | blue;
    }

    public static float getRed(int current) {
        return toRGBArray(current)[0];
    }

    public static float getGreen(int current) {
        return toRGBArray(current)[1];
    }

    public static float getBlue(int current) {
        return toRGBArray(current)[2];
    }
}
