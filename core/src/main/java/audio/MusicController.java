package audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.HashMap;
import java.util.Map;


import static utilities.constants.AudiConstants.DEFAULT_MUSIC_VOLUME;

public class MusicController {

    // Stores all music tracks with a unique ID
    private static Map<String, Music> musics;

    // Currently playing music
    private static Music currentMusic;
    private static String idCurrentMusic;
    private static float currentMusicVolume;

    // Pause state
    private static boolean paused;

    // Fade effect variables
    private static final float FADE_STEP = 0.02f;
    private static float fadeStep;
    private static float targetVolume;
    private static boolean fading;

    // Crossfade effect variables
    private static Music fadeOutMusic;
    private static float fadeOutVolume;
    private static String nextId;
    private static boolean crossFading;

    /**
     * Constructor initializes the music map and state variables.
     */
    public MusicController() {
        musics = new HashMap<>();
        paused = false;
        fading = false;
        crossFading = false;
    }

    /**
     * Creates a new Music instance from the provided file path.
     * @param path The internal path to the audio file.
     * @return A Music instance.
     */
    public Music generateMusicFromPath(String path) {
        return Gdx.audio.newMusic(Gdx.files.internal(path));
    }

    /**
     * Adds a music instance to the controller's map using an ID.
     * @param id Identifier for the music.
     * @param music Music object to store.
     */
    public void addMusic(String id, Music music) {
        musics.put(id, music);
    }

    /**
     * Plays a music track by ID with default volume and no looping.
     * Does not restart if music is already playing.
     * @param id Identifier of the music to play.
     */
    public void playMusic(String id) {
        if (!musics.containsKey(id)) {
            Gdx.app.error("MusicController", "Music with Id " + id + " not found!");
        }
        if (currentMusic != null) {
            Gdx.app.debug("MusicController", "A music is already in play mode!");
        } else {
            currentMusic = musics.get(id);
            idCurrentMusic = id;
            currentMusic.setVolume(DEFAULT_MUSIC_VOLUME);
            currentMusicVolume = DEFAULT_MUSIC_VOLUME;
            currentMusic.setLooping(false);
            currentMusic.play();
        }
    }

    /**
     * Plays a music track by ID with a specific volume and no looping.
     * @param id Identifier of the music.
     * @param volume Volume level between 0.0 and 1.0.
     */
    public void playMusic(String id, float volume) {
        if (!musics.containsKey(id)) {
            Gdx.app.error("MusicController", "Music with Id " + id + " not found!");
        }
        if (currentMusic != null) {
            Gdx.app.debug("MusicController", "A music is already in play mode!");
        } else {
            currentMusic = musics.get(id);
            idCurrentMusic = id;
            currentMusic.setVolume(Math.clamp(volume, 0f, 1f));
            currentMusicVolume = Math.clamp(volume, 0f, 1f);
            currentMusic.setLooping(false);
            currentMusic.play();
        }
    }

    /**
     * Plays a music track with specified looping, default volume.
     * @param id Identifier of the music.
     * @param looping Whether the music should loop.
     */
    public void playMusic(String id, boolean looping) {
        if (!musics.containsKey(id)) {
            Gdx.app.error("MusicController", "Music with Id " + id + " not found!");
        }
        if (currentMusic != null) {
            Gdx.app.debug("MusicController", "A music is already in play mode!");
        } else {
            currentMusic = musics.get(id);
            idCurrentMusic = id;
            currentMusic.setVolume(DEFAULT_MUSIC_VOLUME);
            currentMusicVolume = DEFAULT_MUSIC_VOLUME;
            currentMusic.setLooping(looping);
            currentMusic.play();
        }
    }

    /**
     * Plays music with a specific volume and looping setting.
     * @param id Identifier of the music.
     * @param volume Volume level between 0.0 and 1.0.
     * @param looping Whether the music should loop.
     */
    public void playMusic(String id, float volume, boolean looping) {
        if (!musics.containsKey(id)) {
            Gdx.app.error("MusicController", "Music with Id " + id + " not found!");
        }
        if (currentMusic == musics.get(id)) {
            Gdx.app.debug("MusicController", "A music is already in play mode!");
        } else {
            currentMusic = musics.get(id);
            currentMusic.setVolume(Math.clamp(volume, 0f, 1f));
            currentMusicVolume = Math.clamp(volume, 0f, 1f);
            currentMusic.setLooping(looping);
            currentMusic.play();
        }
    }

