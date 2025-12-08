package Renderers;

import Game.GameLoop;
import Game.InputManager;
import Game.LoopState;
import com.jogamp.opengl.*;
import Shapes.*;
import Physics.ActionManager;
import Physics.Collision.Collider;

public class GameRenderer implements GLEventListener, GameLoop {

    private LoopState loopState;
    private ActionManager actionManager;
    private InputManager inputManager;
    private GL2 gl;

    // Game objects
    private Circle player;
    private Vector2 velocity = new Vector2(0,0);
    private Vector2 gravity = new Vector2(0, -0.01);

    public GameRenderer() {
        inputManager = new InputManager();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        loopState = new LoopState();
        gl = drawable.getGL().getGL2();
        actionManager = new ActionManager(inputManager);

        gl.glClearColor(0, 0, 0, 1);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);

        // Simple player
        player = new Circle.Builder()
                .center(new Point(400,300))
                .radius(20)
                .color(Color.WHITE)
                .filled(true)
                .build();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        handleLoop(loopState, gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        gl.glViewport(0,0,w,h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, w, 0, h, -1, 1);
    }

    @Override
    public void physicsUpdate() {
        actionManager.update();
        player.move(velocity);
        velocity = velocity.add(gravity);
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        gl.glPushMatrix();
        player.draw(gl);
        gl.glPopMatrix();
    }
}
