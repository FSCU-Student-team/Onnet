package Physics.Entities;

import Shapes.Circle;
import Shapes.Point;
import Shapes.Vector2;
import com.jogamp.opengl.GL2;

public class Ball {
    private final Circle circle;
    private Vector2 pos;
    private Vector2 vel;
    private final double radius;
    private final double screenWidth, screenHeight;

    private final double gravity = 200; // px/s²
    private double bounce = 1;          // energy loss (0..1)

    // Define a small velocity threshold to determine if the ball is "at rest"
    private static final double MIN_VELOCITY = 1.0; // px/s (Adjust this value as needed)

    public Ball(Circle circle, double screenWidth, double screenHeight) {
        this.circle = circle;
        this.radius = circle.getWidth() / 2.0;
        this.pos = new Vector2(circle.getCenter().x(), circle.getCenter().y());
        this.vel = Vector2.ZERO;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void launch(double angleDeg, double speed) {
        double rad = Math.toRadians(angleDeg);
        vel = new Vector2(Math.cos(rad) * speed, Math.sin(rad) * speed);
    }

    public void setBounce(double bounce) {
        this.bounce = Math.max(0.0, Math.min(bounce, 1.0));
    }

    // dt = seconds per physics step
    // dt = seconds per physics step
    public void update(double dt) {
        // Apply gravity
        vel = vel.add(new Vector2(0, -gravity * dt));

        // Update position
        pos = pos.add(vel.scale(dt));

        // --- Floor collision ---
        if (pos.y() - radius < 0 && vel.y() < 0) {
            pos = pos.withY(radius);

            // Reflect velocity
            vel = vel.reflect(new Vector2(0, 1)).scale(bounce);

            // Only stop if velocity is extremely tiny
            if (Math.abs(vel.y()) < 0.01) {
                vel = vel.withY(0);
            }
        }

        // --- Ceiling ---
        if (pos.y() + radius > screenHeight && vel.y() > 0) {
            pos = pos.withY(screenHeight - radius);
            vel = vel.reflect(new Vector2(0, -1)).scale(bounce);
        }

        // --- Left wall ---
        if (pos.x() - radius < 0 && vel.x() < 0) {
            pos = pos.withX(radius);
            vel = vel.reflect(new Vector2(1, 0)).scale(bounce);
        }

        // --- Right wall ---
        if (pos.x() + radius > screenWidth && vel.x() > 0) {
            pos = pos.withX(screenWidth - radius);
            vel = vel.reflect(new Vector2(-1, 0)).scale(bounce);
        }

        // Update circle
        circle.setOrigin(new Point(pos.x(), pos.y()));
    }


    public void draw(GL2 gl) {
        circle.draw(gl);
    }

    public void applyForce(Vector2 force, double dt) {
        vel = vel.add(force.scale(dt));
    }

    public Vector2 getPosition() {
        return pos;
    }

    public Vector2 getVelocity() {
        return vel;
    }
}