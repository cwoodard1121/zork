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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
    public static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
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
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
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
    public static void registerCommand(Class<?> command) {
        String name = command.getSimpleName().toLowerCase();
        try {
            Constructor<?> constructor = command.getConstructor(String.class);
            Command c = (Command) constructor.newInstance(name);
            CommandConstants.commands.put(name,c);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }






public static class SoundHandler {

    private static final Thread radioPlayerThread = getRadioPlayerThread();

    private static volatile boolean stopped = false;
    private static volatile boolean interrupted = false;

    private static final Queue<String> songQueueTemplate = new ConcurrentLinkedQueue<>();
    private static Queue<String> songQueue = new ConcurrentLinkedQueue<>();


    public static synchronized void playSong(String song) {
        final String soundName = song;
                try {
            
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(getFileFromBin(soundName));
        
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    int i = 0;
                        while(i < (TimeUnit.MICROSECONDS.toSeconds(clip.getMicrosecondLength()))) {
                            if(interrupted) break;
                            i++;
                            Thread.sleep(1000);
                        }
                        interrupted = false;

                    clip.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    public static void stop() {
        stopped = true;
        interrupted = true;
    }

    public static void loopQueuedSongs() {
        while(true) {
            if(!stopped) {
                if(songQueue.size() > 0) {
                    String song = songQueue.poll();
                    playSong(song);
                } else {
                    songQueue = songQueueTemplate;
                }
            }
        }
    }

    private static Thread getRadioPlayerThread() {
        return new Thread(new Runnable() {

            @Override
            public void run() {
                loopQueuedSongs();
            }
            
        });
    }

    public static void startAfterInterruption() {
        interrupted = false;
        stopped = false;
    }

    public static void skip() {
        interrupted = true;
    }

    public static void shuffle() {

    }

    static {
        /**
         * add file names here
         */
        songQueueTemplate.add("omission.wav");
        songQueueTemplate.add("house_of_the_rising_sun.wav");
        songQueueTemplate.add("american_pie.wav");
        songQueueTemplate.add("stairway_to_heaven.wav");
        radioPlayerThread.start();

    }

    /**
     * plays ttc subway sound
     */
    public static synchronized void subwaySound() {
        playSound("subway.wav",false);
    }


    /**
     * plays title music
     */
    public static synchronized void playTitleSound() {
        playSound("mainmenu.wav",true);
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
    public static synchronized void playSound(String sound, boolean loop) {
        stop();
        final String soundName = sound.replace(".wav", "").concat(".wav");
        Constants.SoundConstants.playSounds.put(soundName, true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
            
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(getFileFromBin(soundName));
        
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    if(loop) clip.loop(1000000);
                    clip.start();
                    int i = 0;
                        while((!loop && i < (TimeUnit.MICROSECONDS.toSeconds(clip.getMicrosecondLength())) && SoundConstants.playSounds.get(soundName)) || (loop && SoundConstants.playSounds.get(soundName)) || clip.isActive() && SoundConstants.playSounds.get(soundName)) {
                            i++;
                            Thread.sleep(1000);
                        }

                    clip.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }).start();

    }
    }
}
