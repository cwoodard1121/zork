package zork;

import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import sun.audio.*;

import datatypes.SoundPlayerUsingClip;
import zork.commands.EnterSubway;
import zork.commands.Go;
import zork.commands.Stop;

public class Constants {
    public static final class PlayerConstants {
        public static final int DEFAULT_HEALTH = 100;
    }

    public static final class SoundConstants {
        public static final HashMap<String,Boolean> playSounds = new HashMap<>();
    }

    public static final class RenderConstants {
        public static final int BORDER_LENGTH = 20;
        public static final int BORDER_HEIGHT = 20;
    }

    public static final class CommandConstants {
        public static final HashMap<String,Command> commands = new HashMap<>();
    }
        /**
         * This static block registers every command, do this after you make one.
         */
    public static void initCommands() {
        CommandConstants.commands.put("go", new Go("go"));
        CommandConstants.commands.put("stop", new Stop("stop"));
        CommandConstants.commands.put("entersubway", new EnterSubway("entersubway"));
    }

    public static synchronized void subwaySound() {
        playSound("subway.wav",3,false);
    }

    public static synchronized void playTitleSound() {
        playSound("mainmenu.wav",43,true);
    }


    public static synchronized void playSound(String soundName, int secs, boolean loop) {
        final int seconds = secs;
        Constants.SoundConstants.playSounds.put(soundName, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File soundFile = new File(new File(".").getPath() + "\\bin\\zork\\data\\" + soundName);
                    FileInputStream stream = new FileInputStream(soundFile);
                    AudioStream audioStream = new AudioStream(stream);
                    AudioPlayer.player.start(audioStream);
                    int i = 0;
                    if(loop) {
                        while(Constants.SoundConstants.playSounds.get(soundName)) {
                            if(i >= seconds) {
                                AudioPlayer.player.stop(audioStream);
                                soundFile = new File(new File(".").getPath() + "\\bin\\zork\\data\\" + soundName);
                                stream = new FileInputStream(soundFile);
                                audioStream = new AudioStream(stream);
                                AudioPlayer.player.start(audioStream);
                                i = 0;
                            }
                            i++;
                            Thread.sleep(1000);
                        }
                    } else {
                        while(i < seconds && Constants.SoundConstants.playSounds.get(soundName)) {
                            i++;
                            Thread.sleep(1000);
                        }
                    }
                    AudioPlayer.player.stop(audioStream);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        

    }

}
