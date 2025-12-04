package Game;

import com.jogamp.opengl.GL2;

public interface GameLoop {

    double PHYSICS_STEP = 1.0 / 500.0; // 500 Physics updates per second
    long NANO_TO_SEC = 1_000_000_000L;

    //what will be done every physics update.
    void physicsUpdate();

    //what will be done every render update (Frames per second).
    void renderUpdate(GL2 gl);

    //Ensures that the appropriate amount of physics updates are done every frame.
    default void handleLoop(LoopState state, GL2 gl) {
        long now = System.nanoTime();
        double deltaTime = (double) (now - state.lastTime) / NANO_TO_SEC;
        state.lastTime = now;

        state.accumulator += deltaTime;

        while (state.accumulator >= PHYSICS_STEP) {
            physicsUpdate();
            state.accumulator -= PHYSICS_STEP;
        }

        renderUpdate(gl);
    }
}
