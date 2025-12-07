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
        updateAABB();
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
        updateAABB();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        updateAABB();
    }

    private void updateAABB() {
        aabb = new AABB(
                center.x() - radius,
                center.y() - radius,
                center.x() + radius,
                center.y() + radius
        );
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
        return switch (other) {
            case RectangleCollider r -> getRectangleMTV(r);
            case TriangleCollider t -> getTriangleMTV(t);
            case CircleCollider c -> getCircleMTV(c);
            default -> Vector2.ZERO;
        };

        // TODO: and circle-ellipse and circle-polynomial.
    }



    //helper MTV methods below:

    private Vector2 getTriangleMTV(TriangleCollider t) {
        Point A = t.getA();
        Point B = t.getB();
        Point C = t.getC();

        // 1 – If circle center is inside triangle -> push out along smallest edge-normal overlap
        if (pointInTriangle(center, t)) {
            MTVResult r1 = mtvCircleAgainstEdge(center, radius, A, B);
            MTVResult r2 = mtvCircleAgainstEdge(center, radius, B, C);
            MTVResult r3 = mtvCircleAgainstEdge(center, radius, C, A);

            MTVResult best = smallestPositive(r1, r2, r3);
            if (best != null) return best.vector();
        }

        // 2 – Vertex penetration
        MTVResult vA = mtvCircleAgainstPoint(center, radius, A);
        MTVResult vB = mtvCircleAgainstPoint(center, radius, B);
        MTVResult vC = mtvCircleAgainstPoint(center, radius, C);

        MTVResult bestVertex = smallestPositive(vA, vB, vC);
        if (bestVertex != null) return bestVertex.vector();

        // 3 – Edge penetration
        MTVResult e1 = mtvCircleAgainstEdge(center, radius, A, B);
        MTVResult e2 = mtvCircleAgainstEdge(center, radius, B, C);
        MTVResult e3 = mtvCircleAgainstEdge(center, radius, C, A);

        MTVResult bestEdge = smallestPositive(e1, e2, e3);
        if (bestEdge != null) return bestEdge.vector();

        return Vector2.ZERO;
    }

    private MTVResult mtvCircleAgainstPoint(Point center, double radius, Point p) {
        Vector2 diff = center.subtract(p);
        double distSq = diff.magnitudeSquared();

        if (distSq >= radius * radius) return null;

        double dist = Math.sqrt(distSq);
        if (dist == 0) {
            // center exactly on vertex → choose arbitrary outward direction (x positive)
            return new MTVResult(new Vector2(radius, 0), radius);
        }

        double overlap = radius - dist;
        Vector2 normal = diff.normalize();
        return new MTVResult(normal.scale(overlap), overlap);
    }

    private MTVResult mtvCircleAgainstEdge(Point center, double radius, Point p1, Point p2) {
        Vector2 ab = p2.subtract(p1);
        Vector2 ac = center.subtract(p1);

        double denom = ab.dot(ab);
        if (denom == 0) {
            // degenerate edge -> treat as point
            return mtvCircleAgainstPoint(center, radius, p1);
        }

        double proj = ac.dot(ab) / denom;
        proj = Math.max(0, Math.min(1, proj));

        Point closest = new Point(
                p1.x() + ab.x() * proj,
                p1.y() + ab.y() * proj
        );

        Vector2 diff = center.subtract(closest);
        double distSq = diff.magnitudeSquared();
        if (distSq >= radius * radius) return null;

        double dist = Math.sqrt(distSq);
        if (dist == 0) {
            // center exactly on edge -> push perpendicular to edge
            Vector2 perp = ab.perpendicular().normalize();
            return new MTVResult(perp.scale(radius), radius);
        }

        double overlap = radius - dist;
        Vector2 normal = diff.normalize();
        return new MTVResult(normal.scale(overlap), overlap);
    }

    private MTVResult smallestPositive(MTVResult... results) {
        MTVResult best = null;
        for (MTVResult r : results) {
            if (r != null && r.magnitude() > 0) {
                if (best == null || r.magnitude() < best.magnitude()) best = r;
            }
        }
        return best;
    }


    public Vector2 getRectangleMTV(RectangleCollider r) {
        // Rectangle dimensions
        double hw = r.getWidth() / 2;
        double hh = r.getHeight() / 2;

        // Rectangle center
        Point centerR = new Point(r.getOrigin().x() + hw, r.getOrigin().y() + hh);

        // Circle center in rectangle local space
        double cx = center.x() - centerR.x();
        double cy = center.y() - centerR.y();

        // Rotate circle by -rectangle rotation
        double angle = Math.toRadians(r.getRotation());
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double localX = cos * cx + sin * cy;
        double localY = -sin * cx + cos * cy;

        // Closest point on rectangle
        double closestX = Math.max(-hw, Math.min(localX, hw));
        double closestY = Math.max(-hh, Math.min(localY, hh));

        double dx = localX - closestX;
        double dy = localY - closestY;
        double distSq = dx * dx + dy * dy;

        if (distSq == 0) {
            // Circle center is inside rectangle: push out along smallest axis
            double overlapX = radius + hw - Math.abs(localX);
            double overlapY = radius + hh - Math.abs(localY);
            if (overlapX < overlapY) {
                dx = (localX < 0 ? -1 : 1) * overlapX;
                dy = 0;
            } else {
                dx = 0;
                dy = (localY < 0 ? -1 : 1) * overlapY;
            }
        } else if (distSq < radius * radius) {
            double dist = Math.sqrt(distSq);
            double overlap = radius - dist;
            dx = dx / dist * overlap;
            dy = dy / dist * overlap;
        } else {
            // no collision
            return Vector2.ZERO;
        }

        // Rotate back to world space
        double mtvX = cos * dx - sin * dy;
        double mtvY = sin * dx + cos * dy;

        return new Vector2(mtvX, mtvY);
    }

    private Vector2 getCircleMTV(CircleCollider c) {
        Vector2 diff = center.subtract(c.center);
        double distSq = diff.magnitudeSquared();
        double r = radius + c.radius;

        // No collision
        if (distSq >= r * r) return Vector2.ZERO;

        // Overlapping or touching
        double dist = Math.sqrt(distSq);

        // Special case: centers exactly overlap
        if (dist == 0) {
            return new Vector2(r, 0);
        }

        double overlap = r - dist;

        return diff.scale(1.0 / dist).scale(overlap);
    }

}
