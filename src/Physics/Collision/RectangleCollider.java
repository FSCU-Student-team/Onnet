package Physics.Collision;

import Shapes.Point;
import Shapes.Vector2;

public class RectangleCollider implements Collider {

    private Point origin;
    private double width;
    private double height;
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

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public double getWidth(){
        return width;
    }

    public void setWidth(double width){
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
