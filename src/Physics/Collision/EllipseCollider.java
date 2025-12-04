package Physics.Collision;

import Shapes.Point;
import Shapes.Vector2;

public class EllipseCollider implements Collider {

    private Point center;
    private double radiusX;
    private double radiusY;
    private double rotation;

    @Override
    public AABB getAABB() {
        double cos = Math.cos(Math.toRadians(rotation));
        double sin = Math.sin(Math.toRadians(rotation));

        // width and height of rotated ellipse bounding box
        double w = Math.sqrt((radiusX * cos) * (radiusX * cos) + (radiusY * sin) * (radiusY * sin));
        double h = Math.sqrt((radiusX * sin) * (radiusX * sin) + (radiusY * cos) * (radiusY * cos));

        return new AABB(center.x() - w, center.y() - h, center.x() + w, center.y() + h);
    }

    @Override
    public boolean intersectsCircle(CircleCollider c) {
        double cx = c.getCenter().x() - center.x();
        double cy = c.getCenter().y() - center.y();

        double cos = Math.cos(-Math.toRadians(rotation));
        double sin = Math.sin(-Math.toRadians(rotation));
        double x = cos * cx - sin * cy;
        double y = sin * cx + cos * cy;

        double dx = x / radiusX;
        double dy = y / radiusY;

        double scaledRadius = c.getRadius() / Math.max(radiusX, radiusY);
        return dx*dx + dy*dy <= (1 + scaledRadius) * (1 + scaledRadius);
    }


    @Override
    public boolean intersectsRectangle(RectangleCollider r) {
        return false; //will do later
    }

    @Override
    public boolean intersectsEllipse(EllipseCollider e) {
        return false; //will do later
    }

    @Override
    public boolean intersectsTriangle(TriangleCollider t) {
        return false; //will do later
    }

    @Override
    public Vector2 getMTV(Collider other) {
        return null; //will do later
    }

    public double getRadiusY() {
        return radiusY;
    }

    public void setRadiusY(double radiusY) {
        this.radiusY = radiusY;
    }

    public double getRadiusX() {
        return radiusX;
    }

    public void setRadiusX(double radiusX) {
        this.radiusX = radiusX;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
