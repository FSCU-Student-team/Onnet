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
        return null;
    }

    @Override
    public boolean intersectsCircle(CircleCollider c) {
        return false;
    }

    @Override
    public boolean intersectsRectangle(RectangleCollider r) {
        return false;
    }

    @Override
    public boolean intersectsEllipse(EllipseCollider e) {
        return false;
    }

    @Override
    public boolean intersectsTriangle(TriangleCollider t) {
        return false;
    }

    @Override
    public Vector2 getMTV(Collider other) {
        return null;
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
