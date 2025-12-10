package Shapes;

import Physics.Collision.CircleCollider;
import Physics.Collision.Collider;
import com.jogamp.opengl.GL2;

public class Circle implements Shape {
    private double radius;
    private Point center;
    private double angle; //in degrees
    private boolean filled;
    private final Color color;
    private final double restitution; // restitution = (1 - energyLossRatioPerImpact)

    CircleCollider collider;


    public Color getColor() {
        return color;
    }

    public Circle(Builder builder) {
        radius = builder.radius;
        center = builder.center;
        angle = builder.angle;
        filled = builder.filled;
        color = builder.color;
        restitution = builder.restitution;
        collider  = new CircleCollider(center, radius);
    }

    // set the center as point not as Coordinate
    @Override
    public void setOrigin(Point center) {
        this.center = center;
        collider.setCenter(center);
    }

    //  return the Center as point
    @Override
    public Point getCenter() {
        return center;
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
    public void scale(double Factor) {
        radius *= Factor;
        collider.setRadius(radius);
    }

    // rotate is by the angle as it's polar coordinates as radian
    @Override
    public void rotate(double Angle) {
        this.angle = Angle;
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public double getRestitution() {
        return restitution;
    }

    // returns angle in radian
    public double getAngle() {
        return angle;
    }

    // moving is by have new point of moved x and moved y by new value as speed
    @Override
    public void move(Vector2 delta) {
        center = new Point(delta.x() + center.x(), delta.y() + center.y());
        collider.setCenter(center);
    }

    @Override
    public void draw(GL2 gl) {
        int iterations = Math.max(20, (int) (radius * 2));
        double angleStepDeg = 360.0 / iterations;

        double rotationRad = Math.toRadians(angle);

        color.useColorGl(gl);

        gl.glBegin(filled ? GL2.GL_POLYGON : GL2.GL_LINE_LOOP);

        for (int i = 0; i < iterations; i++) {
            double theta = Math.toRadians(i * angleStepDeg) + rotationRad;

            double x = center.x() + radius * Math.cos(theta);
            double y = center.y() + radius * Math.sin(theta);

            gl.glVertex2d(x, y);
        }

        gl.glEnd();
    }


    @Override
    public Circle copy() {
        return new Builder()
                .radius(radius)
                .filled(filled)
                .color(color) //color is immutable
                .center(new Point(center.x(), center.y()))
                .angle(angle)
                .restitution(restitution)
                .build();
    }

    public static class Builder {
        private double radius;
        private Point center = new Point(0, 0);
        private double angle;
        private boolean filled;
        private Color color = Color.WHITE;
        private double restitution = 0;

        public Builder radius(double radius) {
            this.radius = radius;
            return this;
        }

        public Builder center(Point center) {
            this.center = center;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder filled(boolean fill) {
            this.filled = fill;
            return this;
        }

        public Builder angle(double rotation) {
            this.angle = rotation;
            return this;
        }

        public Builder restitution(double restitution) {
            this.restitution = restitution;
            return this;
        }

        public Circle build() {
            if (restitution < 0) restitution = 0;
            if (radius < 0) radius = 0;
            if (angle < 0) angle = (angle % 360 + 360) % 360; //turn negative angle to positive
            if (angle > 360) angle = angle % 360;
            return new Circle(this);
        }
    }
}

