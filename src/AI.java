import java.util.Random;

public class AI {

    /**
     * @return random position so the AI can move
     */
    public int move(){
        Random rand = new Random();
        return rand.nextInt(6) + 0;
    }



}
