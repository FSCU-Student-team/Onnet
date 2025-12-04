package Shapes;

import com.jogamp.opengl.GL2;

public class Rectangle implements Shape {

    private double width;
    private double height;
    private Color color;
    private boolean fill;
    private double rotation; // degrees
    private Point origin;    // bottom-left

    // Private constructor
    private Rectangle(Builder b) {
        this.width = b.width;
        this.height = b.height;
        this.color = b.color;
        this.fill = b.fill;
        this.rotation = b.rotation;
        this.origin = b.origin;
    }

    // ----------------- Shape methods -----------------
    @Override
    public void setOrigin(Point origin) {
        this.origin = origin;
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
    }

    @Override
    public void Rotate(double angle) {
        this.rotation += angle;
    }

    @Override
    public void Move(double x, double y) {
        this.origin = new Point(origin.x() + x, origin.y() + y);
    }

    @Override
    public void Draw(GL2 gl) {
        color.useColorGl(gl);

        gl.glPushMatrix();
        gl.glTranslated(origin.x(), origin.y(), 0);
        gl.glRotated(rotation, 0, 0, 1);

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

    @Override
    public Shape Copy() {
        return new Builder()
                .width(width)
                .height(height)
                .color(color)
                .fill(fill)
                .rotation(rotation)
                .origin(origin)
                .build();
    }

    @Override
    public Shape Copy(Shape ref) {
        if (ref instanceof Rectangle r) {
            this.width = r.width;
            this.height = r.height;
            this.color = r.color;
            this.fill = r.fill;
            this.rotation = r.rotation;
            this.origin = r.origin;
        }
        return this;
    }

    // ----------------- Builder -----------------
    public static class Builder {
        private double width = 1;
        private double height = 1;
        private Color color = Color.BLACK;
        private boolean fill = false;
        private double rotation = 0;
        private Point origin = new Point(0, 0);

        public Builder width(double width) { this.width = width; return this; }
        public Builder height(double height) { this.height = height; return this; }
        public Builder color(Color color) { this.color = color; return this; }
        public Builder fill(boolean fill) { this.fill = fill; return this; }
        public Builder rotation(double rotation) { this.rotation = rotation; return this; }
        public Builder origin(Point origin) { this.origin = origin; return this; }

        public Rectangle build() {
            return new Rectangle(this);
        }
    }
}
