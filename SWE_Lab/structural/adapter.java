package structural;

// Target
interface MediaPlayer {
    void play(String filename);
}

// Adaptee
class AdvancedPlayer {
    public void playVLC(String file) {System.out.println("VLC: " + file);}
    public void playMP4(String file) {System.out.println("MP4: " + file);}
}

class AdvancedPlayerAdapter implements MediaPlayer {
    private AdvancedPlayer adaptee;

    public AdvancedPlayerAdapter() {
        this.adaptee = new AdvancedPlayer();
    }

    @Override
    public void play(String filename) {
        if(filename.endsWith(".vlc")) {
            adaptee.playVLC(filename);
        } else if(filename.endsWith(".mp4")) {
            adaptee.playMP4(filename);
        }
    }
}

/**
 * adapter
 */
public class adapter {
    public static void main(String[] args) {
        MediaPlayer player = new AdvancedPlayerAdapter();
        player.play("movie.vlc");
        player.play("show.mp4");
    }
}
