package Shapes;

import com.jogamp.opengl.GL2;


public class Triangle implements Shape {
    private Point P1;
    private Point P2;
    private Point P3;
    private Color color;
    private double angle;
    private double width;
    private double height;
    private boolean filled;

    public Triangle(Builder b) {
        this.P1 = b.Pone;
        this.P2 = b.Ptwo;
        this.P3 = b.Pthree;
        this.color = b.color;
        this.angle = b.angle;
        this.width = b.width;
        this.height = b.height;
        this.filled=b.filled;
    }

    @Override
    public void setOrigin(Point origin) {

    }

    @Override
    public Point getCenter() {
        return new Point(getWidth() / 2.0, getHeight() / 2.0);
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
    public void Scale(double Factor) {
        double centerX = (P1.x() + P2.x() + P3.x()) / 3;
        double centerY = (P1.y() + P2.y() + P3.y()) / 3;
        Point centroid = new Point(centerX, centerY);

        Vector2 v1 = P1.subtract(centroid).scale(Factor);
        Vector2 v2 = P2.subtract(centroid).scale(Factor);
        Vector2 v3 = P3.subtract(centroid).scale(Factor);

        P1 = centroid.add(v1);
        P2 = centroid.add(v2);
        P3 = centroid.add(v3);
    }

    @Override
    public void Rotate(double Angle) {
        double rad = Math.toRadians(Angle);

        double centerX = (P1.x() + P2.x() + P3.x()) / 3;
        double centerY = (P1.y() + P2.y() + P3.y()) / 3;
        Point centroid = new Point(centerX, centerY);

        P1 = rotatePoint(P1, centroid, rad);
        P2 = rotatePoint(P2, centroid, rad);
        P3 = rotatePoint(P3, centroid, rad);
    }

    // Helper method to rotate a point around a center
    private Point rotatePoint(Point p, Point center, double rad) {
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        double dx = p.x() - center.x();
        double dy = p.y() - center.y();

        double xNew = cos * dx - sin * dy + center.x();
        double yNew = sin * dx + cos * dy + center.y();

        return new Point(xNew, yNew);
    }

    @Override
    public void Move(double x, double y) {
        Vector2 v = new Vector2(x, y);
        P1 = P1.add(v);
        P2 = P2.add(v);
        P3 = P3.add(v);

    }

    @Override
    public void Draw(GL2 gl) {
        gl.glColor4d(color.r(), color.g(), color.b(), color.a());
        if (filled) {
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex2d(P1.x(), P1.y());
            gl.glVertex2d(P2.x(), P2.y());
            gl.glVertex2d(P3.x(), P3.y());
            gl.glEnd();
        } else gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex2d(P1.x(), P1.y());
        gl.glVertex2d(P2.x(), P2.y());
        gl.glVertex2d(P3.x(), P3.y());
        gl.glEnd();


    }

    @Override
    public Shape Copy() {
        return new Builder()
                .Pone(P1)
                .Ptwo(P2)
                .Pthree(P3)
                .angle(angle)
                .width(width)
                .height(height)
                .color(color)

                .isFilled(filled)
                .build();
    }


    @Override
    public Shape Copy(Shape ref) {
        if (ref instanceof Triangle t) {
            return new Builder()
                    .Pone(t.P1)
                    .Ptwo(t.P2)
                    .Pthree(t.P3)
                    .color(t.color)
                    .angle(t.angle)
                    .width(t.width)
                    .height(t.height)
                    .isFilled(t.filled)
                    .build();
        }
        return null;
    }

    public static class Builder {

        private Point Pone = new Point(0, 0);
        private Point Ptwo = new Point(0, 0);
        private Point Pthree = new Point(0, 0);
        private Color color = Color.BLACK;
        private double angle = 0;
        private double width = 0;
        private double height = 0;
        private boolean filled = false;


        public static Builder builder() {
            return new Builder();
        }

        public Builder isFilled(boolean filled) {
            this.filled = filled;
            return this;
        }



        public Builder Pone(Point Pone) {
            this.Pone = Pone;
            return this;
        }

        public Builder Ptwo(Point Ptwo) {
            this.Ptwo = Ptwo;
            return this;
        }

        public Builder Pthree(Point Pthree) {
            this.Pthree = Pthree;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder width(double width) {
            this.width = width;
            return this;
        }

        public Builder height(double height) {
            this.height = height;
            return this;
        }

        public Builder angle(double angle) {
            this.angle = angle;
            return this;
        }

        public Triangle build() {
            return new Triangle(this);
        }
    }
}
