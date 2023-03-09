package zork;
import java.io.*;

import javax.xml.transform.stream.StreamSource;

public class Graphics {
    private Game game;


    public Graphics(Game game) {
        this.game = game;
    }

    public void render() {
        StringBuilder builder = new StringBuilder();
        
    }


    public String createBorder(int length, int height) {
        StringBuilder borderBuilder = new StringBuilder();
        // borderBuilder.append()
        //TODO: FINISH
        return null;
    }

    public void slowTextSpeed(String text, int delay) throws InterruptedException {
        for(int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));
            Thread.sleep(delay);
        }
        System.out.println();
    }
    
    public void showCutScene(long frameTime) throws InterruptedException, IOException {
        
        
        File f = new File(new File("").getAbsolutePath().concat("\\bin\\zork\\data\\cutscene.txt"));
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line = null;
        boolean isText = true;
        boolean isTextBypass = false;
        while((line = reader.readLine()) != null) {
            if(line.contains("@")) {
                isText = !isText;
                continue;
            } if(line.contains("&")) {
                Thread.sleep(frameTime);
                continue;
            } if(line.contains("~")) {
                isTextBypass = !isTextBypass;
                continue;
            }
            
            else {
                for (int i = 0; i < line.length(); i++) {
                    System.out.print(line.charAt(i));
                    if(((Character.isLetter(line.charAt(i)) && isText) || isTextBypass)) {
                        Thread.sleep(75);
                    }
                }
                System.out.println();
            } if(line.contains("~")) {
                isTextBypass = !isTextBypass;
                continue;
            }
        }
        Thread.sleep(frameTime);

    }
}








