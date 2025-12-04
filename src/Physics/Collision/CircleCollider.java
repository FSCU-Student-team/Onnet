package Physics.Collision;

import Shapes.Point;
import Shapes.Vector2;

public class CircleCollider implements Collider {

    private Point center;
    private double radius;
    private AABB aabb;

    public CircleCollider(Point center, double radius) {
        this.center = center;
        this.radius = radius;
        aabb = new AABB(
                center.x() - radius,
                center.y() - radius,
                center.x() + radius,
                center.y() + radius
        );
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
        aabb = new AABB(center.x() - radius, center.y() - radius, center.x() + radius, center.y() + radius);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        aabb = new AABB(center.x() - radius, center.y() - radius, center.x() + radius, center.y() + radius);
    }

    @Override
    public AABB getAABB() {
        return aabb;
    }

    //collision circle-circle
    @Override
    public boolean intersectsCircle(CircleCollider c) {
        double r = radius + c.radius;

        Vector2 diff = center.subtract(c.center);
        return diff.magnitudeSquared() <= r * r;
    }

    //collision circle-rectangle
    @Override
    public boolean intersectsRectangle(RectangleCollider r) {
        // Rectangle properties
        double hw = r.getWidth() / 2;
        double hh = r.getHeight() / 2;

        Point centerR = new Point(r.getOrigin().x() + hw, r.getOrigin().y() + hh); // rectangle center
        double angle = Math.toRadians(r.getRotation()); // rotation in degrees

        // Translate circle to rectangle local space
        double cx = center.x() - centerR.x();
        double cy = center.y() - centerR.y();

        // Rotate circle by -angle
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double localX = cos * cx + sin * cy;
        double localY = -sin * cx + cos * cy;

        // Closest point on rectangle in local space
        double closestX = Math.max(-hw, Math.min(localX, hw));
        double closestY = Math.max(-hh, Math.min(localY, hh));

        // Distance to circle center
        double dx = localX - closestX;
        double dy = localY - closestY;

        return dx * dx + dy * dy <= radius * radius;
    }

    //collision circle-ellipse
    @Override
    public boolean intersectsEllipse(EllipseCollider e) {
        double cx = center.x();
        double cy = center.y();

        double ex = e.getCenter().x();
        double ey = e.getCenter().y();
        double rx = e.getRadiusX();
        double ry = e.getRadiusY();
        double angle = Math.toRadians(e.getRotation());

        // Translate circle center to ellipse local space
        double px = cx - ex;
        double py = cy - ey;

        // Rotate circle center by -angle
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double pxRot = cos * px + sin * py;
        double pyRot = -sin * px + cos * py;

        // Normalize to ellipse axes
        double dx = pxRot / rx;
        double dy = pyRot / ry;

        // Scale radius conservatively
        double scaledRadius = radius / Math.max(rx, ry);

        // Check intersection
        return dx * dx + dy * dy <= (1 + scaledRadius) * (1 + scaledRadius);
    }


    @Override
    public boolean intersectsTriangle(TriangleCollider t) {
        Point p = center; // circle center

        // Check if circle center is inside triangle (barycentric)
        if (pointInTriangle(p, t)) return true;

        // Check distance to each edge
        if (circleEdgeIntersection(p, radius, t.getA(), t.getB())) return true;
        if (circleEdgeIntersection(p, radius, t.getB(), t.getC())) return true;
        return circleEdgeIntersection(p, radius, t.getC(), t.getA());
    }

    // Check if a point is inside a triangle using barycentric coordinates
    private boolean pointInTriangle(Point p, TriangleCollider t) {
        Vector2 v0 = t.getC().subtract(t.getA());
        Vector2 v1 = t.getB().subtract(t.getA());
        Vector2 v2 = p.subtract(t.getA());

        double dot00 = v0.dot(v0);
        double dot01 = v0.dot(v1);
        double dot02 = v0.dot(v2);
        double dot11 = v1.dot(v1);
        double dot12 = v1.dot(v2);

        double invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return u >= 0 && v >= 0 && (u + v) <= 1;
    }

    // Check if circle intersects a line segment
    private boolean circleEdgeIntersection(Point c, double radius, Point p1, Point p2) {
        Vector2 ab = p2.subtract(p1);
        Vector2 ac = c.subtract(p1);

        double t = ac.dot(ab) / ab.dot(ab);
        t = Math.max(0, Math.min(1, t));

        Point closest = new Point(p1.x() + ab.x() * t, p1.y() + ab.y() * t);
        Vector2 diff = c.subtract(closest);

        return diff.magnitudeSquared() <= radius * radius;
    }


    @Override
    public Vector2 getMTV(Collider other) {
        // will do later
        return null;
    }
}
