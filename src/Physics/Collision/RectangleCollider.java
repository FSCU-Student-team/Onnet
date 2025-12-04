package Physics.Collision;

import Shapes.Point;
import Shapes.Vector2;

public class RectangleCollider implements Collider {

    private Point origin;
    private double width;
    private double height;
    private double rotation;
    private double cosRot, sinRot;
    private AABB aabb;

    public RectangleCollider(Point origin, double width, double height, double rotation) {
        this.origin = origin;
        this.width = width;
        this.height = height;
        setRotation(rotation);
    }

    @Override
    public AABB getAABB() {
        return aabb;
    }

    @Override
    public boolean intersectsCircle(CircleCollider c) {
        if (!c.getAABB().intersects(aabb)) return false;

        final double hw = width / 2;
        final double hh = height / 2;
        final Point centerR = new Point(origin.x() + hw, origin.y() + hh);

        // translate circle into rectangle local space
        final double dx = c.getCenter().x() - centerR.x();
        final double dy = c.getCenter().y() - centerR.y();

        // rotate by -rectangle.rotation (cached)
        final double localX = cosRot * dx + sinRot * dy; // -angle rotation simplified
        final double localY = -sinRot * dx + cosRot * dy;

        // closest point on rectangle
        final double closestX = Math.max(-hw, Math.min(localX, hw));
        final double closestY = Math.max(-hh, Math.min(localY, hh));

        // distance squared to circle
        final double distSq = (localX - closestX) * (localX - closestX) + (localY - closestY) * (localY - closestY);
        return distSq <= c.getRadius() * c.getRadius();
    }

    @Override
    public boolean intersectsRectangle(RectangleCollider r) {
        return false; // will do later
    }

    @Override
    public boolean intersectsEllipse(EllipseCollider e) {
        return false; // will do later
    }

    @Override
    public boolean intersectsTriangle(TriangleCollider t) {
        return false; // will do later
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
        updateAABB();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        updateAABB();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        updateAABB();
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        // cache trig
        final double rad = Math.toRadians(rotation);
        cosRot = Math.cos(rad);
        sinRot = Math.sin(rad);
        updateAABB();
    }

    private void updateAABB() {
        final double hw = width / 2;
        final double hh = height / 2;
        final Point centerR = new Point(origin.x() + hw, origin.y() + hh);

        final double[] xs = new double[4];
        final double[] ys = new double[4];

        final double[] dxs = {-hw, hw, hw, -hw};
        final double[] dys = {-hh, -hh, hh, hh};

        for (int i = 0; i < 4; i++) {
            xs[i] = centerR.x() + cosRot * dxs[i] - sinRot * dys[i];
            ys[i] = centerR.y() + sinRot * dxs[i] + cosRot * dys[i];
        }

        final double minX = Math.min(Math.min(xs[0], xs[1]), Math.min(xs[2], xs[3]));
        final double maxX = Math.max(Math.max(xs[0], xs[1]), Math.max(xs[2], xs[3]));
        final double minY = Math.min(Math.min(ys[0], ys[1]), Math.min(ys[2], ys[3]));
        final double maxY = Math.max(Math.max(ys[0], ys[1]), Math.max(ys[2], ys[3]));

        aabb = new AABB(minX, minY, maxX, maxY);
    }
}
