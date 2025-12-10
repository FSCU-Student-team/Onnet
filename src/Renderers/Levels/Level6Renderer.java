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

public class Level6Renderer implements GLEventListener, GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private LoopState loopState;
    private EntityUtils entityUtils = new EntityUtils();
    private Circle playerCircle;
    private Rectangle goalRectangle;
    private Rectangle movingPlatform;
    private Circle bouncingCircle;
    // Tunables
    private static final double MAX_POWER = 200.0;      // max "power" the player can set
    private static final double POWER_INCREMENT = 1.0;  // amount W/S changes power
    private static final double ANGLE_INCREMENT = 0.5;  // degrees per A/D press
    private static final double POWER_SCALE = 0.05;     // converts "power" -> velocity (pixels per physics step)
    // lower = slower launch, raise to speed up

    private double currentPower = 50.0;   // sensible default
    private Vector2 gravity = new Vector2(0, -0.05); // tuned for visible arc (you can lower magnitude if too fast)
    private double angle = 45.0;// degrees (0 -> right, 90 -> up)
    private double Tries;
    private double score;

    private List<Shape> shapes = new ArrayList<>();

    private boolean isLaunched = false;
    private boolean isWon = false;
    private boolean isDead = false;

    private Vector2 velocity = new Vector2(0, 0);

    // Level 6 specific variables
    private double platformSpeed = 100.0;
    private boolean platformMovingRight = true;
    private Vector2 circleVelocity = new Vector2(80, 60);
    private TextRenderer textRenderer;

    public Level6Renderer(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        loopState = new LoopState();
        actionManager = new ActionManager(inputManager);

        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 1);

        // change these values to match that size.
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);

        // --- INPUT BINDINGS (small increments, only when not launched) ---
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

        // Launch: compute velocity using POWER_SCALE to avoid unit mismatch
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

        // shapes
        // player: start near bottom-left
        playerCircle = new Circle.Builder()
                .color(Color.WHITE)
                .radius(15)
                .angle(0)
                .center(new Point(100, 100)) // start pos
                .filled(true)
                .build();

        // goal
        goalRectangle = new Rectangle.Builder()
                .color(Color.YELLOW)
                .width(40)
                .height(40)
                .fill(false)
                .origin(new Point(650, 500))
                .build();

        // static environment (platforms / walls)
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

        // moving platform
        movingPlatform = new Rectangle.Builder()
                .color(Color.CYAN)
                .rotation(0)
                .fill(true)
                .origin(new Point(200, 150))
                .restitution(0.8)
                .width(150)
                .height(20)
                .build();

        // bouncing obstacle circle
        bouncingCircle = new Circle.Builder()
                .color(Color.MAGENTA)
                .filled(true)
                .center(new Point(400, 300))
                .restitution(1.0)
                .radius(30)
                .angle(0)
                .build();

        // Challenge obstacles
        Rectangle obstacle1 = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(300, 0))
                .restitution(0.5)
                .width(50)
                .height(200)
                .build();

        Rectangle obstacle2 = new Rectangle.Builder()
                .color(Color.GREEN)
                .rotation(0)
                .fill(true)
                .origin(new Point(500, 250))
                .restitution(0.7)
                .width(50)
                .height(150)
                .build();

        Triangle ramp = new Triangle.Builder()
                .color(Color.ORANGE)
                .fill(true)
                .restitution(0.9)
                .addPoint(new Point(600, 100))
                .addPoint(new Point(750, 100))
                .addPoint(new Point(600, 250))
                .build();

        // IMPORTANT: don't add playerCircle to entityUtils shapes list (avoid self-collision)
        entityUtils.addShape(goalRectangle);
        entityUtils.addShape(floor);
        entityUtils.addShape(ceiling);
        entityUtils.addShape(leftWall);
        entityUtils.addShape(rightWall);
        entityUtils.addShape(movingPlatform);
        entityUtils.addShape(bouncingCircle);
        entityUtils.addShape(obstacle1);
        entityUtils.addShape(obstacle2);
        entityUtils.addShape(ramp);

        // set up entity utils with starting velocity and gravity
        entityUtils.updatePlayerVelocity(velocity);
        entityUtils.updateGravity(gravity);


        shapes.clear();
        shapes.add(playerCircle);
        shapes.add(goalRectangle);
        shapes.add(floor);
        shapes.add(ceiling);
        shapes.add(leftWall);
        shapes.add(rightWall);
        shapes.add(movingPlatform);
        shapes.add(bouncingCircle);
        shapes.add(obstacle1);
        shapes.add(obstacle2);
        shapes.add(ramp);

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        // nothing special needed here
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

        // platform moves left/right
        movingPlatform.move(new Vector2((platformMovingRight ? platformSpeed : -platformSpeed) * GameLoop.PHYSICS_STEP, 0));
        if (movingPlatform.getCenter().x() > 715) platformMovingRight = false;
        else if (movingPlatform.getCenter().x() < 85) platformMovingRight = true;

        // bouncing circle
        bouncingCircle.move(circleVelocity.scale(GameLoop.PHYSICS_STEP));
        Point center = bouncingCircle.getCenter();
        double radius = bouncingCircle.getWidth() / 2;

        if (center.x() - radius < 10 || center.x() + radius > 790) {
            circleVelocity = circleVelocity.withX(-circleVelocity.x());
        }
        if (center.y() - radius < 10 || center.y() + radius > 590) {
            circleVelocity = circleVelocity.withY(-circleVelocity.y());
        }


        if (isLaunched && !isWon && !isDead) {
            // inform entity utils of the current velocity
            entityUtils.updatePlayerVelocity(velocity);

            // check collisions (may modify internal player velocity)
            entityUtils.checkCollisions(playerCircle);

            // read back updated velocity from collisions
            velocity = entityUtils.getPlayerVelocity();

            // move player according to velocity
            playerCircle.move(velocity);

            // apply gravity for next frame
            velocity = velocity.add(gravity);


            checkWin();
            checkDie();


        }
    }

    private void checkDie() {
        // placeholder: you can check overlap with red rectangles here and set isDead
        if (entityUtils.checkPlayerDying(playerCircle)) {
            isDead = true;
            Tries+=1;
            if (Tries < 3)
                resetLevel();
            else
                System.out.println("Die");
        }
    }

    private void checkWin() {
        if (entityUtils.checkPlayerWinning(playerCircle, goalRectangle)) {
            isWon = true;
            score = Tries * 1000;
        }
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        // Draw all shapes
        for (Shape shape : shapes) {
            shape.draw(gl);
        }

        // Aim line (visible only before launch). Length scales with currentPower.
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
        if (!isWon && Tries >= 3) {
            textRenderer = new TextRenderer(new Font("Monospaced", Font.BOLD, 60));
            textRenderer.beginRendering(800, 600);

            textRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            textRenderer.draw("YOU Lose!", 250, 300);

            textRenderer.endRendering();
        }

        gl.glPopMatrix();

        // play any bounce sounds queued by entityUtils
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
        if (Tries < 3) {
            isLaunched = false;
            isWon = false;
            isDead = false;

            // Reset player position
            playerCircle.setOrigin(new Point(100, 100));

            velocity = new Vector2(0, 0);
            entityUtils.updatePlayerVelocity(velocity);

            currentPower = 50.0;
            angle = 45.0;
        }
    }
}
