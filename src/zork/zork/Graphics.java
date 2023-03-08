package zork;
import java.io.*;

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
    
    public void showCutScene(long frameTime) throws InterruptedException, IOException {
        
        
        File f = new File(new File("").getAbsolutePath().concat("\\bin\\zork\\data\\cutscene.txt"));
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line = null;
        boolean isText = true;
        while((line = reader.readLine()) != null) {
            if(line.contains("@")) {
                isText = !isText;
            }
            if(line.contains("&")) {
                Thread.sleep(frameTime);
            } else {
                for (int i = 0; i < line.length(); i++) {
                    System.out.print(line.charAt(i));
                    if((Character.isLetter(line.charAt(i)) && isText)) {
                        Thread.sleep(150);
                    }
                }
                System.out.println();
                //System.out.println(line);
            }
        }
        Thread.sleep(frameTime);

    }
}








