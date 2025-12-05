package Pages;

import com.jogamp.opengl.*;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import Game.GameLoop;
import Game.InputManager;

public class DevGlListener implements GLEventListener,GameLoop {
    private DevFrame frame = new DevFrame();
    private InputManager input = frame.getInputManager();

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
// use  this when having textures or buffers and need to remove it
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        // handle_loop() when you draw remember to update
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        if (input != null) {
            input.viewportWidth = width;
            input.viewportHeight = height;
            input.setOrthoBounds(0, width, 0, height);
        }
        //here you can reload the matrix mode and the ortho and the other staff
    }
    public void physicsUpdate() {
//   uses as updating the game parts on physics
    }

    @Override
    public void renderUpdate(GL2 gl) {
//   uses to render the components of the draw to update it
    }
}