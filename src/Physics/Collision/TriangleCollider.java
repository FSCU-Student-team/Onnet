package Physics.Collision;

import Shapes.Point;
import Shapes.Vector2;

public class TriangleCollider implements Collider {

    private Point A;
    private Point B;
    private Point C;

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
        return null;
    }

    public Point getA() {
        return A;
    }

    public void setA(Point a) {
        A = a;
    }

    public Point getB() {
        return B;
    }

    public void setB(Point b) {
        B = b;
    }

    public Point getC() {
        return C;
    }

    public void setC(Point c) {
        C = c;
    }
}
