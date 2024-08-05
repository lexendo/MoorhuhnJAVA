package org.example.moorhuhn;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages music playback for the game, including loading a playlist and playing tracks in sequence.
 */
public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private final List<Media> playlist;
    private int currentTrackIndex;

    /**
     * Constructs a MusicPlayer and loads the music playlist.
     */
    public MusicPlayer() {
        playlist = new ArrayList<>();
        loadPlaylist();
        currentTrackIndex = 0;
    }

    /**
     * Loads the music playlist from resources.
     */
    private void loadPlaylist() {
        try {
            URL firstSongUrl = getClass().getResource("/org/example/moorhuhn/Sounds/firstSong.mp3");
            URL secondSongUrl = getClass().getResource("/org/example/moorhuhn/Sounds/SecondSong.mp3");

            playlist.add(new Media(Objects.requireNonNull(firstSongUrl).toString()));
            playlist.add(new Media(Objects.requireNonNull(secondSongUrl).toString()));
        } catch (Exception e) {
            System.out.println("Error loading music: " + e.getMessage());
        }
    }

    /**
     * Starts playing the music playlist.
     */
    public void play() {
        if (!playlist.isEmpty()) {
            playTrack(currentTrackIndex);
        }
    }

    /**
     * Stops the currently playing music track.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // Private methods below

    private void playTrack(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        mediaPlayer = new MediaPlayer(playlist.get(index));
        mediaPlayer.setVolume(0.1);
        mediaPlayer.setOnEndOfMedia(this::playNextTrack);
        mediaPlayer.play();
    }

    private void playNextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
        playTrack(currentTrackIndex);
    }
}
