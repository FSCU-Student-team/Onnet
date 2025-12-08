package Pages;

import Game.GameLoop;
import Game.Input;
import Game.InputManager;
import Game.LoopState;
import Physics.ActionManager;
import Renderers.EntityUtils;
import Shapes.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.util.ArrayList;
import java.util.List;

public class GLlevel1 implements GLEventListener, GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private LoopState loopState;
    private EntityUtils entityUtils = new EntityUtils();
    private Circle playerCircle;
    private Rectangle GoalRectangle;
    private double MaxPower = 100;
    private double currentPower = 0;
    private Vector2 Force = new Vector2(0, 0);
    private Vector2 gravity = new Vector2(0, 0);
    private double minPower = 0;
    private double angle = 0;
    private List<Shape> shapes = new ArrayList<>();
    private boolean isLunched = false;

    public GLlevel1() {
        inputManager = new InputManager();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        loopState = new LoopState();
        actionManager = new ActionManager(inputManager);
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 1);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);

        actionManager.bind(Input.A, () -> setAngle(angle + 1));
        actionManager.bind(Input.S, () -> setCurrentPower(currentPower - 1));
        actionManager.bind(Input.D, () -> setAngle(angle - 1));
        actionManager.bind(Input.W, () -> setCurrentPower(currentPower + 1));
        actionManager.bind(Input.Z, () -> isLunched = true);
        actionManager.bind(Input.Escape, this::togglePause);

        playerCircle =new Circle.Builder().color(Color.BLUE).radius(20).angle(0).center(new Point(50,40)).filled(true).build();
        GoalRectangle =new Rectangle.Builder().color(Color.YELLOW).width(20).height(20).fill(false).origin(new Point(500,500)).build();

        entityUtils.addShape(playerCircle);entityUtils.addShape(GoalRectangle);
        shapes=entityUtils.getShapes();

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
        actionManager.update();
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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle % 360;
    }

    public double getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(double currentPower) {
        if (currentPower < minPower)
            this.currentPower = minPower;
        else
            this.currentPower = currentPower;
    }
}
