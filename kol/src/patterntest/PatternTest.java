package patterntest;

import java.util.ArrayList;
import java.util.List;
public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}
class Song {
    private final String title;
    private final String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }

    @Override
    public String toString() {
        return String.format("%s - %s", title, artist);
    }
}

/* =======================
   COMMAND PATTERN
   ======================= */
interface Command {
    void execute();
}

class PlayCommand implements Command {
    private final MP3Player player;

    public PlayCommand(MP3Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.getState().pressPlay(player);
    }
}

class StopCommand implements Command {
    private final MP3Player player;

    public StopCommand(MP3Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.getState().pressStop(player);
    }
}

class FwdCommand implements Command {
    private final MP3Player player;

    public FwdCommand(MP3Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.getState().pressFWD(player);
    }
}

class RewCommand implements Command {
    private final MP3Player player;

    public RewCommand(MP3Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.getState().pressREW(player);
    }
}

/* =======================
   STATE PATTERN
   ======================= */
interface PlayerState {
    void pressPlay(MP3Player p);
    void pressStop(MP3Player p);
    void pressFWD(MP3Player p);
    void pressREW(MP3Player p);

    int code(); // 0 stopped, 1 playing, 2 paused (за toString)
}

class PlayingState implements PlayerState {
    @Override
    public void pressPlay(MP3Player p) {
        // повторно play -> пак е playing (обично задачите го печатат истото)
        p.printPlaying();
        p.setState(this);
    }

    @Override
    public void pressStop(MP3Player p) {
        // ако свири -> паузира
        p.printPaused();
        p.setState(p.getPausedState());
    }

    @Override
    public void pressFWD(MP3Player p) {
        // песната се паузира и следната станува моментална (кружно)
        p.nextSong();
        p.setState(p.getPausedState());
    }

    @Override
    public void pressREW(MP3Player p) {
        // песната се паузира и претходната станува моментална (кружно)
        p.prevSong();
        p.setState(p.getPausedState());
    }

    @Override
    public int code() {
        return 1;
    }
}

class PausedState implements PlayerState {
    @Override
    public void pressPlay(MP3Player p) {
        p.printPlaying();
        p.setState(p.getPlayingState());
    }

    @Override
    public void pressStop(MP3Player p) {
        // ако е веќе паузирано -> reset
        p.resetToStart();
        p.printStopped();
        p.setState(p.getStoppedState());
    }

    @Override
    public void pressFWD(MP3Player p) {
        p.nextSong();
        // останува paused (логично)
        p.setState(this);
    }

    @Override
    public void pressREW(MP3Player p) {
        p.prevSong();
        p.setState(this);
    }

    @Override
    public int code() {
        return 2;
    }
}

class StoppedState implements PlayerState {
    @Override
    public void pressPlay(MP3Player p) {
        p.printPlaying();
        p.setState(p.getPlayingState());
    }

    @Override
    public void pressStop(MP3Player p) {
        // ако е stopped -> останува stopped и reset
        p.resetToStart();
        p.printStopped();
        p.setState(this);
    }

    @Override
    public void pressFWD(MP3Player p) {
        p.nextSong();
        // останува stopped (не пушта ништо)
        p.setState(this);
    }

    @Override
    public void pressREW(MP3Player p) {
        p.prevSong();
        p.setState(this);
    }

    @Override
    public int code() {
        return 0;
    }
}

/* =======================
   CONTEXT (MP3Player)
   ======================= */
class MP3Player {
    private final List<Song> songs;
    private int currentSongIndex;

    // States (singletons per player)
    private final PlayerState playingState = new PlayingState();
    private final PlayerState pausedState  = new PausedState();
    private final PlayerState stoppedState = new StoppedState();

    private PlayerState state;

    // Commands (buttons)
    private final Command playCommand;
    private final Command stopCommand;
    private final Command fwdCommand;
    private final Command rewCommand;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        this.currentSongIndex = 0;
        this.state = stoppedState;

        this.playCommand = new PlayCommand(this);
        this.stopCommand = new StopCommand(this);
        this.fwdCommand  = new FwdCommand(this);
        this.rewCommand  = new RewCommand(this);
    }

    // Buttons required by judge
    public void pressPlay() { playCommand.execute(); }
    public void pressStop() { stopCommand.execute(); }
    public void pressFWD()  { fwdCommand.execute(); }
    public void pressREW()  { rewCommand.execute(); }

    public void printCurrentSong() {
        System.out.println(songs.get(currentSongIndex));
    }

    // Helpers used by states
    void nextSong() {
        currentSongIndex = (currentSongIndex + 1) % songs.size();
    }

    void prevSong() {
        currentSongIndex = (currentSongIndex - 1 + songs.size()) % songs.size();
    }

    void resetToStart() {
        currentSongIndex = 0;
    }

    void printPlaying() {
        System.out.printf("Song %d is playing%n", currentSongIndex);
    }

    void printPaused() {
        System.out.printf("Song %d is paused%n", currentSongIndex);
    }

    void printStopped() {
        System.out.println("Songs are stopped");
    }

    // State access
    PlayerState getState() { return state; }
    void setState(PlayerState state) { this.state = state; }

    PlayerState getPlayingState() { return playingState; }
    PlayerState getPausedState()  { return pausedState; }
    PlayerState getStoppedState() { return stoppedState; }

    @Override
    public String toString() {
        // формат што често се бара во вакви задачи: индекс + state code
        return String.format("MP3Player{currentSong = %d, state = %d}", currentSongIndex, state.code());
    }
}
