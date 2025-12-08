package Game;

import com.jogamp.opengl.GL2;

public interface GameLoop {

    double PHYSICS_STEP = 1.0 / 500.0;
    long NANO_TO_SEC = 1_000_000_000L;

    // game paused or not
    boolean[] GAME_PAUSED = new boolean[] { false };
    // (array so it's mutable even in interface)

    void physicsUpdate();
    void renderUpdate(GL2 gl);
    void inputUpdate();

    // pause/unpause toggle
    default void togglePause() {
        GAME_PAUSED[0] = !GAME_PAUSED[0];
    }

    // stop all loops
    default void stop() {
        GAME_PAUSED[0] = true;
    }

    //start all loops
    default void start() {
        GAME_PAUSED[0] = false;
    }

    default boolean isPaused() {
        return GAME_PAUSED[0];
    }

    // main loop
    default void handleLoop(LoopState state, GL2 gl) {
        inputUpdate();
        if (GAME_PAUSED[0]) {
            // Only render, no physics
            renderUpdate(gl);
            // restart time to avoid teleportation
            state.lastTime = System.nanoTime();
            return;
        }

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
