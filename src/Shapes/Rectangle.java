package Shapes;

import Physics.Collision.Collider;
import Physics.Collision.RectangleCollider;
import com.jogamp.opengl.GL2;

public class Rectangle implements Shape {

    private double width;
    private double height;
    private final Color color;
    private final boolean fill;
    private double angle; // degrees
    private Point origin;    // starting from bottom left
    private double restitution;

    private RectangleCollider collider;


    private Rectangle(Builder b) {
        this.width = b.width;
        this.height = b.height;
        this.color = b.color;
        this.fill = b.fill;
        this.angle = b.angle;
        this.origin = b.origin;
        this.restitution = b.restitution;
        collider = new RectangleCollider(origin, width, height, angle);
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
    public void scale(double factor) {
        this.width *= factor;
        this.height *= factor;
        collider.setWidth(width);
        collider.setHeight(height);
    }

    @Override
    public void rotate(double angle) {
        this.angle += angle;
        collider.setRotation(this.angle);
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public double getRestitution() {
        return restitution;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public void move(Vector2 delta) {
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
        gl.glRotated(angle, 0, 0, 1);

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
    public Rectangle copy() {
        return new Builder()
                .width(width)
                .height(height)
                .color(color)
                .fill(fill)
                .rotation(angle)
                .restitution(restitution)
                .origin(new Point(origin.x(), origin.y()))
                .build();
    }

    public static class Builder {
        private double width = 0;
        private double height = 0;
        private Color color = Color.BLUE;
        private boolean fill = false;
        private double angle = 0;
        private Point origin = new Point(0, 0);
        private double restitution = 0;

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
            this.angle = rotation;
            return this;
        }

        public Builder origin(Point origin) {
            this.origin = origin;
            return this;
        }

        public Builder restitution(double restitution) {
            this.restitution = restitution;
            return this;
        }

        public Rectangle build() {
            if (restitution < 0) restitution = 0;
            if (angle < 0) angle = (angle % 360 + 360) % 360; //turn negative angle to positive
            if (angle > 360) angle = angle % 360;
            return new Rectangle(this);
        }
    }
}
