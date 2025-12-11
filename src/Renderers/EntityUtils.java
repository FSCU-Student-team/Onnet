package Renderers;

import Game.SoundHandler;
import Physics.Collision.Collider;
import Shapes.*;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {
    private Vector2 gravity = new Vector2(0, -0.01);
    private Vector2 playerVelocity = new Vector2(0, 0);
    private List<Shape> shapes = new ArrayList<>();

    private static final String[] bounceSounds = {"Sounds/mixkit-light-impact-on-the-ground-2070.wav",
    "Sounds/bounce.wav"};

    private boolean allowSounds;

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public void clearShapes() {
        shapes.clear();
    }

    public Vector2 getGravity() {
        return gravity;
    }

    public Vector2 getPlayerVelocity() {
        return playerVelocity;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void allowBounceSounds() {
        allowSounds = true;
    }

    public void updateGravity(Vector2 gravity) {
        this.gravity = gravity;
    }

    public void updatePlayerVelocity(Vector2 velocity) {
        playerVelocity = velocity;
    }

    //checks collisions between the player and all shapes, returns boolean for whether bounced or not
    public void checkCollisions(Shape player) {
        boolean bounced = false;
        for (Shape shape : shapes) {
            if (player.getCollider().intersects(shape.getCollider())) {
                collide(player, shape.getCollider(), shape.getRestitution());
                bounced = true;
            }
        }
    }

    //collision and bounce algorithm
    private void collide(Shape player, Collider other, double restitution) {
        Collider collider = player.getCollider();
        Vector2 mtv = collider.getMTV(other);
        if (!mtv.isZero()) {
            if (allowSounds && !playerVelocity.isZero()) {
                playBounceSound(restitution);
            }
            player.move(mtv);
            Vector2 normal = mtv.normalize();
            playerVelocity = playerVelocity.reflect(normal).scale(restitution);
        }
    }

    private void playBounceSound(double restitution) {
        if (restitution >= 0 && restitution < 0.6) {
            SoundHandler.play(bounceSounds[0], 1.2);
        } else if (restitution >= 0.6){
            SoundHandler.play(bounceSounds[1], 0.8);
        }
    }

    public boolean checkPlayerDying(Circle playerCircle) {
        for (Shape shape : shapes) {

            if (shape == playerCircle) {
                continue;
            }

            if (shape.getColor().toString().equals(Color.RED.toString())) {


                double pX = playerCircle.getCenter().x();
                double pY = playerCircle.getCenter().y();
                double pRadius = playerCircle.getWidth() / 2.0;

                double sX = shape.getCenter().x();
                double sY = shape.getCenter().y();
                double sHalfWidth = shape.getWidth() / 2.0;
                double sHalfHeight = shape.getHeight() / 2.0;

                double left = sX - sHalfWidth;
                double right = sX + sHalfWidth;
                double bottom = sY - sHalfHeight;
                double top = sY + sHalfHeight;


                double closestX = Math.max(left, Math.min(pX, right));
                double closestY = Math.max(bottom, Math.min(pY, top));

                double dx = pX - closestX;
                double dy = pY - closestY;

                if ((Math.sqrt(dx * dx + dy * dy)) < (pRadius)) {
                    SoundHandler.play("Sounds/Voicy_Geometry Dash Death Sound.wav");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkPlayerWinning(Circle playerCircle, Rectangle goalRectangle) {
        double goalX = goalRectangle.getCenter().x();
        double goalY = goalRectangle.getCenter().y();
        double goalHalfWidth = goalRectangle.getWidth() / 2.0;
        double goalHalfHeight = goalRectangle.getHeight() / 2.0;

        double left = goalX - goalHalfWidth;
        double right = goalX + goalHalfWidth;
        double bottom = goalY - goalHalfHeight;
        double top = goalY + goalHalfHeight;

        double pX = playerCircle.getCenter().x();
        double pY = playerCircle.getCenter().y();
        double pRadius = playerCircle.getWidth() / 2.0;

        double closestX = Math.max(left, Math.min(pX, right));
        double closestY = Math.max(bottom, Math.min(pY, top));

        double dx = pX - closestX;
        double dy = pY - closestY;

        if ((dx * dx + dy * dy) < (pRadius * pRadius)) {
            SoundHandler.play("Sounds/mixkit-conference-audience-clapping-strongly-476.wav");
            return true;
        }
        return false;
    }
}
