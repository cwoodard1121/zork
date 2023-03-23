package zork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import sun.audio.*;
import zork.Constants.CommandConstants;

public class Utils {

    /**
     * @param fileName
     * @return InputStream of the file selected, relative to the bin directory.
     */
    public static InputStream getFileFromBin(String fileName) {
        File soundFile = new File(new File(".").getPath() + "\\bin\\zork\\data\\" + fileName);
        try {
            return (InputStream) new FileInputStream(soundFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fileName
     * @return BufferedReader of the file that is selected, relative to the bin directory.
     */
    public static BufferedReader getReaderFromBin(String fileName) {
        File soundFile = new File(new File(".").getPath() + "\\bin\\zork\\data\\" + fileName);
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(soundFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Registers command to the command list.
     * @param command
     */
    public static void registerCommand(Class<? extends Command> command) {
        String name = command.getSimpleName().toLowerCase();
        System.out.println(name);
        try {
            Constructor<? extends Command> constructor = command.getConstructor(String.class);
            Command c = constructor.newInstance(name);
            CommandConstants.commands.put(name,c);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
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
                    AudioStream audioStream = new AudioStream(getFileFromBin(soundName));
                    AudioPlayer.player.start(audioStream);
                    int i = 0;
                    if(loop) {
                        while(Constants.SoundConstants.playSounds.get(soundName)) {
                            if(i >= seconds) {
                                AudioPlayer.player.stop(audioStream);
                                audioStream = new AudioStream(getFileFromBin(soundName));
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
