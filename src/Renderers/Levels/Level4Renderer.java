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

public class Level4Renderer implements GLEventListener, GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private LoopState loopState;
    private EntityUtils entityUtils = new EntityUtils();
    private Circle playerCircle;
    private Rectangle goalRectangle;

    // Tunables
    private static final double MAX_POWER = 200.0;      // max "power" the player can set
    private static final double POWER_INCREMENT = 1.0;  // amount W/S changes power
    private static final double ANGLE_INCREMENT = 0.5;  // degrees per A/D press
    private static final double POWER_SCALE = 0.05;     // converts "power" -> velocity (pixels per physics step)
    // lower = slower launch, raise to speed up

    private double currentPower = 20.0;   // sensible default
    private Vector2 gravity = new Vector2(0, -0.05); // tuned for visible arc (you can lower magnitude if too fast)
    private double angle = 135.0;          // degrees (0 -> right, 90 -> up)

    private List<Shape> shapes = new ArrayList<>();

    private boolean isLaunched = false;
    private boolean isWon = false;
    private boolean isDead = false;

    private Vector2 velocity = new Vector2(0, 0);
    private double Tries ;
    private double score = 0;
    private TextRenderer textRenderer;

    public Level4Renderer(InputManager inputManager) {
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
                .center(new Point(320, 35)) // start pos
                .filled(true)
                .build();

        // goal
        goalRectangle = new Rectangle.Builder()
                .color(Color.YELLOW)
                .width(70)
                .height(30)
                .fill(false)
                .origin(new Point(450, 50))
                .build();

        // static environment (platforms / walls)
        Rectangle floor = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(0.0)
                .width(1000)
                .height(10)
                .build();

        Rectangle ceiling = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 600))
                .restitution(0.0)
                .width(1000)
                .height(10)
                .build();

        Rectangle leftWall = new Rectangle.Builder()
                .color(Color.GREEN)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(1.0)
                .width(80)
                .height(1000)
                .build();

        Rectangle rightWall = new Rectangle.Builder()
                .color(Color.GRAY)
                .rotation(0)
                .fill(true)
                .origin(new Point(720, 0))
                .restitution(0.25)
                .width(80)
                .height(1000)
                .build();


        Rectangle ramp1 = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(300, 100))
                .restitution(0.5)
                .width(100)
                .height(100)
                .build();
        Rectangle ramp2 = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(350, 10))
                .restitution(0.5)
                .width(50)
                .height(90)
                .build();
        Rectangle ramp3 = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(300, 80))
                .restitution(0.5)
                .width(50)
                .height(20)
                .build();
        Rectangle ramp4 = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(300, 10))
                .restitution(0.5)
                .width(50)
                .height(10)
                .build();


        // IMPORTANT: don't add playerCircle to entityUtils shapes list (avoid self-collision)
        entityUtils.addShape(goalRectangle);
        entityUtils.addShape(floor);
        entityUtils.addShape(ceiling);
        entityUtils.addShape(leftWall);
        entityUtils.addShape(rightWall);

        entityUtils.addShape(ramp1);
        entityUtils.addShape(ramp2);
        entityUtils.addShape(ramp3);
        entityUtils.addShape(ramp4);


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
        shapes.add(ramp1);
        shapes.add(ramp2);
        shapes.add(ramp3);
        shapes.add(ramp4);


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
        if (entityUtils.checkPlayerDying(playerCircle)) {
            isDead = true;
            Tries += 1;
            if (Tries<3)
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
        gl.glPushMatrix();

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

        gl.glPopMatrix();
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
        isLaunched = false;
        isWon = false;
        isDead = false;

        // Reset player position
        playerCircle.setOrigin(new Point(320, 35));

        velocity = new Vector2(0, 0);
        entityUtils.updatePlayerVelocity(velocity);

        currentPower = 50.0;
        angle = 45.0;
    }
}
