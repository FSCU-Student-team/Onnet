package Shapes;

public record Point(double x, double y) {

    //Creates a copy of this point
    public Point copy() {
        return new Point(x, y);
    }

    //Calculates the distance to another point
    public double distanceTo(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    //Adds a vector to this point (point plus vector = point)
    public Point add(Vector2 v) {
        return new Point(this.x + v.x(), this.y + v.y());
    }

    //Subtracts another point from this point (point - point = vector)
    public Vector2 subtract(Point other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }
}

