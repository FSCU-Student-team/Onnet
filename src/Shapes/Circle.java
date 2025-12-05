package Shapes;

import Physics.Collision.CircleCollider;
import Physics.Collision.Collider;
import com.jogamp.opengl.GL2;

public class Circle implements Shape {
    private double radius;
    private Point Center;
    private double Angle; //in degrees
    private boolean filled;
    private final Color color;
    private double Cx, Cy;

    CircleCollider collider;


    public Circle(Builder builder) {
        radius = builder.radius;
        Center = builder.Center;
        Angle = builder.Angle;
        filled = builder.filled;
        color = builder.color;
        collider  = new CircleCollider(Center, radius);
    }

    // set the center as point not as Coordinate
    @Override
    public void setOrigin(Point center) {
        Center = center;
        Cx = center.x();
        Cy = center.y();
        collider.setCenter(center);
    }

    //  return the Center as point
    @Override
    public Point getCenter() {
        return Center;
    }

    // sets radius
    public void setRadius(double radius) {
        this.radius = radius;
        collider.setRadius(radius);
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
        collider.setRadius(radius);
    }

    // rotate is by the angle as it's polar coordinates as radian
    @Override
    public void Rotate(double Angle) {
        this.Angle = Angle;
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    // returns angle in radian
    public double getAngle() {
        return Angle;
    }

    // moving is by have new point of moved x and moved y by new value as speed
    @Override
    public void Move(Vector2 delta) {
        Center = new Point(delta.x() + Center.x(), delta.y() + Center.y());
        collider.setCenter(Center);
    }

    @Override
    public void draw(GL2 gl) {
        int iterations = Math.max(20, (int) (radius * 2));
        double angleStepDeg = 360.0 / iterations;

        double rotationRad = Math.toRadians(Angle);

        color.useColorGl(gl);

        gl.glBegin(filled ? GL2.GL_POLYGON : GL2.GL_LINE_LOOP);

        for (int i = 0; i < iterations; i++) {
            double theta = Math.toRadians(i * angleStepDeg) + rotationRad;

            double x = Center.x() + radius * Math.cos(theta);
            double y = Center.y() + radius * Math.sin(theta);

            gl.glVertex2d(x, y);
        }

        gl.glEnd();
    }


    @Override
    public Circle Copy() {
        return new Builder()
                .Radius(radius)
                .Filled(filled)
                .color(color)
                .Center(Center)
                .Angle(Angle)
                .Build();
    }

    public static class Builder {
        private double radius;
        private Point Center;
        private double Angle;
        private boolean filled;
        private Color color;
        private double Cx, Cy;

        public Builder Radius(double radius) {
            this.radius = radius;
            return this;
        }

        public Builder Center(Point center) {
            this.Center = center;
            this.Cx = center.x();
            this.Cy = center.y();
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder Filled(boolean fill) {
            this.filled = fill;
            return this;
        }

        public Builder Angle(double rotation) {
            this.Angle = rotation;
            return this;
        }

        public Circle Build() {
            return new Circle(this);
        }
    }
}

