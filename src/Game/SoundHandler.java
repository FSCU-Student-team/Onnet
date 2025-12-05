package Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundHandler {
    private SoundHandler() {

    }

    public static void play(String sound){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sound));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
