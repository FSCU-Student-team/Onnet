package Shapes;

//import Others_Files.Color;
//import Others_Files.Point;
import com.jogamp.opengl.GL2;

public class CircleShape implements Shape {
    private double radius;
    private Point Center;
    private double Angle;
    private boolean filled;
    private final Color color;


    public CircleShape(Builder builder) {
        radius = builder.radius;
        Center = builder.Center;
        Angle = builder.Angle;
        filled = builder.filled;
        color = builder.color;
    }

    // set the center as point not as Coordinate
    @Override
    public void setCenter(Point center) {
        Center = center;
    }

    //  return the Center as point
    @Override
    public Point getCenter() {
        return Center;
    }
    // sets radius
    public void setRadius(double radius) {
        this.radius = radius;
    }
    //  returns the width as double of the radius
    @Override
    public double getWidth() {
        return radius * 2;
    }
    // sets if the circle filled or no
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    // returns ture as it's going to be filled circle
    public boolean isFilled() {
        return filled;
    }

    // return the height as double of radius
    @Override
    public double getHeight() {
        return radius * 2;
    }

    // scaling with increase or decrease the radius with Factor
    @Override
    public void Scale(double Factor) {
        radius *= Factor;
    }

    // rotate is by the angle as it's polar coordinates as radian
    @Override
    public void Rotate(double Angle) {
        this.Angle = Angle;
    }

    // returns angle in radian
    public double getAngle() {
        return Angle;
    }

    // moving is by have new point of moved x and moved y by new value as speed
    @Override
    public void Move(double x, double y) {
        Center = new Point(x + Center.x(), y + Center.y());
    }

    // Drawing circle as filled or not angle is radian
    @Override
    public void Draw(GL2 gl) {
        int Numseg = (int) Math.min(20, radius * 10);
        double angleStep = 2 * Math.PI / Numseg;
        double rotate_rad = Math.toRadians(angleStep);
        if (isFilled()) {
            gl.glBegin(GL2.GL_TRIANGLE_FAN);
            gl.glVertex2d(Center.x(), Center.y());
        } else {
            gl.glBegin(GL2.GL_LINE_LOOP);
        }
        for (int i = 0; i < Numseg; i++) {
            double theta = Math.toRadians(i * angleStep + rotate_rad);//as rotation
            gl.glVertex2d(Center.x() + radius * Math.cos(theta), Center.y() + radius * Math.sin(theta));
        }
        gl.glEnd();
    }

    @Override
    public Shape Copy() {
        return new Builder(radius, Center, Angle, filled, color).Build();
    }

    @Override
    public Shape Copy(Shape ref) {
        if (ref instanceof CircleShape copy) {
            return new Builder(copy.radius, copy.Center, copy.Angle, copy.filled, copy.color).Build();
        }
        return this;
    }

    private static class Builder {
        private final double radius;
        private final Point Center;
        private final double Angle;
        private final boolean filled;
        private final Color color;

        public Builder(double radius, Point Center, double Angle, boolean filled, Color color) {
            this.radius = radius;
            this.Center = Center;
            this.Angle = Angle;
            this.filled = filled;
            this.color = color;
        }

        public CircleShape Build() {
            return new CircleShape(this);
        }
    }
}

