package Renderers;

import Game.GameLoop;
import Game.Input;
import Game.InputManager;
import Game.LoopState;
import Physics.ActionManager;
import Shapes.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuBackground implements GLEventListener, GameLoop {
    ActionManager actionManager;
    public InputManager inputManager;
    LoopState loopState;
    GL2 gl;

    List<Shape> shapes = new ArrayList<>();

    Circle circle;

    EntityUtils entityUtils = new EntityUtils();

    Vector2 velocity = new Vector2(0, 0);
    Vector2 gravity = new Vector2(0, -0.01);

    public MenuBackground() {
        inputManager = new InputManager();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        loopState = new LoopState();
        actionManager = new ActionManager(inputManager);
        gl = glAutoDrawable.getGL().getGL2();

        gl.glClearColor(0, 0, 0, 1);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);

        circle = new Circle.Builder().color(Color.WHITE).angle(0).filled(true).center(new Point(400, 600)).radius(10).build();
        Rectangle rectangle = new Rectangle.Builder().color(Color.BLUE).fill(true).origin(new Point(0, 50)).restitution(0.9938).width(1000).height(10).build();

        entityUtils.updatePlayerVelocity(velocity);
        entityUtils.updateGravity(gravity);

        //DEV NOTE: don't add the circle (player) to this list, or else it will check itself for intersection which will crash the app
        entityUtils.addShape(rectangle);

        shapes = entityUtils.getShapes();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        handleLoop(loopState, gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    @Override
    public void physicsUpdate() {
        inputUpdate();
        // Update EntityUtils with new velocity so bounce uses correct vector
        entityUtils.updatePlayerVelocity(velocity);

        entityUtils.checkCollisions(circle);

        // Read back the corrected velocity after collision
        velocity = entityUtils.getPlayerVelocity();

        circle.move(velocity);
        velocity = velocity.add(gravity);
    }


    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();
        circle.draw(gl);

        for (Shape shape : shapes) {
            shape.draw(gl);
        }


        gl.glPopMatrix();
    }

    @Override
    public void inputUpdate() {
        actionManager.update();
    }
}
