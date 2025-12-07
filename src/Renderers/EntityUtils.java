package Renderers;

import Game.SoundHandler;
import Physics.Collision.Collider;
import Shapes.Shape;
import Shapes.Vector2;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {
    private Vector2 gravity = new Vector2(0, -0.01);
    private Vector2 playerVelocity = new Vector2(0, 0);
    private List<Shape> shapes = new ArrayList<>();

    private static final String[] bounceSounds = {"Sounds/bounce.wav"};

    private boolean allowSounds;

    public void addShape(Shape shape){
        shapes.add(shape);
    }

    public void removeShape(Shape shape){
        shapes.remove(shape);
    }

    public void clearShapes(){
        shapes.clear();
    }

    public Vector2 getGravity(){
        return gravity;
    }

    public Vector2 getPlayerVelocity(){
        return playerVelocity;
    }

    public List<Shape> getShapes(){
        return shapes;
    }

    public void allowBounceSounds(){
        allowSounds = true;
    }

    public void updateGravity(Vector2 gravity){
        this.gravity = gravity;
    }

    public void updatePlayerVelocity(Vector2 velocity){
        playerVelocity = velocity;
    }

    //checks collisions between the player and all shapes, returns boolean for whether bounced or not
    public void checkCollisions(Shape player){
        boolean bounced = false;
        for (Shape shape : shapes){
            if (player.getCollider().intersects(shape.getCollider())){
                collide(player, shape.getCollider(), shape.getRestitution());
                bounced = true;
            }
        }
    }

    //collision and bounce algorithm
    private void collide(Shape player, Collider other, double restitution){
        Collider collider = player.getCollider();
        Vector2 mtv = collider.getMTV(other);
        if (!mtv.isZero()) {
            if (allowSounds && !playerVelocity.isZero()){
                playBounceSound(restitution);
            }
            player.move(mtv);
            Vector2 normal = mtv.normalize();
            playerVelocity = playerVelocity.reflect(normal).scale(restitution);
        }
    }

    private void playBounceSound(double restitution) {
        if (restitution >= 0) {
            SoundHandler.play(bounceSounds[0]);
        } //TODO: ADD DIFFERENT SOUNDS DEPENDING ON RESTITUTION VALUE
    }


}
