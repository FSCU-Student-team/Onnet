package Renderers.Levels;

import Game.*;
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

public class Level8Renderer implements GLEventListener, GameLoop {
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

    private double currentPower = 50.0;
    private Vector2 gravity = new Vector2(0, -0.05);
    private double angle = 45.0;
    private double Tries;
    private double score;

    private long timeElapsed;

    private List<Shape> shapes = new ArrayList<>();

    private boolean isLaunched = false;
    private boolean isWon = false;
    private boolean isDead = false;

    private Vector2 velocity = new Vector2(0, 0);

    // Level 10 specific: Final boss level with multiple moving parts
    private Rectangle rotatingObstacle1;
    private Rectangle rotatingObstacle2;
    private Circle movingTarget;
    private boolean targetMovingUp = true;
    private TextRenderer textRenderer;

    public Level8Renderer(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        loopState = new LoopState();
        actionManager = new ActionManager(inputManager);

        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.2f, 0.0f, 0.3f, 1); // Purple background

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);

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
            }
        });
        actionManager.bind(Input.Escape, this::togglePause);

        // Player
        playerCircle = new Circle.Builder()
                .color(Color.WHITE)
                .radius(15)
                .angle(0)
                .center(new Point(100, 100))
                .filled(true)
                .build();

        // Goal - Small and hard to hit!
        goalRectangle = new Rectangle.Builder()
                .color(Color.YELLOW)
                .width(25)
                .height(25)
                .fill(false)
                .origin(new Point(600, 550))
                .build();

        // Boundaries
        Rectangle floor = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(0.0)
                .width(800)
                .height(10)
                .build();

        Rectangle ceiling = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 590))
                .restitution(0.0)
                .width(800)
                .height(10)
                .build();

        Rectangle leftWall = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(0.0)
                .width(10)
                .height(600)
                .build();

        Rectangle rightWall = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(790, 0))
                .restitution(0.0)
                .width(10)
                .height(600)
                .build();

        // Rotating obstacles
        rotatingObstacle1 = new Rectangle.Builder()
                .color(Color.CYAN)
                .rotation(0)
                .fill(true)
                .origin(new Point(250, 200))
                .restitution(0.7)
                .width(80)
                .height(20)
                .build();

        rotatingObstacle2 = new Rectangle.Builder()
                .color(Color.MAGENTA)
                .rotation(45)
                .fill(true)
                .origin(new Point(500, 350))
                .restitution(0.7)
                .width(60)
                .height(60)
                .build();

        // Moving target obstacle
        movingTarget = new Circle.Builder()
                .color(Color.GREEN)
                .filled(true)
                .center(new Point(300, 300))
                .restitution(1.0)
                .radius(25)
                .angle(0)
                .build();

        // Complex maze-like obstacles
        Rectangle maze1 = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(200, 0))
                .restitution(0.5)
                .width(20)
                .height(150)
                .build();

        Rectangle maze2 = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(200, 250))
                .restitution(0.5)
                .width(20)
                .height(150)
                .build();

        Rectangle maze3 = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(400, 100))
                .restitution(0.5)
                .width(20)
                .height(200)
                .build();

        Triangle spike = new Triangle.Builder()
                .color(Color.ORANGE)
                .fill(true)
                .restitution(0.3)
                .addPoint(new Point(400, 600))
                .addPoint(new Point(350, 600))
                .addPoint(new Point(375, 500))
                .build();

        // Add to entity utils
        entityUtils.addShape(goalRectangle);
        entityUtils.addShape(floor);
        entityUtils.addShape(ceiling);
        entityUtils.addShape(leftWall);
        entityUtils.addShape(rightWall);
        entityUtils.addShape(rotatingObstacle1);
        entityUtils.addShape(rotatingObstacle2);
        entityUtils.addShape(movingTarget);
        entityUtils.addShape(maze1);
        entityUtils.addShape(maze2);
        entityUtils.addShape(maze3);
        entityUtils.addShape(spike);

        entityUtils.updatePlayerVelocity(velocity);
        entityUtils.updateGravity(gravity);

        shapes.clear();
        shapes.add(playerCircle);
        shapes.addAll(entityUtils.getShapes());

        timeElapsed = System.currentTimeMillis();
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

        // Rotating obstacles
        rotatingObstacle1.rotate(40 / 360.0);
        rotatingObstacle2.rotate(-30 / 360.0);

        // Moving target
        if (targetMovingUp) {
            movingTarget.move(new Vector2(0, 80 * GameLoop.PHYSICS_STEP));
            if (movingTarget.getCenter().y() > 450) targetMovingUp = false;
        } else {
            movingTarget.move(new Vector2(0, -80 * GameLoop.PHYSICS_STEP));
            if (movingTarget.getCenter().y() < 260) targetMovingUp = true;
        }

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
        // placeholder: you can check overlap with red rectangles here and set isDead
        if (entityUtils.checkPlayerDying(playerCircle)) {
            isDead = true;
            Tries++;
            resetLevel();
        }
    }

    private void checkWin() {
        if (entityUtils.checkPlayerWinning(playerCircle, goalRectangle)) {
            isWon = true;
            score = Math.max(100000 - (System.currentTimeMillis() - timeElapsed), 0);
            LeaderboardHandler.save(8, new LeaderboardEntry(GlobalVariables.playerName, score));
        }
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        for (Shape shape : shapes) {
            shape.draw(gl);
        }

        if (!isLaunched) {
            gl.glBegin(GL2.GL_LINES);
            // use white (or whatever color your shapes use)
            if ((currentPower / MAX_POWER) * 100 <= 30)//green
                gl.glColor3f(0f, 1f, 0f);
            else if ((currentPower / MAX_POWER) * 100 <= 70)//Yellow
                gl.glColor3f(1f, 1f, 0f);
            else if ((currentPower / MAX_POWER) * 100 >= 70)//red
                gl.glColor3f(1f, 0f, 0f);

            double len = Math.max(10, currentPower * 0.4); // visual length; tweak multiplier if desired
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
        if (isWon) {
            textRenderer = new TextRenderer(new Font("Monospaced", Font.BOLD, 60));
            textRenderer.beginRendering(800, 600);

            textRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f); // أخضر
            textRenderer.draw("YOU WIN!", 250, 300);
            textRenderer.draw("yourScore:" + (score), 150, 150);

            textRenderer.endRendering();
        }

        gl.glPopMatrix();
        entityUtils.allowBounceSounds();
    }

    @Override
    public void inputUpdate() {
        actionManager.update();
    }

    public double getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(double newPower) {
        if (newPower > MAX_POWER) this.currentPower = MAX_POWER;
        else this.currentPower = Math.max(newPower, 5);
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    private void resetLevel() {
        // Reset flags
        isLaunched = false;
        isWon = false;
        isDead = false;

        // Reset player position
        playerCircle.setOrigin(new Point(100, 100));

        velocity = new Vector2(0, 0);
        entityUtils.updatePlayerVelocity(velocity);

        currentPower = 20.0;
    }
}
