package mod.flatcoloredblocks.core.util;

public class WeightedAverage {

    final float[] weights;

    public WeightedAverage(float... weights) {
        this.weights = weights;
    }

    public float value(final float... values) {
        if (values.length != weights.length)
            throw new IllegalArgumentException("values.length != weights.length");

        float result = 0;
        for (int i = 0; i < values.length; i++) {
            result += values[i] * weights[i];
        }
        return result;
    }
}
