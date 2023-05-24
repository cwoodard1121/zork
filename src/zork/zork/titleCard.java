package zork;

import javax.swing.Renderer;

public class titleCard {
    public void printTitle() throws InterruptedException {
        Graphics g = new Graphics(null);
        int delay = 2;
        System.out.println();
        g.slowTextSpeed(" _ _ _ ___| |___ ___ _____ ___   | |_ ___       ", delay);
        g.slowTextSpeed("| | | | -_| |  _| . |     | -_|  |  _| . |_ _ _ ", delay);
        g.slowTextSpeed("|_____|___|_|___|___|_|_|_|___|  |_| |___|_|_|_|", delay);
        System.out.println("\n");
        g.slowTextSpeed("     .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *", delay);
        g.slowTextSpeed("    _____________________________________________________________________________________", delay);
        g.slowTextSpeed("*   |                                                                                   | *", delay);
        g.slowTextSpeed("  * | ██████╗ ██████╗ ██╗███╗   ███╗███████╗ ██████╗ ██╗   ██╗███████╗███████╗████████╗ |    *", delay);
        g.slowTextSpeed("*   | ██╔══██╗██╔══██╗██║████╗ ████║██╔════╝██╔═══██╗██║   ██║██╔════╝██╔════╝╚══██╔══╝ | *", delay);
        g.slowTextSpeed("  * | ██████╔╝██████╔╝██║██╔████╔██║█████╗  ██║   ██║██║   ██║█████╗  ███████╗   ██║    |    *", delay);
        g.slowTextSpeed("*   | ██╔═══╝ ██╔══██╗██║██║╚██╔╝██║██╔══╝  ██║▄▄ ██║██║   ██║██╔══╝  ╚════██║   ██║    | *", delay);
        g.slowTextSpeed("  * | ██║     ██║  ██║██║██║ ╚═╝ ██║███████╗╚██████╔╝╚██████╔╝███████╗███████║   ██║    |    *", delay);
        g.slowTextSpeed("*   | ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝     ╚═╝╚══════╝ ╚══▀▀═╝  ╚═════╝ ╚══════╝╚══════╝   ╚═╝    | *", delay);
        g.slowTextSpeed("  * |___________________________________________________________________________________|    *", delay);
        System.out.println();
        g.slowTextSpeed("     .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *  .  *", delay);
        System.out.println();
        g.slowTextSpeed(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::", delay);
        g.slowTextSpeed("A text-based adventure video game created by:", delay);
        System.out.println();
        delay = 25;
        g.slowTextSpeed("C. Woodard", delay);
        System.out.println("\n");
        g.slowTextSpeed("E. McMullen", delay);
        System.out.println("\n");
        g.slowTextSpeed("M. Carnegie", delay);
        System.out.println("\n");
        g.slowTextSpeed("L. Minato", delay);
        System.out.println("\n");
        delay = 2;
        g.slowTextSpeed(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::", delay);
        g.slowTextSpeed("___________________________________________________________________________________", delay);
        System.out.print("\nType Start to begin: ");
    }

    // \n\n C.Woodard \n\n E.McMullen \n\n M.Carnegie \n\n and L.Minato\n"
}