    /**
     * Pauses the currently playing music.
     */
    public void pauseCurrentMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            paused = true;
            currentMusic.pause();
        }
    }

    /**
     * Resumes the paused music if available.
     */
    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            paused = false;
            currentMusic.play();
        }
    }

    /**
     * Starts a fade effect to the specified volume target.
     * @param targetVolume The volume to reach (between 0 and 1).
     */
    public void fadeTo(float targetVolume) {
        MusicController.targetVolume = Math.clamp(targetVolume, 0f, 1f);
        fading = true;
    }

    /**
     * Requests an increase in volume using the default fade step.
     */
    public void increaseVolume() {
        fadeTo(currentMusicVolume + FADE_STEP);
    }

    /**
     * Requests a decrease in volume using the default fade step.
     */
    public void decreaseVolume() {
        fadeTo(currentMusicVolume - FADE_STEP);
    }

    // Optional method for accelerated volume changes based on frame rate.
    /*
    public void accelerationVolume() {
        float accelerationFactor = 2.0f;
        fadeTo(currentMusicVolume + FADE_STEP * Gdx.graphics.getDeltaTime() * 60 * accelerationFactor);
    }
    */

    /**
     * Starts a crossfade transition between current music and the next.
     * @param nextId ID of the next music to play.
     */
    public void crossFade(String nextId) {
        if (!musics.containsKey(nextId)) {
            Gdx.app.error("MusicController", nextId + " Not Found !");
            return;
        }
        if (idCurrentMusic != null && idCurrentMusic.equals(nextId)) {
            return;
        }
        MusicController.nextId = nextId;
        fadeOutMusic = currentMusic;
        fadeOutVolume = currentMusicVolume;
        crossFading = true;

        Music newMusic = musics.get(nextId);
        newMusic.setVolume(0);
        newMusic.setLooping(true);
        newMusic.play();

        currentMusic = newMusic;
        idCurrentMusic = nextId;
    }

    /**
     * Called every frame to handle volume fading and crossfade effects.
     * Should be called inside the render or update loop.
     */
    public void update() {
        // Handle fading
        if (fading && currentMusic != null) {
            float currentVol = currentMusic.getVolume();

            if (Math.abs(currentVol - targetVolume) < FADE_STEP) {
                currentMusic.setVolume(targetVolume);
                currentMusicVolume = targetVolume;
                fading = false;
            } else if (currentVol < targetVolume) {
                float newVolume = currentVol + FADE_STEP;
                currentMusic.setVolume(newVolume);
                currentMusicVolume = newVolume;
            } else {
                float newVolume = currentVol - FADE_STEP;
                currentMusic.setVolume(newVolume);
                currentMusicVolume = newVolume;
            }
        }

        // Handle crossfading
        if (crossFading && fadeOutMusic != null) {
            float currentFadeOutVol = fadeOutMusic.getVolume();
            float currentFadeInVol = currentMusic.getVolume();

            if (currentFadeOutVol > FADE_STEP) {
                fadeOutMusic.setVolume(currentFadeOutVol - FADE_STEP);
            } else {
                fadeOutMusic.stop();
                fadeOutMusic = null;
                crossFading = false;
            }

            if (currentFadeInVol < currentMusicVolume - FADE_STEP) {
                currentMusic.setVolume(currentFadeInVol + FADE_STEP);
            } else {
                currentMusic.setVolume(currentMusicVolume);
            }
        }
    }

    /**
     * Sets the current music volume instantly.
     * @param value New volume (between 0 and 1).
     * @return The clamped volume value that was set.
     */
    public float setMusicVolume(float value) {
        float volume = Math.clamp(value, 0f, 1f);
        if (currentMusic != null) {
            currentMusicVolume = volume;
            currentMusic.setVolume(currentMusicVolume);
        }
        return volume;
    }

    /**
     * Returns the current volume of the music.
     * @return Current volume level.
     */
    public float getVolume() {
        return currentMusicVolume;
    }

    /**
     * Checks if the music is currently paused.
     * @return True if paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Stops the currently playing music and clears references.
     */
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
            idCurrentMusic = null;
        }
    }

    /**
     * Disposes a specific music track and removes it from the map.
     * @param id Identifier of the music to remove.
     */
    public void disposeMusic(String id) {
        if (!musics.containsKey(id)) {
            Gdx.app.error("MusicController", "Music with Id " + id + " not found!");
        } else {
            musics.get(id).dispose();
            musics.remove(id);
        }
    }
}
