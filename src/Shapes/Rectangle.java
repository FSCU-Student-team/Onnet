package Shapes;

import Physics.Collision.Collider;
import Physics.Collision.RectangleCollider;
import com.jogamp.opengl.GL2;

public class Rectangle implements Shape {

    private double width;
    private double height;
    private final Color color;
    private final boolean fill;
    private double rotation; // degrees
    private Point origin;    // starting from bottom left
    private RectangleCollider collider;


    private Rectangle(Builder b) {
        this.width = b.width;
        this.height = b.height;
        this.color = b.color;
        this.fill = b.fill;
        this.rotation = b.rotation;
        this.origin = b.origin;
        collider = new RectangleCollider(origin, width, height, rotation);
    }

    @Override
    public void setOrigin(Point origin) {
        this.origin = origin;
        collider.setOrigin(origin);
    }

    @Override
    public Point getCenter() {
        return new Point(origin.x() + width / 2, origin.y() + height / 2);
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void Scale(double factor) {
        this.width *= factor;
        this.height *= factor;
        collider.setWidth(width);
        collider.setHeight(height);
    }

    @Override
    public void Rotate(double angle) {
        this.rotation += angle;
        collider.setRotation(rotation);
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    public double getRotation() {
        return rotation;
    }

    @Override
    public void Move(Vector2 delta) {
        this.origin = new Point(origin.x() + delta.x(), origin.y() + delta.y());
        collider.setOrigin(origin);
        collider.setOrigin(origin);
    }

    @Override
    public void draw(GL2 gl) {
        color.useColorGl(gl);

        gl.glPushMatrix();

        // Move to rectangle center
        double centerX = origin.x() + width / 2;
        double centerY = origin.y() + height / 2;
        gl.glTranslated(centerX, centerY, 0);

        // Rotate around center
        gl.glRotated(rotation, 0, 0, 1);

        // Move back by half width/height to draw from bottom-left
        gl.glTranslated(-width / 2, -height / 2, 0);

        // Draw rectangle
        if (fill) {
            gl.glBegin(GL2.GL_QUADS);
        } else {
            gl.glBegin(GL2.GL_LINE_LOOP);
        }

        gl.glVertex2d(0, 0);
        gl.glVertex2d(width, 0);
        gl.glVertex2d(width, height);
        gl.glVertex2d(0, height);

        gl.glEnd();
        gl.glPopMatrix();
    }


    //provides a deep copy of the rectangle
    @Override
    public Rectangle Copy() {
        return new Builder()
                .width(width)
                .height(height)
                .color(color)
                .fill(fill)
                .rotation(rotation)
                .origin(new Point(origin.x(), origin.y()))
                .build();
    }

    public static class Builder {
        private double width = 1;
        private double height = 1;
        private Color color = Color.BLACK;
        private boolean fill = false;
        private double rotation = 0;
        private Point origin = new Point(0, 0);

        public Builder width(double width) {
            this.width = width;
            return this;
        }

        public Builder height(double height) {
            this.height = height;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder fill(boolean fill) {
            this.fill = fill;
            return this;
        }

        public Builder rotation(double rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder origin(Point origin) {
            this.origin = origin;
            return this;
        }

        public Rectangle build() {
            return new Rectangle(this);
        }
    }
}
