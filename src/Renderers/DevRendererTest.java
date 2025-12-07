package Renderers;

import Game.*;
import Physics.ActionManager;
import Shapes.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.util.ArrayList;
import java.util.List;

public class DevRendererTest implements GLEventListener, GameLoop {
    ActionManager actionManager;
    public InputManager inputManager;
    LoopState loopState;
    GL2 gl;

    List<Shape> shapes = new ArrayList<>();

    Circle circle;

    EntityUtils entityUtils = new EntityUtils();

    Vector2 gravity = new Vector2(0, -0.01);
    Vector2 velocity = new Vector2(0, 0);

    public DevRendererTest() {
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

        actionManager.bind(Input.A, () -> velocity = velocity.add(new Vector2(-0.05, 0)));
        actionManager.bind(Input.D, () -> velocity = velocity.add(new Vector2(0.05, 0)));
        actionManager.bind(Input.W, () -> velocity = velocity.add(new Vector2(0, 0.05)));
        actionManager.bind(Input.S, () -> velocity = velocity.add(new Vector2(0, -0.05)));


        circle = new Circle.Builder().color(Color.WHITE).angle(0).filled(true).center(new Point(400, 300)).radius(20).build();
        Rectangle rectangle = new Rectangle.Builder().color(Color.BLUE).rotation(10).fill(true).origin(new Point(0, 50)).restitution(0.5).width(1000).height(10).build();
        Rectangle rectangleTop = new Rectangle.Builder().color(Color.BLUE).rotation(0).fill(true).origin(new Point(0, 500)).restitution(0.5).width(1000).height(10).build();
        Rectangle rectangleLeft = new Rectangle.Builder().color(Color.BLUE).rotation(0).fill(true).origin(new Point(700, 0)).restitution(0.5).width(10).height(1000).build();
        Rectangle rectangleRight = new Rectangle.Builder().color(Color.BLUE).rotation(0).fill(true).origin(new Point(50, 0)).restitution(0.5).width(10).height(1000).build();

        Triangle triangle = new Triangle.Builder().color(Color.GREEN).fill(true).addPoint(new Point(100, 100)).addPoint(new Point(200, 200)).addPoint(new Point(300, 100)).restitution(0.9).build();

        Circle notPlayerCircle = new Circle.Builder().color(Color.GRAY).filled(true).restitution(0.1).center(new Point(500, 200)).radius(50).build();

        entityUtils.updatePlayerVelocity(velocity);
        entityUtils.updateGravity(gravity);

        //DEV NOTE: don't add the circle (player) to this list, or else it will check itself for intersection which will crash the app
        entityUtils.addShape(rectangle);
        entityUtils.addShape(rectangleTop);
        entityUtils.addShape(rectangleLeft);
        entityUtils.addShape(rectangleRight);
        entityUtils.addShape(triangle);
        entityUtils.addShape(notPlayerCircle);

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
        actionManager.update();

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

        for (Shape shape : shapes) {
            shape.draw(gl);
        }

        circle.draw(gl);

        gl.glPopMatrix();

        entityUtils.allowBounceSounds();
    }
}
