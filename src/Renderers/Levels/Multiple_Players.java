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
import java.util.Random;

public class Multiple_Players implements GLEventListener, GameLoop {
    private InputManager inputManager;
    private ActionManager actionManager;
    private LoopState loopState;
    private EntityUtils entityUtils1 = new EntityUtils();
    private EntityUtils entityUtils2 = new EntityUtils();
    private Circle player1Circle;
    private Circle player2Circle;
    private Rectangle goalRectangle;
    private static final double MAX_POWER = 200.0;      // max "power" the player can set
    private static final double POWER_INCREMENT = 0.6;  // amount W/S changes power
    private static final double ANGLE_INCREMENT = 0.25;  // degrees per A/D press
    private static final double POWER_SCALE = 0.05;
    private double currentPower1 = 20.0;
    private double currentPower2 = 20.0;// sensible default
    private Vector2 gravity = new Vector2(0, -0.05); // tuned for visible arc (you can lower magnitude if too fast)
    private double angle1 = 45.0;
    private double angle2 = 45.0;// degrees (0 -> right, 90 -> up)

    private List<Shape> shapes1 = new ArrayList<>();
    private List<Shape> shapes2 = new ArrayList<>();

    private boolean isLaunched = false;
    private boolean isWon = false;
    private boolean isDead = false;

    private Vector2 velocity1 = new Vector2(0, 0);
    private Vector2 velocity2 = new Vector2(0, 0);
    private Rectangle MiddleTopWall2;
    private double score1 = 0;
    private double score2 = 0;
    private long timeElapsed;
    private TextRenderer textRenderer;
    private Random random = new Random();

    public Multiple_Players(InputManager inputManager) {
        this.inputManager = inputManager;
    }


    @Override
    public void physicsUpdate() {
        inputUpdate();


        if (isLaunched && !isWon && !isDead) {
            // inform entity utils of the current velocity
            entityUtils1.updatePlayerVelocity(velocity1);
            entityUtils2.updatePlayerVelocity(velocity2);

            // check collisions (may modify internal player velocity)
            entityUtils1.checkCollisions(player1Circle);
            entityUtils2.checkCollisions(player2Circle);

            // read back updated velocity from collisions
            velocity1 = entityUtils1.getPlayerVelocity();
            velocity2 = entityUtils2.getPlayerVelocity();


            // move player according to velocity
            player1Circle.move(velocity1);
            player2Circle.move(velocity2);

            // apply gravity for next frame
            velocity1 = velocity1.add(gravity);
            velocity2 = velocity2.add(gravity);


            checkWin();
            checkDie();


        }

    }

    private void checkDie() {
        if (entityUtils1.checkPlayerDying(player1Circle)) {
            isDead = true;
            resetLevel();
        }
        if (entityUtils2.checkPlayerDying(player2Circle)) {
            isDead = true;
            resetLevel();
        }
    }

    private void checkWin() {
        if (entityUtils1.checkPlayerWinning(player1Circle, goalRectangle)) {
//            isWon = true;
            long time = System.currentTimeMillis() - timeElapsed;
            score1 = Math.max(100000 - time, 0);
            //   LeaderboardHandler.save(13, new LeaderboardEntry(GlobalVariables.playerName, score1)); delete comment and add level number
            timeElapsed = System.currentTimeMillis();
        }
        if (entityUtils2.checkPlayerWinning(player2Circle, goalRectangle)) {
//            isWon = true;
            long time = System.currentTimeMillis() - timeElapsed;
            score2 = Math.max(100000 - time, 0);
            //    LeaderboardHandler.save(13, new LeaderboardEntry(GlobalVariables.playerName, score2));
            timeElapsed = System.currentTimeMillis();
        }
    }

