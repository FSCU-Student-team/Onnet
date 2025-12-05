package Renderers;

import Game.GameLoop;
import Game.InputManager;
import Game.LoopState;
import Physics.ActionManager;
import Physics.Entities.Ball;
import Shapes.Circle;
import Shapes.Color;
import Shapes.Point;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class DevRendererTest implements GLEventListener, GameLoop {
    ActionManager actionManager;
    public InputManager inputManager;
    LoopState loopState;
    GL2 gl;
    Ball ball;

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

        Circle circle = new Circle.Builder().color(Color.WHITE).Angle(0).Filled(true).Center(new Point(400,300)).Radius(20).Build();

        ball = new Ball(circle, 800, 600);
        ball.launch(70, 500);
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
        ball.update(GameLoop.PHYSICS_STEP);
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();
        ball.draw(gl);

        gl.glPopMatrix();
    }
}
