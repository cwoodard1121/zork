package zork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InitAscii {
        public static void fillAsciiArt (String roomName) throws FileNotFoundException, IOException {
        // \\bin\\zork\\data\\cutscene.txt
        String roomNameFile = roomName + ".txt";
        File f = new File(new File("").getAbsolutePath().concat("\\src\\zork\\zork\\locations\\" + roomNameFile));
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line = null;
        while((line = reader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                System.out.print(line.charAt(i));
            }
            System.out.println();
            continue;

        }
    }
}
