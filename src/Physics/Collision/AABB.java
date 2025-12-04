package Physics.Collision;

// Axis-aligned bounding box, contains bounds of (x,y) positions
public record AABB(double minX, double minY, double maxX, double maxY) {

    //check if this AABB intersects another
    public boolean intersects(AABB other) {
        return !(other.minX > maxX || other.maxX < minX ||
                other.minY > maxY || other.maxY < minY);
    }

    //check if a point is inside the AABB
    public boolean contains(double x, double y) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }
}

