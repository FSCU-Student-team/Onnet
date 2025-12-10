package Renderers.Levels;

import Game.GameLoop;
import Game.Input;
import Game.InputManager;
import Game.LoopState;
import Physics.ActionManager;
import Renderers.EntityUtils;
import Shapes.*;
import Shapes.Color;
import Shapes.Point;
import Shapes.Rectangle;
import Shapes.Shape;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level11Renderer implements GLEventListener, GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private LoopState loopState;
    private EntityUtils entityUtils = new EntityUtils();
    private Circle playerCircle;
    private Rectangle goalRectangle;

    // Tunables
    private static final double MAX_POWER = 200.0;
    private static final double POWER_INCREMENT = 1.0;
    private static final double ANGLE_INCREMENT = 0.5;
    private static final double POWER_SCALE = 0.05;

    private double currentPower = 20.0;
    private Vector2 gravity = new Vector2(0, -0.1);
    private double angle = 45.0;

    private List<Shape> shapes = new ArrayList<>();

    private boolean isLaunched = false;
    private boolean isWon = false;
    private boolean isDead = false;

    private Vector2 velocity = new Vector2(0, 0);
    private double score = 0;

    private long timeElapsed;  // NEW — timer for scoring

    private TextRenderer textRenderer;

    @Override
    public void physicsUpdate() {
        inputUpdate();

        if (isLaunched && !isWon && !isDead) {
            entityUtils.updatePlayerVelocity(velocity);

            entityUtils.checkCollisions(playerCircle);

            velocity = entityUtils.getPlayerVelocity();

            playerCircle.move(velocity);

            velocity = velocity.add(gravity);

            checkWin();
            checkDie();
        }
    }

    private void checkDie() {
        if (entityUtils.checkPlayerDying(playerCircle)) {
            isDead = true;
            resetLevel(); // immediate reset, no tries, no losing
        }
    }

    private void checkWin() {
        if (entityUtils.checkPlayerWinning(playerCircle, goalRectangle)) {
            isWon = true;
            long time = System.currentTimeMillis() - timeElapsed;
            score = Math.max(100000 - time, 0);
        }
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();

        for (Shape shape : shapes) shape.draw(gl);

        if (!isLaunched) {
            gl.glBegin(GL2.GL_LINES);

            if ((currentPower / MAX_POWER) * 100 <= 30)
                gl.glColor3f(0f, 1f, 0f);
            else if ((currentPower / MAX_POWER) * 100 <= 70)
                gl.glColor3f(1f, 1f, 0f);
            else
                gl.glColor3f(1f, 0f, 0f);

            double len = Math.max(10, currentPower * 0.4);
            double radius = playerCircle.getWidth() / 2.0;
            double rad = Math.toRadians(angle);
            double x1 = playerCircle.getCenter().x() + radius * Math.cos(rad);
            double y1 = playerCircle.getCenter().y() + radius * Math.sin(rad);
            double x2 = x1 + len * Math.cos(rad);
            double y2 = y1 + len * Math.sin(rad);

            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
            gl.glEnd();
        }

        gl.glPopMatrix();

        if (isWon) {
            textRenderer = new TextRenderer(new Font("Monospaced", Font.BOLD, 60));
            textRenderer.beginRendering(800, 600);

            textRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            textRenderer.draw("YOU WIN!", 250, 300);
            textRenderer.draw("yourScore:" + (score), 150, 150);

            textRenderer.endRendering();
        }

        entityUtils.allowBounceSounds();
    }

    @Override
    public void inputUpdate() {
        actionManager.update();
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

        // INPUT
        actionManager.bind(Input.A, () -> {
            if (!isLaunched) angle = (angle + ANGLE_INCREMENT) % 360;
        });
        actionManager.bind(Input.D, () -> {
            if (!isLaunched) angle = (angle - ANGLE_INCREMENT + 360) % 360;
        });
        actionManager.bind(Input.W, () -> {
            if (!isLaunched) setCurrentPower(currentPower + POWER_INCREMENT);
        });
        actionManager.bind(Input.S, () -> {
            if (!isLaunched) setCurrentPower(currentPower - POWER_INCREMENT);
        });

        actionManager.bind(Input.R, this::resetLevel);

        actionManager.bind(Input.Space, () -> {
            if (!isLaunched) {
                isLaunched = true;
                double rad = Math.toRadians(angle);
                double speed = currentPower * POWER_SCALE;
                velocity = new Vector2(speed * Math.cos(rad), speed * Math.sin(rad));
                entityUtils.updatePlayerVelocity(velocity);

                // start timer on launch
                timeElapsed = System.currentTimeMillis();
            }
        });

        actionManager.bind(Input.Escape, this::togglePause);
        playerCircle = new Circle.Builder()
                .filled(true)
                .center(new Point(50, 50))
                .angle(0)
                .radius(20)
                .color(Color.WHITE)
                .restitution(0.5)
                .build();
        goalRectangle = new Rectangle.Builder()
                .fill(false)
                .restitution(0.01)
                .origin(new Point(640, 520))
                .height(50)
                .width(50)
                .rotation(0)
                .color(Color.YELLOW)
                .build();
        Circle circleDie1=new Circle.Builder()
                .filled(true)
                .center(new Point(600, 500))
                .angle(0)
                .radius(20)
                .color(Color.RED)
                .restitution(0.01)
                .build();
        Circle circleDie2=new Circle.Builder()
                .filled(true)
                .center(new Point(450, 400))
                .angle(0)
                .radius(20)
                .color(Color.RED)
                .restitution(0.01)
                .build();
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

    public void setCurrentPower(double newPower) {
        if (newPower > MAX_POWER)
            this.currentPower = MAX_POWER;
        else
            this.currentPower = Math.max(newPower, 5);
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public double getCurrentPower() {
        return currentPower;
    }

    private void resetLevel() {
        isLaunched = false;
        isWon = false;
        isDead = false;

        playerCircle.setOrigin(new Point(100, 100));

        velocity = new Vector2(0, 0);
        entityUtils.updatePlayerVelocity(velocity);

        currentPower = 20.0;
        angle = 45.0;
    }
}
