package zork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer;
import sun.audio.*;
import zork.Constants.CommandConstants;
import zork.Constants.SoundConstants;

public class Utils {

    /**
     * @param fileName
     * @return InputStream of the file selected, relative to the bin directory.
     */
    public static InputStream getFileStreamFromBin(String fileName) {
        File soundFile = new File(new File(".").getPath() + "\\bin\\zork\\data\\" + fileName);
        try {
            return (InputStream) new FileInputStream(soundFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 
     * @param fileName
     * @return File supplied
     */
    public static File getFileFromBin(String fileName) {
        return new File(new File(".").getPath() + "\\bin\\zork\\data\\" + fileName);
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
     * @param fileName
     * @return BufferedWriter of the file that is selected, relative to the bin directory.
     */
    public static BufferedWriter getWriterFromBin(String fileName) {
        File soundFile = new File(new File(".").getPath() + "\\bin\\zork\\data\\" + fileName);
        try {
            return new BufferedWriter(new FileWriter(soundFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
    *
    * @param packageName The base package
    * @return The classes
    * @throws ClassNotFoundException
    * @throws IOException
    */
    public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }


    /**
    * Recursive method used to find all classes in a given directory and subdirs.
    *
    * @param directory   The base directory
    * @param packageName The package name for classes found inside the base directory
    * @return The classes
    * @throws ClassNotFoundException
    */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }


    /**
     * Registers command to the command list.
     * @param command
     */
    public static void registerCommand(Class<? extends Command> command) {
        String name = command.getSimpleName().toLowerCase();
        if(Game.isTesting) {
            System.out.println("REGISTERED COMMAND:" +  name + " FULL CLASSPATH:" + command.getName());
        }
        try {
            Constructor<? extends Command> constructor = command.getConstructor(String.class);
            Command c = constructor.newInstance(name);
            CommandConstants.commands.put(name,c);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * plays ttc subway sound
     */
    public static synchronized void subwaySound() {
        playSound("subway.wav",3,false);
    }

    /**
     * plays title music
     */
    public static synchronized void playTitleSound() {
        playSound("mainmenu.wav",44,true);
    }


    /**
     * Stops the provided sound
     * @param soundName
     */
    public static void stopSound(String soundName) {
        soundName = soundName.replace(".wav", "").concat(".wav");
        SoundConstants.playSounds.put(soundName,false);
    }

    /**
     * Plays sound from the bin directory in WAV format.
     * @param sound The name of the file
     * @param secs The length of the file
     * @param loop Whether or not the sound should loop until cancelled
     */
    public static void playSound(String sound, int secs, boolean loop) {
        final int seconds = secs;
        InputStream s =  getFileStreamFromBin(sound);
        // Media hit = new Media(getFileFromBin(sound).toURI().toString());
        // MediaPlayer mediaPlayer = new MediaPlayer(hit);
        //mediaPlayer.play();
    }



    // public static synchronized void playSound(String sound, int secs, boolean loop) {
    //     final String soundName = sound.replace(".wav", "").concat(".wav");
    //     final int seconds = secs;
    //     Constants.SoundConstants.playSounds.put(soundName, true);
    //     new Thread(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 AudioStream audioStream = new AudioStream(getFileFromBin(soundName));
    //                 AudioPlayer.player.start(audioStream);
    //                 int i = 0;
    //                 if(loop) {
    //                     while(Constants.SoundConstants.playSounds.get(soundName)) {
    //                         if(i >= seconds) {
    //                             AudioPlayer.player.stop(audioStream);
    //                             audioStream = new AudioStream(getFileFromBin(soundName));
    //                             AudioPlayer.player.start(audioStream);
    //                             i = 0;
    //                         }
    //                         i++;
    //                         Thread.sleep(1000);
    //                     }
    //                 } else {
    //                     while(i < seconds && Constants.SoundConstants.playSounds.get(soundName)) {
    //                         i++;
    //                         Thread.sleep(1000);
    //                     }
    //                 }
    //                 AudioPlayer.player.stop(audioStream);
    //             } catch(Exception e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }).start();

        

    // }
}
