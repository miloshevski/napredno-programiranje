package lab8;

import java.util.ArrayList;
import java.util.List;

class Song {
    private final String title;
    private final String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return String.format("Song{title=%s, artist=%s}", title, artist);
    }
}

class MP3Player {

    private enum State { STOPPED, PAUSED, PLAYING }

    private final List<Song> songList;
    private int currentSong;
    private State state;

    public MP3Player(List<Song> songs) {
        this.songList = new ArrayList<>(songs);
        this.currentSong = 0;
        this.state = State.STOPPED;
    }

    public void pressPlay() {
        if (state == State.PLAYING) {
            System.out.println("Song is already playing");
            return;
        }
        System.out.printf("Song %d is playing%n", currentSong);
        state = State.PLAYING;
    }

    public void pressStop() {
        if (state == State.PLAYING) {
            System.out.printf("Song %d is paused%n", currentSong);
            state = State.PAUSED;
        }else if(state == State.STOPPED){
            System.out.println("Songs are already stopped");
        }else {
            currentSong = 0;
            state = State.STOPPED;
            System.out.println("Songs are stopped");
        }
    }

    public void pressFWD() {
        System.out.println("Forward...");
        // песната се паузира + следна станува моментална (кружно)
        state = State.PAUSED;
        currentSong = (currentSong + 1) % songList.size();
    }

    public void pressREW() {
        System.out.println("Reward...");
        // песната се паузира + претходна станува моментална (кружно)
        state = State.PAUSED;
        currentSong = (currentSong - 1 + songList.size()) % songList.size();
    }

    public void printCurrentSong() {
        System.out.println(songList.get(currentSong));
    }

    @Override
    public String toString() {
        return String.format(
                "MP3Player{currentSong = %d, songList = %s}",
                currentSong,
                songList
        );
    }
}


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

//Vasiot kod ovde