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
import Game.LeaderboardHandler;
import Game.LeaderboardEntry;
import Game.GlobalVariables;

public class Level12Renderer implements GLEventListener, GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private LoopState loopState;
    private EntityUtils entityUtils = new EntityUtils();
    private Circle playerCircle;
    private Rectangle goalRectangle;
    // Tunables
    private static final double MAX_POWER = 200.0;
    private static final double POWER_INCREMENT = 0.6;
    private static final double ANGLE_INCREMENT = 0.25;
    private static final double POWER_SCALE = 0.05;

    private double currentPower = 50.0;
    private Vector2 gravity = new Vector2(0, -0.05);
    private double angle = 135.0; // Start aiming left/up

    private List<Shape> shapes = new ArrayList<>();

    private boolean isLaunched = false;
    private boolean isWon = false;
    private boolean isDead = false;

    private Vector2 velocity = new Vector2(0, 0);
    private TextRenderer textRenderer;
    private double score;
    private long timeElapsed;
    private double Tries;

    // Factory elements
    private Rectangle conveyorBelt1;
    private Rectangle conveyorBelt2;
    private Circle gear1;
    private Circle gear2;
    private Rectangle piston;
    private boolean pistonExtending = true;
    private double pistonTimer = 0;

    // Conveyor speeds
    private final double CONVEYOR_SPEED_1 = 80.0;
    private final double CONVEYOR_SPEED_2 = -60.0; // Opposite direction

    public Level12Renderer(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        loopState = new LoopState();
        actionManager = new ActionManager(inputManager);

        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.3f, 0.3f, 0.3f, 1); // Factory gray

        // change these values to match that size.
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);

        // --- INPUT BINDINGS (small increments, only when not launched) ---
        actionManager.bind(Input.A, () -> {
            if (!isLaunched) angle = (angle + ANGLE_INCREMENT) % 360;
            System.out.println(angle);
        });
        actionManager.bind(Input.D, () -> {
            if (!isLaunched) angle = (angle - ANGLE_INCREMENT + 360) % 360;
            System.out.println(angle);
        });
        actionManager.bind(Input.W, () -> {
            if (!isLaunched) setCurrentPower(currentPower + POWER_INCREMENT);
            System.out.println(currentPower);
        });
        actionManager.bind(Input.S, () -> {
            if (!isLaunched) setCurrentPower(currentPower - POWER_INCREMENT);
            System.out.println(currentPower);
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

        // Player (metal ball)
        playerCircle = new Circle.Builder()
                .color(new Color(0.7f, 0.7f, 0.7f)) // Steel gray
                .radius(15)
                .angle(0)
                .center(new Point(700, 100)) // Start on right side
                .filled(true)
                .build();

        // Goal (factory output chute)
        goalRectangle = new Rectangle.Builder()
                .color(Color.YELLOW)
                .width(60)
                .height(60)
                .fill(false)
                .origin(new Point(50, 100)) // Goal on left side
                .build();

        // Factory floor and walls
        Rectangle floor = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(0.0)
                .width(800)
                .height(20)
                .build();

        Rectangle ceiling = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 580))
                .restitution(0.0)
                .width(800)
                .height(20)
                .build();

        Rectangle leftWall = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(0.0)
                .width(20)
                .height(600)
                .build();

        Rectangle rightWall = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(780, 0))
                .restitution(0.0)
                .width(20)
                .height(600)
                .build();

        // CONVEYOR BELT 1 (moves left)
        conveyorBelt1 = new Rectangle.Builder()
                .color(new Color(0.2f, 0.2f, 0.3f)) // Dark blue/black
                .rotation(0)
                .fill(true)
                .origin(new Point(100, 150))
                .restitution(0.8)
                .width(300)
                .height(30)
                .build();

        // CONVEYOR BELT 2 (moves right)
        conveyorBelt2 = new Rectangle.Builder()
                .color(new Color(0.2f, 0.2f, 0.3f))
                .rotation(0)
                .fill(true)
                .origin(new Point(400, 300))
                .restitution(0.8)
                .width(300)
                .height(30)
                .build();

        // GEARS
        gear1 = new Circle.Builder()
                .color(new Color(0.5f, 0.4f, 0.2f)) // Bronze
                .filled(true)
                .center(new Point(250, 180))
                .restitution(0.7)
                .radius(40)
                .angle(0)
                .build();

        gear2 = new Circle.Builder()
                .color(new Color(0.5f, 0.4f, 0.2f))
                .filled(true)
                .center(new Point(550, 330))
                .restitution(0.7)
                .radius(35)
                .angle(0)
                .build();

        // PISTON (moves up/down)
        piston = new Rectangle.Builder()
                .color(new Color(0.6f, 0.1f, 0.1f)) // Red
                .rotation(0)
                .fill(true)
                .origin(new Point(200, 250))
                .restitution(0.5)
                .width(40)
                .height(80)
                .build();

        // Factory obstacles
        Rectangle crate1 = new Rectangle.Builder()
                .color(new Color(0.6f, 0.4f, 0.2f)) // Brown
                .rotation(0)
                .fill(true)
                .origin(new Point(500, 100))
                .restitution(0.6)
                .width(60)
                .height(60)
                .build();

        Rectangle crate2 = new Rectangle.Builder()
                .color(new Color(0.6f, 0.4f, 0.2f))
                .rotation(15)
                .fill(true)
                .origin(new Point(350, 400))
                .restitution(0.6)
                .width(70)
                .height(70)
                .build();

        // Ramps
        Triangle ramp1 = new Triangle.Builder()
                .color(new Color(0.5f, 0.5f, 0.5f))
                .fill(true)
                .restitution(0.8)
                .addPoint(new Point(600, 200))
                .addPoint(new Point(750, 200))
                .addPoint(new Point(600, 350))
                .build();

        Triangle ramp2 = new Triangle.Builder()
                .color(new Color(0.5f, 0.5f, 0.5f))
                .fill(true)
                .restitution(0.8)
                .addPoint(new Point(100, 350))
                .addPoint(new Point(100, 500))
                .addPoint(new Point(250, 350))
                .build();

        // Add to entity utils
        entityUtils.addShape(goalRectangle);
        entityUtils.addShape(floor);
        entityUtils.addShape(ceiling);
        entityUtils.addShape(leftWall);
        entityUtils.addShape(rightWall);
        entityUtils.addShape(conveyorBelt1);
        entityUtils.addShape(conveyorBelt2);
        entityUtils.addShape(gear1);
        entityUtils.addShape(gear2);
        entityUtils.addShape(piston);
        entityUtils.addShape(crate1);
        entityUtils.addShape(crate2);
        entityUtils.addShape(ramp1);
        entityUtils.addShape(ramp2);

        entityUtils.updatePlayerVelocity(velocity);
        entityUtils.updateGravity(gravity);

        // For rendering
        shapes.add(playerCircle);
        shapes.add(goalRectangle);
        shapes.add(floor);
        shapes.add(ceiling);
        shapes.add(leftWall);
        shapes.add(rightWall);
        shapes.add(conveyorBelt1);
        shapes.add(conveyorBelt2);
        shapes.add(gear1);
        shapes.add(gear2);
        shapes.add(piston);
        shapes.add(crate1);
        shapes.add(crate2);
        shapes.add(ramp1);
        shapes.add(ramp2);

        timeElapsed = System.currentTimeMillis();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {}

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

        // Animate factory parts
        updateFactoryParts();

        if (isLaunched && !isWon && !isDead) {
            // Apply conveyor belt forces if player is on them
            applyConveyorForces();

            entityUtils.updatePlayerVelocity(velocity);
            entityUtils.checkCollisions(playerCircle);
            velocity = entityUtils.getPlayerVelocity();
            playerCircle.move(velocity);
            velocity = velocity.add(gravity);

            checkWin();
            checkDie();
        }
    }

    private void updateFactoryParts() {
        // Rotate gears
        gear1.rotate(25/360.0);
        gear2.rotate(-20/360.0);

        // Move piston up and down
        pistonTimer += GameLoop.PHYSICS_STEP;
        if (pistonTimer > 5.0) { // Change direction every 2 seconds
            pistonExtending = !pistonExtending;
            pistonTimer = 0;
        }

        double pistonSpeed = 40.0 * GameLoop.PHYSICS_STEP;
        if (pistonExtending) {
            piston.move(new Vector2(0, pistonSpeed));
        } else {
            piston.move(new Vector2(0, -pistonSpeed));
        }
    }

    private void applyConveyorForces() {
        // Check if player is on conveyor belt 1
        if (conveyorBelt1.getCollider().intersects(playerCircle.getCollider())) {
            velocity = velocity.add(new Vector2(-CONVEYOR_SPEED_1 * GameLoop.PHYSICS_STEP, 0));
        }

        // Check if player is on conveyor belt 2
        if (conveyorBelt2.getCollider().intersects(playerCircle.getCollider())) {
            velocity = velocity.add(new Vector2(-CONVEYOR_SPEED_2 * GameLoop.PHYSICS_STEP, 0));
        }
    }

    private void checkDie() {
        // placeholder: you can check overlap with red rectangles here and set isDead
        if (entityUtils.checkPlayerDying(playerCircle)) {
            isDead = true;
            Tries += 1;
            resetLevel();

        }
    }

    private void checkWin() {
        if (entityUtils.checkPlayerWinning(playerCircle, goalRectangle)) {
            isWon = true;
            score = Math.max(100000 - (System.currentTimeMillis() - timeElapsed), 0);
            LeaderboardHandler.save(12, new LeaderboardEntry(GlobalVariables.playerName, score));
            timeElapsed = System.currentTimeMillis();
        }
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // Draw conveyor belt patterns (stripes)
        drawConveyorPattern(gl, conveyorBelt1, CONVEYOR_SPEED_1 > 0);
        drawConveyorPattern(gl, conveyorBelt2, CONVEYOR_SPEED_2 > 0);

        // Draw all shapes
        for (Shape shape : shapes) {
            shape.draw(gl);
        }

        // Draw gear teeth
        drawGearTeeth(gl, gear1);
        drawGearTeeth(gl, gear2);

        if (!isLaunched) {
            gl.glBegin(GL2.GL_LINES);
            if ((currentPower / MAX_POWER) * 100 <= 30)
                gl.glColor3f(0f, 1f, 0f);
            else if ((currentPower / MAX_POWER) * 100 <= 70)
                gl.glColor3f(1f, 1f, 0f);
            else
                gl.glColor3f(1f, 0f, 0f);

            double len = Math.max(30, currentPower * 0.4);
            double rad = Math.toRadians(angle);
            double x1 = playerCircle.getCenter().x();
            double y1 = playerCircle.getCenter().y();
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

    private void drawConveyorPattern(GL2 gl, Rectangle belt, boolean movingRight) {
        // Draw stripes on conveyor belt
        gl.glColor3f(0.8f, 0.8f, 0.2f); // Yellow stripes
        gl.glLineWidth(3.0f);
        gl.glBegin(GL2.GL_LINES);

        double x = belt.getCenter().x();
        double y = belt.getCenter().y();
        double width = belt.getWidth();
        double height = belt.getHeight();

        // Draw diagonal stripes
        for (double i = 0; i < width; i += 20) {
            double x1 = x + i;
            double y1 = y;
            double x2 = x + i + 10;
            double y2 = y + height;

            if (!movingRight) {
                // Reverse direction for left-moving belt
                x1 = x + width - i;
                x2 = x + width - i - 10;
            }

            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
        }
        gl.glEnd();
        gl.glLineWidth(1.0f);
    }

    private void drawGearTeeth(GL2 gl, Circle gear) {
        // Draw gear teeth around the circle
        gl.glColor3f(0.3f, 0.2f, 0.1f); // Dark brown
        gl.glBegin(GL2.GL_TRIANGLES);

        Point center = gear.getCenter();
        double radius = gear.getWidth() / 2.0; // FIXED: Use getWidth() instead of getRadius()
        int teeth = 12;

        for (int i = 0; i < teeth; i++) {
            double angle1 = Math.toRadians(i * (360.0 / teeth) + gear.getAngle());
            double angle2 = Math.toRadians((i + 0.5) * (360.0 / teeth) + gear.getAngle());
            double angle3 = Math.toRadians((i + 1) * (360.0 / teeth) + gear.getAngle());

            // Outer tooth point
            double toothLength = radius * 0.3;
            double x2 = center.x() + (radius + toothLength) * Math.cos(angle2);
            double y2 = center.y() + (radius + toothLength) * Math.sin(angle2);

            // Base points
            double x1 = center.x() + radius * Math.cos(angle1);
            double y1 = center.y() + radius * Math.sin(angle1);
            double x3 = center.x() + radius * Math.cos(angle3);
            double y3 = center.y() + radius * Math.sin(angle3);

            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
            gl.glVertex2d(x3, y3);
        }
        gl.glEnd();
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
        playerCircle.setOrigin(new Point(700, 100));
        velocity = new Vector2(0, 0);
        entityUtils.updatePlayerVelocity(velocity);
        currentPower = 50.0;
        angle = 135.0;
    }
}
