package Physics.Collision;

import Shapes.Vector2;


public interface Collider {

    // Axis-aligned bounding box (for broad phase)
    AABB getAABB();

    // Simple boolean collision check
    default boolean intersects(Collider other){
        return switch (other) {
            case CircleCollider c -> intersectsCircle(c);
            case RectangleCollider r -> intersectsRectangle(r);
            case EllipseCollider e -> intersectsEllipse(e);
            case TriangleCollider t -> intersectsTriangle(t);
            default -> false;
        };
    }

    boolean intersectsCircle(CircleCollider c);
    boolean intersectsRectangle(RectangleCollider r);
    boolean intersectsEllipse(EllipseCollider e);
    boolean intersectsTriangle(TriangleCollider t);

    // Optional: penetration vector (for physics resolution)
    Vector2 getMTV(Collider other);  // MTV = Minimum Translation Vector
}
