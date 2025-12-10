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
import com.jogamp.opengl.util.TileRenderer;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level7Renderer implements GLEventListener, GameLoop {
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
    private Vector2 gravity = new Vector2(0, -0.03); // Reduced gravity for water
    private double angle = 45.0;
    private double Tries;
    private double score;

    private List<Shape> shapes = new ArrayList<>();

    private boolean isLaunched = false;
    private boolean isWon = false;
    private boolean isDead = false;

    private Vector2 velocity = new Vector2(0, 0);

    // Level 7 specific: Water areas with drag
    private Rectangle waterArea1;
    private Rectangle waterArea2;
    private Rectangle floatingLog;
    private final double WATER_DRAG = 1.0; // Slows ball down (1.0 = no drag)
    private TextRenderer textRenderer;

    public Level7Renderer(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        loopState = new LoopState();
        actionManager = new ActionManager(inputManager);

        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.1f, 0.3f, 0.6f, 1); // Ocean blue

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
                .color(new Color(1f, 0.8f, 0.2f)) // Yellow/orange
                .radius(15)
                .angle(0)
                .center(new Point(100, 100))
                .filled(true)
                .build();

        // Goal (floating buoy)
        goalRectangle = new Rectangle.Builder()
                .color(Color.YELLOW)
                .width(40)
                .height(40)
                .fill(false)
                .origin(new Point(700, 100))
                .build();

        // Boundaries
        Rectangle floor = new Rectangle.Builder()
                .color(new Color(0.4f, 0.2f, 0.0f)) // Brown sand
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(0.0)
                .width(800)
                .height(10)
                .build();

        Rectangle ceiling = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 590))
                .restitution(0.0)
                .width(800)
                .height(10)
                .build();

        Rectangle leftWall = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(0.0)
                .width(10)
                .height(600)
                .build();

        Rectangle rightWall = new Rectangle.Builder()
                .color(Color.BLUE)
                .rotation(0)
                .fill(true)
                .origin(new Point(790, 0))
                .restitution(0.3)
                .width(10)
                .height(600)
                .build();

        // Water areas (apply drag)
        waterArea1 = new Rectangle.Builder()
                .color(new Color(0.2f, 0.5f, 0.8f, 0.5f)) // Semi-transparent blue
                .rotation(0)
                .fill(true)
                .origin(new Point(300, 100))
                .restitution(0.7)
                .width(100)
                .height(100)
                .build();

        waterArea2 = new Rectangle.Builder()
                .color(new Color(0.2f, 0.5f, 0.8f, 0.5f))
                .rotation(0)
                .fill(true)
                .origin(new Point(500, 200))
                .restitution(0.7)
                .width(150)
                .height(100)
                .build();

        // Island platforms
        Rectangle island1 = new Rectangle.Builder()
                .color(new Color(0.6f, 0.4f, 0.1f)) // Brown
                .rotation(0)
                .fill(true)
                .origin(new Point(300, 50))
                .restitution(0.5)
                .width(100)
                .height(50)
                .build();

        Rectangle island2 = new Rectangle.Builder()
                .color(new Color(0.6f, 0.4f, 0.1f))
                .rotation(0)
                .fill(true)
                .origin(new Point(450, 150))
                .restitution(0.5)
                .width(80)
                .height(40)
                .build();

        // Rocks/obstacles
        Circle rock1 = new Circle.Builder()
                .color(Color.DARK_GRAY)
                .filled(true)
                .center(new Point(350, 300))
                .restitution(0.3)
                .radius(25)
                .angle(0)
                .build();

        Circle rock2 = new Circle.Builder()
                .color(Color.DARK_GRAY)
                .filled(true)
                .center(new Point(600, 350))
                .restitution(0.3)
                .radius(30)
                .angle(0)
                .build();

        // Floating log
        floatingLog = new Rectangle.Builder()
                .color(new Color(0.5f, 0.3f, 0.1f))
                .rotation(20)
                .fill(true)
                .origin(new Point(550, 400))
                .restitution(0.8)
                .width(120)
                .height(20)
                .build();

        // Add to entity utils
        entityUtils.addShape(goalRectangle);
        entityUtils.addShape(floor);
        entityUtils.addShape(ceiling);
        entityUtils.addShape(leftWall);
        entityUtils.addShape(rightWall);
        entityUtils.addShape(waterArea1);
        entityUtils.addShape(waterArea2);
        entityUtils.addShape(island1);
        entityUtils.addShape(island2);
        entityUtils.addShape(rock1);
        entityUtils.addShape(rock2);
        entityUtils.addShape(floatingLog);

        entityUtils.updatePlayerVelocity(velocity);
        entityUtils.updateGravity(gravity);

        // Update local shapes list for rendering
        shapes.clear();
        shapes.add(playerCircle);
        shapes.addAll(entityUtils.getShapes());
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

        // Make floating log bob up and down
        floatingLog.move(new Vector2(0, Math.sin(System.currentTimeMillis() * 0.003) * 0.5));

        if (isLaunched && !isWon && !isDead) {
            // Apply water drag if player is in water
            if (waterArea1.getCollider().intersects(playerCircle.getCollider()) ||
                    waterArea2.getCollider().intersects(playerCircle.getCollider())) {
                velocity = velocity.scale(WATER_DRAG); // Slow down in water
            }

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
            Tries++;
            if (Tries < 3)
                resetLevel();
            else
                System.out.println("Die");
        }
    }

    private void checkWin() {
        if (entityUtils.checkPlayerWinning(playerCircle, goalRectangle)) {
            isWon = true;
            score = (-Tries + 3) * 1000;
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
        if (!isWon && Tries >= 3) {
            textRenderer = new TextRenderer(new Font("Monospaced", Font.BOLD, 60));
            textRenderer.beginRendering(800, 600);

            textRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            textRenderer.draw("YOU Lose!", 250, 300);

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
        if (Tries < 3) {
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
}
