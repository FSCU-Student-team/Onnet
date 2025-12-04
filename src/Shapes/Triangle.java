package Shapes;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;

public class Triangle implements Shape {

    private final ArrayList<Point> points; // always size 3, will throw if you attempt to add more than 3
    private final Color color;
    private final boolean fill;

    public Triangle(Builder b) {
        if (b.points.size() != 3)
            throw new IllegalStateException("Triangle must have exactly 3 points.");

        // deep copy to avoid external modification
        this.points = new ArrayList<>(b.points);
        this.color = b.color;
        this.fill = b.filled;
    }

    // Quality of life: to avoid repition
    private Point p1() {
        return points.getFirst();
    }

    private Point p2() {
        return points.get(1);
    }

    private Point p3() {
        return points.get(2);
    }

    @Override
    public Point getCenter() {
        return new Point(
                (p1().x() + p2().x() + p3().x()) / 3.0,
                (p1().y() + p2().y() + p3().y()) / 3.0
        );
    }

    @Override
    public void setOrigin(Point newCenter) {
        Point oldCenter = getCenter();
        Vector2 delta = newCenter.subtract(oldCenter);

        for (int i = 0; i < 3; i++)
            points.set(i, points.get(i).add(delta));
    }

    // Bounds
    @Override
    public double getWidth() {
        double min = Math.min(p1().x(), Math.min(p2().x(), p3().x()));
        double max = Math.max(p1().x(), Math.max(p2().x(), p3().x()));
        return max - min;
    }

    @Override
    public double getHeight() {
        double min = Math.min(p1().y(), Math.min(p2().y(), p3().y()));
        double max = Math.max(p1().y(), Math.max(p2().y(), p3().y()));
        return max - min;
    }

    // Scale
    @Override
    public void Scale(double factor) {
        Point c = getCenter();

        for (int i = 0; i < 3; i++) {
            Vector2 v = points.get(i).subtract(c).scale(factor);
            points.set(i, c.add(v));
        }
    }

    // -------------------------------
    // Rotate
    // -------------------------------
    @Override
    public void Rotate(double deltaAngle) {
        double rad = Math.toRadians(deltaAngle);
        Point c = getCenter();

        for (int i = 0; i < 3; i++)
            points.set(i, rotatePoint(points.get(i), c, rad));
    }

    private Point rotatePoint(Point p, Point center, double rad) {
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        double dx = p.x() - center.x();
        double dy = p.y() - center.y();

        return new Point(
                center.x() + cos * dx - sin * dy,
                center.y() + sin * dx + cos * dy
        );
    }

    // -------------------------------
    // Move
    // -------------------------------
    @Override
    public void Move(double x, double y) {
        Vector2 delta = new Vector2(x, y);

        for (int i = 0; i < 3; i++)
            points.set(i, points.get(i).add(delta));
    }

    // -------------------------------
    // Draw
    // -------------------------------
    @Override
    public void Draw(GL2 gl) {
        color.useColorGl(gl);

        gl.glBegin(fill ? GL2.GL_TRIANGLES : GL2.GL_LINE_LOOP);

        gl.glVertex2d(p1().x(), p1().y());
        gl.glVertex2d(p2().x(), p2().y());
        gl.glVertex2d(p3().x(), p3().y());

        gl.glEnd();
    }

    //provides a deep copy
    @Override
    public Triangle Copy() {
        return new Builder()
                .addPoint(p1())
                .addPoint(p2())
                .addPoint(p3())
                .color(color)
                .isFilled(fill)
                .build();
    }

    public static class Builder {

        private final ArrayList<Point> points = new ArrayList<>();
        private Color color = Color.BLACK;
        private boolean filled = false;

        public static Builder builder() {
            return new Builder();
        }

        public Builder isFilled(boolean filled) {
            this.filled = filled;
            return this;
        }

        public Builder addPoint(Point point) {
            if (points.size() == 3)
                throw new IllegalArgumentException("Can't add more than 3 points to a triangle... you woke up Pythagoras his grave by the way.");

            points.add(point);
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Triangle build() {
            return new Triangle(this);
        }
    }
}