    @Override
    public void renderUpdate(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();

        for (Shape shape : shapes1) shape.draw(gl);
        for (Shape shape : shapes2) shape.draw(gl);

        if (!isLaunched) {
            gl.glBegin(GL2.GL_LINES);

            if ((currentPower1 / MAX_POWER) * 100 <= 30)
                gl.glColor3f(0f, 1f, 0f);
            else if ((currentPower1 / MAX_POWER) * 100 <= 70)
                gl.glColor3f(1f, 1f, 0f);
            else
                gl.glColor3f(1f, 0f, 0f);

            double len = Math.max(10, currentPower1 * 0.4);
            double radius = player1Circle.getWidth() / 2.0;
            double rad = Math.toRadians(angle1);
            double x1 = player1Circle.getCenter().x() + radius * Math.cos(rad);
            double y1 = player1Circle.getCenter().y() + radius * Math.sin(rad);
            double x2 = x1 + len * Math.cos(rad);
            double y2 = y1 + len * Math.sin(rad);

            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
            gl.glEnd();
            gl.glBegin(GL2.GL_LINES);

            if ((currentPower2 / MAX_POWER) * 100 <= 30)
                gl.glColor3f(0f, 1f, 0f);
            else if ((currentPower2 / MAX_POWER) * 100 <= 70)
                gl.glColor3f(1f, 1f, 0f);
            else
                gl.glColor3f(1f, 0f, 0f);

            double len2 = Math.max(10, currentPower2 * 0.4);
            double radius2 = player2Circle.getWidth() / 2.0;
            double rad2 = Math.toRadians(angle2);
            double x1_2 = player2Circle.getCenter().x() + radius2 * Math.cos(rad2);
            double y1_2 = player2Circle.getCenter().y() + radius2 * Math.sin(rad2);
            double x2_2 = x1 + len2 * Math.cos(rad);
            double y2_2 = y1 + len2 * Math.sin(rad);

            gl.glVertex2d(x1_2, y1_2);
            gl.glVertex2d(x2_2, y2_2);
            gl.glEnd();
        }

        gl.glPopMatrix();

        if (isWon) {
            textRenderer = new TextRenderer(new Font("Monospaced", Font.BOLD, 60));
            textRenderer.beginRendering(800, 600);

            textRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            if (score1 > score2) {
                textRenderer.draw("P1 WIN!", 250, 300);
                textRenderer.draw("yourScore:" + (score1), 150, 150);
            } else if (score2 > score1) {
                textRenderer.draw("P2 WIN!", 250, 300);
                textRenderer.draw("yourScore:" + (score2), 150, 150);
            } else textRenderer.draw("It's Draw", 250, 300);

            textRenderer.endRendering();
        }

        entityUtils1.allowBounceSounds();
        entityUtils2.allowBounceSounds();
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

        // change these values to match that size.
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);

        // --- INPUT BINDINGS (small increments, only when not launched) ---
        actionManager.bind(Input.LEFT, () -> {
            if (!isLaunched) angle2 = (angle2 + ANGLE_INCREMENT) % 360;
        });
        actionManager.bind(Input.RIGHT, () -> {
            if (!isLaunched) angle2 = (angle2 - ANGLE_INCREMENT + 360) % 360;
        });
        actionManager.bind(Input.UP, () -> {
            if (!isLaunched) setCurrentPower(currentPower2 + POWER_INCREMENT, 2);
        });
        actionManager.bind(Input.DOWN, () -> {
            if (!isLaunched) setCurrentPower(currentPower2 - POWER_INCREMENT, 2);
        });
        actionManager.bind(Input.A, () -> {
            if (!isLaunched) angle1 = (angle1 + ANGLE_INCREMENT) % 360;
        });
        actionManager.bind(Input.D, () -> {
            if (!isLaunched) angle1 = (angle1 - ANGLE_INCREMENT + 360) % 360;
        });
        actionManager.bind(Input.W, () -> {
            if (!isLaunched) setCurrentPower(currentPower1 + POWER_INCREMENT, 1);
        });
        actionManager.bind(Input.S, () -> {
            if (!isLaunched) setCurrentPower(currentPower1 - POWER_INCREMENT, 1);
        });

        actionManager.bind(Input.R, this::resetLevel);

