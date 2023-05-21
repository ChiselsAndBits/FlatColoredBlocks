package mod.flatcoloredblocks.core.util;

public class Triangle {

    public static Triangle fromCoordinates(Coordinate a, Coordinate b, Coordinate c) {
        return new Triangle(a, b, c);
    }

    public static Triangle fromCircleSection(final float radius, final float startAngle, final float endAngle) {
        return new Triangle(
                Coordinate.polar(0, 0),
                Coordinate.polar(radius, endAngle),
                Coordinate.polar(radius, startAngle)
        );
    }

    private final Coordinate a;
    private final Coordinate b;
    private final Coordinate c;

    private Triangle(Coordinate a, Coordinate b, Coordinate c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Coordinate getA() {
        return a;
    }

    public Coordinate getB() {
        return b;
    }

    public Coordinate getC() {
        return c;
    }

    public WeightedAverage getBarycentricWeights(final Coordinate r) {
        final float[] weights = new float[3];

        final float x = r.getX();
        final float y = r.getY();

        final float x1 = a.getX();
        final float y1 = a.getY();

        final float x2 = b.getX();
        final float y2 = b.getY();

        final float x3 = c.getX();
        final float y3 = c.getY();

        final float denominator = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        weights[0] = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / denominator;
        weights[1] = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / denominator;
        weights[2] = 1 - weights[0] - weights[1];

        return new WeightedAverage(weights);
    }

    public float calculateValue(final Coordinate r, final float... values) {
        return getBarycentricWeights(r).value(values);
    }
}
