package mod.flatcoloredblocks.core.util;

public interface Coordinate {

    static Coordinate cartesian(float x, float y) {
        return new CartesianCoordinate(x, y);
    }

    static Coordinate polar(float radius, float angle) {
        return new PolarCoordinate(radius, angle);
    }

    float getX();

    float getY();

    float getRadius();

    float getAngle();

    final class PolarCoordinate implements Coordinate {
        private final float radius;
        private final float angle;

        private PolarCoordinate(float radius, float angle) {
            this.radius = radius;
            this.angle = angle;
        }

        @Override
        public float getX() {
            return (float) (radius * Math.cos(angle));
        }

        @Override
        public float getY() {
            return (float) (radius * Math.sin(angle));
        }

        @Override
        public float getRadius() {
            return radius;
        }

        @Override
        public float getAngle() {
            return angle;
        }
    }

    final class CartesianCoordinate implements Coordinate {
        private final float x;
        private final float y;

        private CartesianCoordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }

        @Override
        public float getRadius() {
            return (float) Math.sqrt(x * x + y * y);
        }

        @Override
        public float getAngle() {
            return (float) Math.atan2(y, x);
        }
    }
}
