import javax.swing.*;
import java.util.Random;

public class AI extends JFrame implements Players {

    public int move(){
        Random rand = new Random();
        return rand.nextInt(6) + 0;
    }



}