        // Launch: compute velocity using POWER_SCALE to avoid unit mismatch
        actionManager.bind(Input.Z, () -> {
            if (!isLaunched) {
                isLaunched = true;
                double rad = Math.toRadians(angle1);
                double speed = currentPower1 * POWER_SCALE;
                velocity1 = new Vector2(speed * Math.cos(rad), speed * Math.sin(rad));
                entityUtils1.updatePlayerVelocity(velocity1);
            }
        });
        actionManager.bind(Input.Space, () -> {
            if (!isLaunched) {
                isLaunched = true;
                double rad = Math.toRadians(angle2);
                double speed = currentPower2 * POWER_SCALE;
                velocity2 = new Vector2(speed * Math.cos(rad), speed * Math.sin(rad));
                entityUtils2.updatePlayerVelocity(velocity2);
            }
        });

        actionManager.bind(Input.Escape, this::togglePause);

        // shapes
        // player: start near bottom-left
        player1Circle = new Circle.Builder()
                .color(Color.WHITE)
                .radius(15)
                .angle(0)
                .center(new Point(100, 340)) // start pos
                .filled(true)
                .build();
        player2Circle = new Circle.Builder()
                .color(Color.WHITE)
                .radius(15)
                .angle(0)
                .center(new Point(600, 340)) // start pos
                .filled(true)
                .build();
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
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(0, 0))
                .restitution(0.0)
                .width(10)
                .height(1000)
                .build();

        Rectangle rightWall = new Rectangle.Builder()
                .color(Color.RED)
                .rotation(0)
                .fill(true)
                .origin(new Point(790, 0))
                .restitution(0.0)
                .width(10)
                .height(1000)
                .build();

        entityUtils1.addShape(goalRectangle);
        entityUtils1.addShape(floor);
        entityUtils1.addShape(ceiling);
        entityUtils1.addShape(leftWall);
        entityUtils1.addShape(rightWall);

        entityUtils2.addShape(goalRectangle);
        entityUtils2.addShape(floor);
        entityUtils2.addShape(ceiling);
        entityUtils2.addShape(leftWall);
        entityUtils2.addShape(rightWall);

        entityUtils1.updateGravity(gravity);
        entityUtils2.updateGravity(gravity);
        entityUtils1.updatePlayerVelocity(velocity1);
        entityUtils2.updatePlayerVelocity(velocity2);

        shapes1.clear();
        shapes1.add(player1Circle);
        shapes1.addAll(entityUtils1.getShapes());

        shapes2.clear();
        shapes2.add(player2Circle);
        shapes2.addAll(entityUtils2.getShapes());

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

    private void resetLevel() {
        // Reset flags
        isLaunched = false;
        isWon = false;
        isDead = false;

        // Reset player position
        int x1 = random.nextInt(20, 780);
        int x2 = random.nextInt(20, 780);
        int y1 = random.nextInt(20, 580);
        int y2 = random.nextInt(20, 580);
        player1Circle.setOrigin(new Point(x1, y1));
        player2Circle.setOrigin(new Point(x2, y2));

        int xg = random.nextInt(20, 780);
        int yg = random.nextInt(20, 580);
        goalRectangle.setOrigin(new Point(xg, yg));

        velocity1 = new Vector2(0, 0);
        entityUtils1.updatePlayerVelocity(velocity1);

        currentPower1 = 20.0;
        currentPower2 = 20.0;
        angle1 = 45.0;
        angle2 = 45.0;
    }

    public double getCurrentPower1() {
        return currentPower1;
    }

    public double getCurrentPower2() {
        return currentPower2;
    }

    public void setCurrentPower(double newPower, int player) {
        if (player == 1) {
            if (newPower > MAX_POWER) this.currentPower1 = MAX_POWER;
            else this.currentPower1 = Math.max(newPower, 5);
        } else if (player == 2) {
            if (newPower > MAX_POWER) this.currentPower2 = MAX_POWER;
            else this.currentPower2 = Math.max(newPower, 5);
        }
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
