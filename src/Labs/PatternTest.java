package Labs;

import java.util.ArrayList;
import java.util.List;
class Song{
    String title;
    String artist;
    public Song(String title, String artist) {
        this.artist = artist;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "Song{" + "title=" + title + ", artist=" + artist  + '}';
    }
}
class MP3Player{
    List<Song>songs;
    int curr;
    String status;
    public MP3Player(List<Song> listSongs) {
        songs = listSongs;
        curr = 0;
        status = "None";
    }

    public void pressPlay() {
        if(status.equals("playing")){
            System.out.printf("Song is already playing\n");
        }
        else{
            System.out.printf("Song %d is playing\n",curr);
        }
        status = "playing";
    }

    public void printCurrentSong() {
        System.out.printf("%s\n",songs.get(curr));
    }

    public void pressStop() {
        if(status.equals("stopped")){
            System.out.printf("Songs are stopped\n");
        }
        else if(status.equals("paused")){
            System.out.printf("Songs are already stopped\n");
        }
        else{
            System.out.printf("Song %d is paused\n",curr);
        }
        status = "paused";
        curr = 0;
    }

    public void pressREW() {
        System.out.printf("Reward...\n");
        curr--;
        if(curr < 0)
            curr = songs.size()-1;
        status = "stopped";
    }

    public void pressFWD() {
        System.out.printf("Forward...\n");
        curr++;
        if(curr == songs.size())
            curr = 0;
        status = "stopped";
    }

    @Override
    public String toString() {
        return "MP3Player{" +"currentSong = "+curr+ ", songList = " + songs + '}';
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


