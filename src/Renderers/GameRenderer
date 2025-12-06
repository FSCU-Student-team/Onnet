package Renderers;

import Game.GameLoop;
import Game.LoopState;
import com.jogamp.opengl.*;

public class GameRenderer implements GLEventListener, GameLoop {

    private LoopState state = new LoopState();

    @Override
    public void init(GLAutoDrawable drawable) {
        state.lastTime = System.nanoTime();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        handleLoop(state, gl); // physics + render
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {}

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void physicsUpdate() {
        // physics logic
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        // rendering logic
    }
}
