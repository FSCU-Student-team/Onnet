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

public class Level11Renderer implements GLEventListener, GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private LoopState loopState;
    private EntityUtils entityUtils = new EntityUtils();
    private Circle playerCircle;
    private Rectangle goalRectangle;

    // Tunables
    private static final double MAX_POWER = 100.0;
    private static final double POWER_INCREMENT = 0.6;
    private static final double ANGLE_INCREMENT = 0.25;
    private static final double POWER_SCALE = 0.09;

    private double currentPower = 20.0;
    private Vector2 gravity = new Vector2(-0.0, -0.0); // Zero gravity in space
    private double angle = 45.0;
    private double score;
    private double Tries;

    private long timeElapsed;
    private TextRenderer textRenderer;

    private List<Shape> shapes = new ArrayList<>();

    private boolean isLaunched = false;
    private boolean isWon = false;
    private boolean isDead = false;

    private Vector2 velocity = new Vector2(0, 0);

    // Level 11 Specific: Black Hole & Orbiting Asteroids
    private Circle blackHole;
    private Circle asteroid1;
    private Circle asteroid2;
    private Circle asteroid3;

    // Orbit variables
    private double orbitAngle1 = 0.0;
    private double orbitAngle2 = 2.0; // Start at different angle
    private double orbitAngle3 = 4.0; // Start at different angle
    private final double ORBIT_SPEED = 0.01; // Speed of rotation
    private final Point CENTER_POINT = new Point(430, 250); // Center of Black Hole

    private final double BLACK_HOLE_GRAVITY_STRENGTH = 900.0;

    public Level11Renderer(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        loopState = new LoopState();
        actionManager = new ActionManager(inputManager);

        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.05f, 0.0f, 0.1f, 1); // Deep Space Dark Blue

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

        actionManager.bind(Input.Space, () -> {
            if (!isLaunched) {
                isLaunched = true;
                double rad = Math.toRadians(angle);
                double speed = currentPower * POWER_SCALE;
                velocity = new Vector2(speed * Math.cos(rad), speed * Math.sin(rad));
                entityUtils.updatePlayerVelocity(velocity);
            }
        });


        // Player
        playerCircle = new Circle.Builder()
                .color(Color.WHITE)
                .radius(15)
                .angle(0)
                .center(new Point(100, 150))
                .filled(true)
                .build();

        // Goal (Hidden behind the black hole)
        goalRectangle = new Rectangle.Builder()
                .color(Color.YELLOW)
                .width(40)
                .height(40)
                .fill(false)
                .origin(new Point(700, 500))
                .build();

        // The Black Hole (Gravity Well)
        blackHole = new Circle.Builder()
                .color(Color.RED)
                .filled(true)
                .center(CENTER_POINT)
                .restitution(0.0)
                .radius(40)
                .angle(0)
                .build();

        // Boundaries
        Rectangle floor = new Rectangle.Builder().color(Color.DARK_GRAY).fill(true).origin(new Point(0, 0)).width(800).height(10).build();
        Rectangle ceiling = new Rectangle.Builder().color(Color.DARK_GRAY).fill(true).origin(new Point(0, 590)).width(800).height(10).build();
        Rectangle leftWall = new Rectangle.Builder().color(Color.DARK_GRAY).fill(true).origin(new Point(0, 0)).width(10).height(600).build();
        Rectangle rightWall = new Rectangle.Builder().color(Color.DARK_GRAY).fill(true).origin(new Point(790, 0)).width(10).height(600).build();

        // Asteroids (Obstacles) - Initial positions will be updated in physics loop
        asteroid1 = new Circle.Builder().color(Color.DARK_GRAY).filled(true).center(new Point(250, 400)).radius(25).build();
        asteroid2 = new Circle.Builder().color(Color.DARK_GRAY).filled(true).center(new Point(550, 200)).radius(30).build();
        asteroid3 = new Circle.Builder().color(Color.DARK_GRAY).filled(true).center(new Point(400, 100)).radius(20).build();

        // Add to EntityUtils
        entityUtils.clearShapes();
        entityUtils.addShape(goalRectangle);
        entityUtils.addShape(floor);
        entityUtils.addShape(ceiling);
        entityUtils.addShape(leftWall);
        entityUtils.addShape(rightWall);
        entityUtils.addShape(blackHole);
        entityUtils.addShape(asteroid1);
        entityUtils.addShape(asteroid2);
        entityUtils.addShape(asteroid3);

        entityUtils.updatePlayerVelocity(velocity);
        entityUtils.updateGravity(gravity);

        // Render List
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

        // Update angles
        orbitAngle1 += ORBIT_SPEED;
        orbitAngle2 += ORBIT_SPEED * 0.8; // Different speed
        orbitAngle3 += ORBIT_SPEED * 1.2; // Faster

        // Radius of orbit (distance from center)
        double r1 = 110;
        double r2 = 140;
        double r3 = 90;

        // Calculate new positions
        asteroid1.setOrigin(new Point(
                CENTER_POINT.x() + r1 * Math.cos(orbitAngle1),
                CENTER_POINT.y() + r1 * Math.sin(orbitAngle1)
        ));

        asteroid2.setOrigin(new Point(
                CENTER_POINT.x() + r2 * Math.cos(orbitAngle2),
                CENTER_POINT.y() + r2 * Math.sin(orbitAngle2)
        ));

        asteroid3.setOrigin(new Point(
                CENTER_POINT.x() + r3 * Math.cos(orbitAngle3),
                CENTER_POINT.y() + r3 * Math.sin(orbitAngle3)
        ));
        if (isLaunched && !isWon && !isDead) {

            // Custom Gravity Logic (Black Hole Pull
            Point pPos = playerCircle.getCenter();
            Point holePos = blackHole.getCenter();

            double dx = holePos.x() - pPos.x();
            double dy = holePos.y() - pPos.y();
            double dist = Math.sqrt(dx * dx + dy * dy);

            // Avoid division by zero and extreme forces at center
            dist = Math.max(dist, 10);

            // Normalize vector
            double dirX = dx / dist;
            double dirY = dy / dist;

            // Force formula (inverse square law approximation)
            double force = BLACK_HOLE_GRAVITY_STRENGTH / (dist * 0.1);

            // Apply force to velocity
            Vector2 gravityPull = new Vector2(dirX * force * GameLoop.PHYSICS_STEP, dirY * force * GameLoop.PHYSICS_STEP);
            velocity = velocity.add(gravityPull);


            entityUtils.updatePlayerVelocity(velocity);
            entityUtils.checkCollisions(playerCircle);
            velocity = entityUtils.getPlayerVelocity();
            playerCircle.move(velocity);

            checkWin();
            checkDie();
        }
    }

    private void checkDie() {
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
            LeaderboardHandler.save(1, new LeaderboardEntry(GlobalVariables.playerName, score));
            timeElapsed = System.currentTimeMillis();
        }
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();

        for (Shape shape : shapes) {
            shape.draw(gl);
        }

        if (!isLaunched) {
            gl.glBegin(GL2.GL_LINES);
            // Power indicator color
            if ((currentPower / MAX_POWER) * 100 <= 30) gl.glColor3f(0f, 1f, 0f);
            else if ((currentPower / MAX_POWER) * 100 <= 70) gl.glColor3f(1f, 1f, 0f);
            else gl.glColor3f(1f, 0f, 0f);

            double len = Math.max(10, currentPower * 0.4);
            double rad = Math.toRadians(angle);
            double radius = playerCircle.getWidth() / 2.0;
            double x1 = playerCircle.getCenter().x() + radius * Math.cos(rad);
            double y1 = playerCircle.getCenter().y() + radius * Math.sin(rad);
            double x2 = x1 + 10 + len * Math.cos(rad);
            double y2 = y1 + 10 + len * Math.sin(rad);

            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
            gl.glEnd();
        }

        gl.glPopMatrix();

        // UI
        if (isWon) {
            textRenderer = new TextRenderer(new Font("Monospaced", Font.BOLD, 60));
            textRenderer.beginRendering(800, 600);
            textRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            textRenderer.draw("YOU WIN!", 250, 300);
            textRenderer.draw("Score: " + (int) score, 250, 200);
            textRenderer.endRendering();
        }

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
        isLaunched = false;
        isWon = false;
        isDead = false;
        playerCircle.setOrigin(new Point(100, 150));
        velocity = new Vector2(0, 0);
        entityUtils.updatePlayerVelocity(velocity);
        currentPower = 20.0;
        angle = 45.0;
    }
}
