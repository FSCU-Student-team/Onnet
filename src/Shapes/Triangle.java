package Shapes;

import Physics.Collision.Collider;
import Physics.Collision.TriangleCollider;
import com.jogamp.opengl.GL2;

import java.util.ArrayList;

public class Triangle implements Shape {

    private final ArrayList<Point> points; // always size 3, will throw if you attempt to add more than 3
    private final Color color;
    private final boolean fill;
    private final double restitution;

    private TriangleCollider collider;

    public Triangle(Builder b) {
        if (b.points.size() != 3)
            throw new IllegalStateException("Triangle must have exactly 3 points.");

        this.points = new ArrayList<>(b.points);
        this.color = b.color;
        this.fill = b.filled;
        this.restitution = b.restitution;

        collider = new TriangleCollider(points);
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
    public void scale(double factor) {
        Point c = getCenter();
        for (int i = 0; i < 3; i++) {
            Vector2 v = points.get(i).subtract(c).scale(factor);
            points.set(i, c.add(v));
        }
        collider.setPoints(points);
    }

    @Override
    public void rotate(double deltaAngle) {
        double rad = Math.toRadians(deltaAngle);
        Point c = getCenter();

        for (int i = 0; i < 3; i++) {
            points.set(i, rotatePoint(points.get(i), c, rad));
        }

        collider.setPoints(points);
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public double getRestitution() {
        return restitution;
    }

    //helper method, rotates each point individually
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


    @Override
    public void move(Vector2 delta) {
        for (int i = 0; i < 3; i++) {
            points.set(i, points.get(i).add(delta));
            collider.setPoints(points);
        }
    }


    @Override
    public void draw(GL2 gl) {
        color.useColorGl(gl);

        gl.glBegin(fill ? GL2.GL_TRIANGLES : GL2.GL_LINE_LOOP);

        gl.glVertex2d(p1().x(), p1().y());
        gl.glVertex2d(p2().x(), p2().y());
        gl.glVertex2d(p3().x(), p3().y());

        gl.glEnd();
    }

    //provides a deep copy
    @Override
    public Triangle copy() {
        return new Builder()
                .addPoint(p1())
                .addPoint(p2())
                .addPoint(p3())
                .color(color)
                .restitution(restitution)
                .fill(fill)
                .build();
    }

    public static class Builder {

        private final ArrayList<Point> points = new ArrayList<>();
        private Color color = Color.BLUE;
        private boolean filled = false;
        private double restitution = 0;


        public static Builder builder() {
            return new Builder();
        }

        public Builder fill(boolean filled) {
            this.filled = filled;
            return this;
        }

        public Builder addPoint(Point point) {
            points.add(point);
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder restitution(double restitution) {
            this.restitution = restitution;
            return this;
        }

        public Triangle build() {
            if (points.size() != 3) {
                //if you get this exception, the professors will want you dead or alive
                throw new IllegalArgumentException("A triangle must have only 3 points... you woke up Pythagoras his grave by the way.");
            }
            return new Triangle(this);
        }
    }
}
