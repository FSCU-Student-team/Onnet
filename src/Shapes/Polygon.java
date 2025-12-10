package Shapes;

import Physics.Collision.Collider;
import com.jogamp.opengl.GL2;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements Shape {

    private final List<Point> points; // ordered vertices
    private final Color color;
    private final boolean fill;
    private double rotation; // in degrees
    private Point origin;    // center point for rotation

    private Polygon(Builder b) {
        if (b.points.size() < 3)
            throw new IllegalArgumentException("A polygon must have at least 3 points... Pythagoras is watching.");

        // deep copy to avoid external modification
        this.points = new ArrayList<>(b.points);
        this.color = b.color;
        this.fill = b.fill;
        this.rotation = b.rotation;
        this.origin = b.origin != null ? b.origin : calculateCenter();
    }

    public Color getColor() {
        return color;
    }

    private Point calculateCenter() {
        double sumX = 0, sumY = 0;
        for (Point p : points) {
            sumX += p.x();
            sumY += p.y();
        }
        return new Point(sumX / points.size(), sumY / points.size());
    }

    @Override
    public Point getCenter() {
        return origin;
    }

    @Override
    public void setOrigin(Point origin) {
        var v = this.origin.subtract(origin);
        points.replaceAll(p -> p.add(v));
        this.origin = origin;
    }

    @Override
    public double getWidth() {
        double minX = points.stream().mapToDouble(Point::x).min().orElse(0);
        double maxX = points.stream().mapToDouble(Point::x).max().orElse(0);
        return maxX - minX;
    }

    @Override
    public double getHeight() {
        double minY = points.stream().mapToDouble(Point::y).min().orElse(0);
        double maxY = points.stream().mapToDouble(Point::y).max().orElse(0);
        return maxY - minY;
    }

    @Override
    public void scale(double factor) {
        points.replaceAll(point -> origin.add(point.subtract(origin).scale(factor)));
    }

    @Override
    public void rotate(double deltaAngle) {
        double rad = Math.toRadians(deltaAngle);
        points.replaceAll(p -> rotatePoint(p, origin, rad));
        rotation += deltaAngle;
    }

    @Override
    public Collider getCollider() {
        return null; //TODO: implement
    }

    @Override
    public double getRestitution() {
        return 0; //TODO: implement
    }

    protected static Point rotatePoint(Point p, Point center, double rad) {
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
        points.replaceAll(point -> point.add(delta));
        origin = origin.add(delta);
    }

    @Override
    public void draw(GL2 gl) {
        color.useColorGl(gl);

        gl.glPushMatrix();
        gl.glRotated(rotation, 0, 0, 1);

        gl.glBegin(fill ? GL2.GL_POLYGON : GL2.GL_LINE_LOOP);
        for (Point p : points) {
            gl.glVertex2d(p.x(), p.y());
        }
        gl.glEnd();

        gl.glPopMatrix();
    }

    @Override
    public Polygon copy() {
        Builder b = new Builder();
        for (Point p : points)
            b.addPoint(new Point(p.x(), p.y()));
        b.color(color).fill(fill).rotation(rotation).origin(new Point(origin.x(), origin.y()));
        return b.build();
    }

    public static class Builder {
        private final List<Point> points = new ArrayList<>();
        private Color color = Color.BLACK;
        private boolean fill = false;
        private double rotation = 0;
        private Point origin = null;

        public Builder addPoint(Point p) {
            points.add(p);
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder fill(boolean fill) {
            this.fill = fill;
            return this;
        }

        public Builder rotation(double rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder origin(Point origin) {
            this.origin = origin;
            return this;
        }

        public Polygon build() {
            return new Polygon(this);
        }
    }
}
