package datatypes;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class SoundPlayerUsingClip implements LineListener {

    boolean isPlaybackCompleted;
    
    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.START == event.getType()) {
            System.out.println("sound started");
        } else if (LineEvent.Type.STOP == event.getType()) {
            isPlaybackCompleted = true;
            System.out.println("sound ended");
        }
    }
}