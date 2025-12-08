package Pages;

import Game.GameLoop;
import Game.InputManager;
import Game.LoopState;
import Physics.ActionManager;
import Renderers.EntityUtils;
import Shapes.Circle;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class GLlevel1 implements GLEventListener , GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private EntityUtils entityUtils=new EntityUtils();
    private Circle playerCircle,Goal;
    private double MaxPower=100;
    private double minPower=0;


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void physicsUpdate() {

    }

    @Override
    public void renderUpdate(GL2 gl) {

    }

    @Override
    public void inputUpdate() {
      inputManager=new InputManager();
    }

    @Override
    public void togglePause() {
        GameLoop.super.togglePause();
    }

    @Override
    public void stop() {
        GameLoop.super.stop();
    }

    @Override
    public void start() {
        GameLoop.super.start();
    }

    @Override
    public boolean isPaused() {
        return GameLoop.super.isPaused();
    }

    @Override
    public void handleLoop(LoopState state, GL2 gl) {
        GameLoop.super.handleLoop(state, gl);
    }
}
