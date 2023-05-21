package mod.flatcoloredblocks.core.util;

public class NormalDistribution {

    private final float standardDeviation;
    private final float mean;

    public NormalDistribution(float standardDeviation, float mean) {
        this.standardDeviation = standardDeviation;
        this.mean = mean;
    }

    public float get(final float x) {
        return (float) (1 / (standardDeviation * Math.sqrt(2 * Math.PI)) * Math.exp(-0.5 * Math.pow((x - mean) / standardDeviation, 2)));
    }

    public float getNormalized(final float x) {
        return get(x) / get(mean);
    }
}
