package mod.flatcoloredblocks.core.util;

public final class MathUtils {

    private MathUtils() {
        throw new IllegalStateException("Can not instantiate an instance of: MathUtils. This is a utility class");
    }

    public static float min(float... vals) {
        double min = vals[0];
        for (int i = 1; i < vals.length; i++) {
            min = Math.min(min, vals[i]);
        }
        return (float) min;
    }

    public static float max(float... vals) {
        double max = vals[0];
        for (int i = 1; i < vals.length; i++) {
            max = Math.max(max, vals[i]);
        }
        return (float) max;
    }
}
