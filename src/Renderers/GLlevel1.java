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

public class GLlevel1 implements GLEventListener, GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private LoopState loopState;
    private EntityUtils entityUtils = new EntityUtils();
    private Circle playerCircle;
    private Rectangle GoalRectangle;
    private double MaxPower = 100;
    private double currentPower = 0;
    private Vector2 gravity = new Vector2(0, 0);
    private double minPower = 0;
    private double angle = 0;
    private List<Shape> shapes = new ArrayList<>();
    private boolean isLunched = false, isWon = false,isDead = false;
    private Vector2 velocity;
    private Rectangle rectangle;

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

        actionManager.bind(Input.A, () -> {
            if (!isLunched)
                setAngle(angle + 1);
        });
        actionManager.bind(Input.S, () -> {
            if (!isLunched)
                setCurrentPower(currentPower - 1);
        });
        actionManager.bind(Input.D, () -> {
            if (!isLunched)
                setAngle(angle - 1);
        });
        actionManager.bind(Input.W, () -> {
            if (!isLunched)
                setCurrentPower(currentPower + 1);
        });
        actionManager.bind(Input.Z, () -> {
            isLunched = true;
            velocity = new Vector2(getCurrentPower() * Math.cos(Math.toRadians(angle)), getCurrentPower() * Math.sin(Math.toRadians(angle)));
        });
        actionManager.bind(Input.Escape, this::togglePause);
        entityUtils.updatePlayerVelocity(velocity);
        entityUtils.updateGravity(gravity);

        playerCircle = new Circle.Builder().color(Color.WHITE).radius(20).angle(0).restitution(0.3).center(new Point(50, 40)).filled(true).build();
        GoalRectangle = new Rectangle.Builder().color(Color.YELLOW).width(20).height(20).fill(false).origin(new Point(700, 200)).build();
        rectangle = new Rectangle.Builder().color(Color.BLUE).origin(new Point(400, 50)).restitution(0.5).width(75).height(100).fill(false).build();
        Rectangle rectangledown = new Rectangle.Builder().color(Color.RED).rotation(10).fill(true).origin(new Point(0, 0)).restitution(0.0).width(1000).height(10).build();
        Rectangle rectangleTop = new Rectangle.Builder().color(Color.RED).rotation(0).fill(true).origin(new Point(0, 600)).restitution(0.0).width(1000).height(10).build();
        Rectangle rectangleLeft = new Rectangle.Builder().color(Color.RED).rotation(0).fill(true).origin(new Point(700, 0)).restitution(0.0).width(10).height(1000).build();
        Rectangle rectangleRight = new Rectangle.Builder().color(Color.RED).rotation(0).fill(true).origin(new Point(0, 0)).restitution(0.0).width(10).height(1000).build();
        entityUtils.addShape(playerCircle);
        entityUtils.addShape(GoalRectangle);
        entityUtils.addShape(rectangle);
        entityUtils.addShape(rectangleTop);
        entityUtils.addShape(rectangledown);
        entityUtils.addShape(rectangleLeft);
        entityUtils.addShape(rectangleRight);

        shapes = entityUtils.getShapes();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        handleLoop(loopState, gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
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
        if (isLunched && !isWon && !isDead) {
            entityUtils.updatePlayerVelocity(velocity);
            entityUtils.checkCollisions(playerCircle);
            velocity = entityUtils.getPlayerVelocity();
            playerCircle.move(velocity);
            velocity.add(gravity);
            check_win();
            check_die();
        }
    }

    private void check_die() {
        //todo check each red rectangle if touched or not and gives dead
    }

    private void check_win()
    {
        double dx=playerCircle.getCenter().x()-GoalRectangle.getCenter().x();
        double dy=playerCircle.getCenter().y()-GoalRectangle.getCenter().y();
        double dis=Math.sqrt(dx*dx+dy*dy);
        if (dis<50)
            isWon = true;
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();

        for (Shape shape : shapes) {
            shape.draw(gl);
        }

        gl.glPopMatrix();

        entityUtils.allowBounceSounds();
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
        if (currentPower > MaxPower)
            this.currentPower = MaxPower;
        else if (currentPower < minPower)
            this.currentPower = minPower;
        else
            this.currentPower = currentPower;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }
}
