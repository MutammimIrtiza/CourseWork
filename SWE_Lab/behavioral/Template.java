
abstract class Game {
    // THE TEMPLATE METHOD
    public final void play() {
        initialize();
        startPlay();
        endPlay();
    }
    protected abstract void initialize();
    protected abstract void startPlay();
    protected abstract void endPlay();
}

class Chess extends Game {

    @Override
    protected void endPlay() {
        System.out.println();
    }

    @Override
    protected void initialize() {
        System.out.println();
    }

    @Override
    protected void startPlay() {
        System.out.println();
    }
    
}

public class Template {
    public static void main(String[] args) {
        Game game = new Chess();
        game.play();
    }
}
