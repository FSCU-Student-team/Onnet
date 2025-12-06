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

public class DevRendererTest implements GLEventListener, GameLoop {
    ActionManager actionManager;
    public InputManager inputManager;
    LoopState loopState;
    GL2 gl;
    Circle circle;
    Rectangle rectangle;
    Rectangle rectangleTop;
    Rectangle rectangleLeft;
    Rectangle rectangleRight;

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
        rectangle = new Rectangle.Builder().color(Color.BLUE).rotation(10).fill(true).origin(new Point(0, 50)).restitution(0.5).width(1000).height(10).build();
        rectangleTop = new Rectangle.Builder().color(Color.BLUE).rotation(0).fill(true).origin(new Point(0, 500)).restitution(0.5).width(1000).height(10).build();
        rectangleLeft = new Rectangle.Builder().color(Color.BLUE).rotation(0).fill(true).origin(new Point(700, 0)).restitution(0.5).width(10).height(1000).build();
        rectangleRight = new Rectangle.Builder().color(Color.BLUE).rotation(0).fill(true).origin(new Point(50, 0)).restitution(0.5).width(10).height(1000).build();

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


        // Check collision with bottom rectangle
        if (circle.getCollider().intersects(rectangle.getCollider())) {
            Vector2 mtv = circle.getCollider().getMTV(rectangle.getCollider());
            if (!mtv.isZero()) {
                circle.move(mtv); // push out of collision
                Vector2 normal = mtv.normalize(); // normal points from rectangle to circle
                velocity = velocity.reflect(normal).scale(rectangle.getRestitution());
            }
        }

        // Check collision with top rectangle
        if (circle.getCollider().intersects(rectangleTop.getCollider())) {
            // Normal pointing down
            Vector2 normal = new Vector2(0, -1);
            circle.move(circle.getCollider().getMTV(rectangleTop.getCollider()));
            velocity = velocity.reflect(normal).scale(rectangleTop.getRestitution());
        }

        if (circle.getCollider().intersects(rectangleLeft.getCollider())) {
            Vector2 normal = new Vector2(-1, 0);
            circle.move(circle.getCollider().getMTV(rectangleLeft.getCollider()));
            velocity = velocity.reflect(normal).scale(rectangleLeft.getRestitution());
        }

        if (circle.getCollider().intersects(rectangleRight.getCollider())) {
            Vector2 normal = new Vector2(1, 0);
            circle.move(circle.getCollider().getMTV(rectangleRight.getCollider()));
            velocity = velocity.reflect(normal).scale(rectangleRight.getRestitution());
        }

        circle.move(velocity);

        velocity = velocity.add(gravity);
    }


    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();
        circle.draw(gl);
        rectangle.draw(gl);
        rectangleTop.draw(gl);
        rectangleLeft.draw(gl);
        rectangleRight.draw(gl);

        gl.glPopMatrix();
    }
}
