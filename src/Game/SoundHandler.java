package Game;

import javax.sound.sampled.*;
import java.io.File;

public class SoundHandler {
    private static long lastPlay = 0;
    private static boolean mute;

    public static void play(String sound) {
        play(sound, 1);
    }

    public static void mute() {
        mute = true;
    }

    public static void unmute() {
        mute = false;
    }

    public static boolean isMuted() {
        return mute;
    }

    public static void play(String sound, double volume) {
        long now = System.currentTimeMillis();
        if (now - lastPlay < 100) return; // 100 ms cooldown
        lastPlay = now;
        if (mute) return;

        try {
            File file = new File(sound);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(in);

            // Apply volume
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                float min = gain.getMinimum();  // usually around -80 dB
                float max = gain.getMaximum();  // usually +6 dB

                // Convert 0.0–1.0 volume to decibels
                double dB = min + (max - min) * volume;

                gain.setValue((float) dB);
            }

            clip.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


