package Shapes;


import Game.Input;
import Game.InputManager;
import Physics.Collision.CircleCollider;
import com.jogamp.opengl.GL2;

import java.awt.*;

public class Ball_Speed_gravity {
    private Circle circle;
    private Point CPoint;
    private CircleCollider collider;
    private Vector2 velocity;

    private boolean isLaunched;
    private double aimAngle = 0;
    private double shotPower = 0;
    private double maxPower = 100;
    private double minPower = 0;
    private final double rotationSpeed = 60;
    private final double powerSpeed = 60;

    private double screenWidth, screenHeight;

    private static final double GRAVITY = -600;
    private static final double SPEED = 120;
    private static final double bounce_factor = -0.8;

    public Ball_Speed_gravity(Point Start, double radius, double Angle, Color color, boolean Filled) {
        circle = new Circle.Builder().Center(Start).Radius(radius).Filled(Filled).color(color).Angle(Angle).Build();
        collider = new CircleCollider(Start, radius);
        velocity = new Vector2(0, 0);
        CPoint = new Point(circle.getCenter().x(), circle.getCenter().y());
        var manager=new InputManager();
        screenWidth=manager.orthoRight-manager.orthoLeft;
        screenHeight=manager.orthoTop-manager.orthoBottom;
    }

    public void update(double dt, int inputState) {
        if (!isLaunched) {
            if (Input.isSet(inputState, Input.LEFT)) {
                aimAngle += rotationSpeed * dt;
                aimAngle %= 360;
            }
            if (Input.isSet(inputState, Input.RIGHT)) {
                aimAngle -= rotationSpeed * dt;
                aimAngle %= 360;
            }
            if (Input.isSet(inputState, Input.UP)) {
                shotPower += powerSpeed * dt;
                if (shotPower > maxPower) shotPower = maxPower;
            }
            if (Input.isSet(inputState, Input.DOWN)) {
                shotPower -= powerSpeed * dt;
                if (shotPower < minPower) shotPower = minPower;
            }
            if (Input.isSet(inputState, Input.Z)) {
                LaunchBall();
            }
        } else {
            double Newy = velocity.y() + (GRAVITY * dt);
            velocity = new Vector2(velocity.x(), Newy);
            circle.Move(velocity);
            collider.setCenter(circle.getCenter());
            CheckWallCollision(screenWidth, screenHeight);
        }

    }
//  here we launch the ball to it's destiny
    private void LaunchBall() {
        double rad = Math.toRadians(aimAngle);
        double Vx = Math.cos(rad) * shotPower;
        double Vy = Math.sin(rad) * shotPower;
        this.velocity = new Vector2(Vx, Vy);
        isLaunched = true;
    }
//  here should add collision, but I just added the lost point
    private void CheckWallCollision(double screenWidth, double screenHeight) {
       //but collision of the other 3 wall here as bounce
        if (screenHeight-circle.getCenter().y()>screenHeight){
            Reset();
        }
    }
//  Reset to the same first point as chance to retry
    private void Reset() {
        isLaunched = false;
        velocity = new Vector2(0, 0);
        circle.setOrigin(CPoint);
        collider.setCenter(circle.getCenter());
        aimAngle=0;
        shotPower=0;
    }
//  draw Circle and line together
    public void Draw(GL2 gl) {
        circle.Draw(gl);
        if (!isLaunched) {
            DrawAimLine(gl,Color.RED);
        }
    }
// drawing line as Aim that gets bigger as more shot power
    private void DrawAimLine(GL2 gl,Color color) {
        double rad=Math.toRadians(aimAngle);

        double endX = circle.getCenter().x()+Math.cos(rad)*0.1;
        double endY = circle.getCenter().y()+Math.sin(rad)*0.1;

        color.useColorGl(gl);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(circle.getCenter().x(), circle.getCenter().y());
        gl.glVertex2d(endX,endY);
        gl.glEnd();
        color.clearColorGl(gl);//no color as no Action on ball
    }
}
