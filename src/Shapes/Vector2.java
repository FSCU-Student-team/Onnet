package Shapes;

public record Vector2(double x, double y) {

    //Static constants for common vectors.
    public static final Vector2 ZERO = new Vector2(0, 0);
    public static final Vector2 ONE = new Vector2(1, 1);
    public static final Vector2 UNIT_X = new Vector2(1, 0);
    public static final Vector2 UNIT_Y = new Vector2(0, 1);

    private static final double EPSILON = 1e-5;


    //adds two vectors together.
    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    //subtracts two vectors from each other.
    public Vector2 subtract(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }


    //scales a vector by a scalar.
    public Vector2 scale(double s) {
        return new Vector2(x * s, y * s);
    }

    //multiplies two vectors together.
    public Vector2 multiply(Vector2 v) {
        return new Vector2(x * v.x, y * v.y);
    }

    //divides a vector by a scalar.
    public Vector2 divide(double s) {
        return new Vector2(x / s, y / s);
    }

    //returns the length of a vector.
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    //returns the squared length of a vector.
    public double magnitudeSquared() {
        return x * x + y * y;
    }

    //returns a normalized vector. (returns a unit vector direction with minimum value 0 and maximum value 1)
    public Vector2 normalize() {
        double mag = magnitude();
        return mag < EPSILON ? ZERO : new Vector2(x / mag, y / mag);
    }

    //normalizes a vector and scales by a given length.
    public Vector2 normalize(double length) {
        return normalize().scale(length);
    }

    //clamps the length of a vector between 0 and max.
    public Vector2 clampMagnitude(double max) {
        double magSq = magnitudeSquared();
        if (magSq <= max * max) return this;
        return normalize().scale(max);
    }

    //returns the dot product of two vectors.
    public double dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    //returns the cross product of two vectors.
    public double cross(Vector2 v) {
        return x * v.y - y * v.x;
    }

    //returns the angle of a vector in degrees.
    public double angle() {
        return Math.toDegrees(Math.atan2(y, x));
    }

    //returns the angle between two vectors in degrees.
    public double angleBetween(Vector2 v) {
        double dot = dot(v);
        double mags = magnitude() * v.magnitude();
        if (mags < EPSILON) return 0;
        double t = dot / mags;
        t = Math.max(-1, Math.min(1, t));
        return Math.toDegrees(Math.acos(t));
    }

    //rotates a vector by a given angle (degrees).
    public Vector2 rotate(double angle) {
        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));
        return new Vector2(
                cos * x - sin * y,
                sin * x + cos * y
        );
    }

    // returns a perpendicular vector to this vector.
    public Vector2 perpendicular() {
        return new Vector2(-y, x);
    }

    // returns a projection of this vector onto another vector. (useful for collision)
    public Vector2 projectionOnto(Vector2 v) {
        double magSq = v.magnitudeSquared();
        if (magSq < EPSILON) return ZERO;
        return v.scale(this.dot(v) / magSq);
    }

    // returns a vector reflected off a surface with the given normal.
    public Vector2 reflect(Vector2 normal) {
        Vector2 n = normal.normalize();
        return this.subtract(n.scale(2 * this.dot(n)));
    }


    // returns the distance between two vectors.
    public double distanceTo(Vector2 v) {
        return Math.sqrt(distanceSquaredTo(v));
    }

    // returns the squared distance between two vectors.
    public double distanceSquaredTo(Vector2 v) {
        double dx = x - v.x;
        double dy = y - v.y;
        return dx * dx + dy * dy;
    }

    // self explanatory
    public boolean isZero() {
        return magnitudeSquared() < EPSILON;
    }

    //returns the same vector with a different x value.
    public Vector2 withX(double x) {
        return new Vector2(x, this.y);
    }

    //returns the same vector with a different y value.
    public Vector2 withY(double y) {
        return new Vector2(this.x, y);
    }

    //returns the negative of a vector.
    public Vector2 negate() {
        return new Vector2(-x, -y);
    }


}
