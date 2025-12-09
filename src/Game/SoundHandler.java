package Game;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SoundHandler {

    private static long lastPlay = 0;
    private static boolean mute = false;

    // Track active clips and whether they were paused by a mute
    private static class TrackedClip {
        final Clip clip;
        long pausedPositionMicro = -1; // -1 = not paused-by-mute

        TrackedClip(Clip clip) {
            this.clip = clip;
        }
    }

    private static final List<TrackedClip> activeClips = new ArrayList<>();

    public static void play(String sound) {
        play(sound, 1.0);
    }

    public static boolean isMuted() {
        return mute;
    }

    /**
     * Pause all currently playing clips (remembering their positions).
     * New sounds attempted while muted will not play.
     */
    public static void mute() {
        synchronized (activeClips) {
            if (mute) return;
            mute = true;

            for (TrackedClip tc : activeClips) {
                Clip c = tc.clip;
                try {
                    if (c.isRunning()) {
                        tc.pausedPositionMicro = c.getMicrosecondPosition();
                        c.stop();
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Unmute and resume any clips that were paused by mute().
     * Clips that finished while muted will not be resumed.
     */
    public static void unmute() {
        synchronized (activeClips) {
            if (!mute) return;
            mute = false;

            Iterator<TrackedClip> it = activeClips.iterator();
            while (it.hasNext()) {
                TrackedClip tc = it.next();
                Clip c = tc.clip;
                try {
                    // If the clip was paused by mute and hasn't finished, resume it
                    if (tc.pausedPositionMicro >= 0 && c.isOpen()) {
                        // Some clips may have reached end and been closed by the listener; check
                        long clipLength = c.getMicrosecondLength();
                        if (tc.pausedPositionMicro < clipLength) {
                            c.setMicrosecondPosition(tc.pausedPositionMicro);
                            c.start();
                        } else {
                            // finished while muted — close and remove
                            c.close();
                            it.remove();
                        }
                    }
                    tc.pausedPositionMicro = -1;
                } catch (Exception ignored) {
                    try {
                        c.close();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    it.remove();
                }
            }
        }
    }

    public static void toggleMute() {
        if (mute) unmute();
        else mute();
    }

    /**
     * Play a sound file. If muted, this will not start playback.
     * Keeps references to clips so they are not GC'd and so we can pause/resume/stop them.
     */
    public static void play(String sound, double volume) {
        long now = System.currentTimeMillis();
        if (now - lastPlay < 100) return; // 100 ms cooldown
        lastPlay = now;

        if (mute) return;

        try {
            File file = new File(sound);
            if (!file.exists()) {
                System.err.println("Sound file not found: " + sound);
                return;
            }

            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(in);

            final TrackedClip tc = new TrackedClip(clip);

            // Track and remove when done
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    synchronized (activeClips) {
                        if (tc.pausedPositionMicro >= 0) {
                            // paused-by-mute -> keep the clip (don't close/remove)
                            return;
                        }
                        // not paused by mute -> clip stopped naturally, clean up
                        try {
                            if (clip.isOpen()) clip.close();
                        } catch (Exception ignored) {}
                        activeClips.remove(tc);
                    }
                }
            });

            // volume control
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float min = gain.getMinimum();
                float max = gain.getMaximum();
                float dB = (float) (min + (max - min) * volume);
                try {
                    gain.setValue(dB);
                } catch (IllegalArgumentException ignored) {
                    // volume out of range; ignore
                }
            }

            // keep alive
            synchronized (activeClips) {
                activeClips.add(tc);
            }

            clip.start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Stop (and remove) all currently tracked clips immediately.
    public static void stopAll() {
        synchronized (activeClips) {
            for (TrackedClip tc : new ArrayList<>(activeClips)) {
                try {
                    tc.clip.stop();
                    tc.clip.close();
                } catch (Exception ignored) {
                }
            }
            activeClips.clear();
        }
    }
}
