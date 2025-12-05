package Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds  {

private File soundFile;


    public Sounds(String Sound) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
       try{
soundFile = new File(Sound);
        AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(soundFile);
        Clip clip=AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }
}
