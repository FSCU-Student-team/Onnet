package Physics.Collision;

import Shapes.Point;
import Shapes.Vector2;

import java.util.ArrayList;

public class TriangleCollider implements Collider {

    private Point A;
    private Point B;
    private Point C;

    public TriangleCollider(Point A, Point B, Point C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

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

    public void setPoints(ArrayList<Point> points) {
        A = points.getFirst();
        B = points.get(1);
        C = points.get(2);
    }

    public Point getA() {
        return new Point(A.x(), A.y());
    }

    public Point getB(){
        return new Point(B.x(), B.y());
    }

    public Point getC(){
        return new Point(C.x(), C.y());
    }

}
