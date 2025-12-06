package Game;

import Game.GameLoop;
import Pages.Page;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;
import java.awt.*;

public class GamePage extends JPanel implements Page, GLEventListener, GameLoop {

    private GLCanvas canvas;
    private FPSAnimator animator;

    public GamePage(JFrame frame) {
        setLayout(new BorderLayout());

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);

        animator = new FPSAnimator(canvas, 60, true);
        add(canvas, BorderLayout.CENTER);
    }

    @Override
    public void init() {
        animator.start();
    }

    @Override
    public void setupFrame() {}
    @Override
    public void setupAnimator() {}
    @Override
    public void addComponents() {}
    @Override
    public void addListeners() {}
    @Override
    public void handleEvents(java.awt.event.ActionEvent e) {}
    @Override
    public void dispose() {
        animator.stop();
    }
    @Override
    public boolean isVisible() { return true; }
    @Override
    public void setVisible(boolean b) { this.setVisible(b); }
    @Override
    public void redraw() { canvas.display(); }

    // -------------------- JOGL --------------------
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0f, 0f, 0f, 1f); // أسود
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // هنا يتم استدعاء GameLoop handleLoop
        handleLoop(new LoopState(), gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    // -------------------- GameLoop --------------------
    @Override
    public void physicsUpdate() {
        // هنا تحط تحديثات الفيزياء
    }

    @Override
    public void renderUpdate(GL2 gl) {
        // هنا تحط رسم اللعبة
    }
}
