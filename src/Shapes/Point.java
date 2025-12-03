package Shapes;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Creates a copy of this point
     */
    public Point copy() {
        return new Point(x, y);
    }

    /**
     * Calculates the distance to another point
     */
    public double distanceTo(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Adds a vector to this point (point + vector = point)
     */
    public Point add(Vector2 v) {
        return new Point(this.x + v.x(), this.y + v.y());
    }

    /**
     * Subtracts another point from this point (point - point = vector)
     */
    public Vector2 subtract(Point other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }
}

