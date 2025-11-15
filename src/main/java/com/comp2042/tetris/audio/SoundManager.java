package com.comp2042.tetris.audio;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages game sound effects for audio feedback.
 *
 * <p>I added this to give the game more life. Sound effects make
 * actions feel more impactful - the satisfying thud when a piece
 * locks, the whoosh when lines clear. It's one of those things you
 * don't notice until it's there, but it makes a big difference.</p>
 *
 * <h2>Supported Sounds</h2>
 * <ul>
 *   <li>MOVE - when piece moves left/right</li>
 *   <li>ROTATE - when piece rotates</li>
 *   <li>DROP - when piece locks into place</li>
 *   <li>CLEAR - when lines are cleared</li>
 *   <li>TETRIS - special sound for 4-line clear</li>
 *   <li>GAME_OVER - when game ends</li>
 *   <li>LEVEL_UP - when level increases</li>
 * </ul>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-15
 */
public class SoundManager {

    /* cache loaded sounds so we don't reload them every time
     * this saves memory and makes playback snappier
     */
    private final Map<SoundType, AudioClip> soundCache;

    /* master volume for all sounds (0.0 to 1.0)
     * defaulting to 70% felt like a good balance
     */
    private double masterVolume = 0.7;

    /* whether sounds are enabled
     * some people prefer to play in silence
     */
    private boolean soundEnabled = true;

    public SoundManager() {
        this.soundCache = new HashMap<>();
        loadSounds();
    }

    /**
     * Preloads all sound effects into memory.
     *
     * <p>I'm loading these upfront so there's no delay when playing.
     * Nothing worse than a sound effect that lags behind the action.</p>
     */
    private void loadSounds() {
        for (SoundType type : SoundType.values()) {
            try {
                URL soundUrl = getClass().getClassLoader().getResource("sounds/" + type.getFileName());
                if (soundUrl != null) {
                    AudioClip clip = new AudioClip(soundUrl.toExternalForm());
                    soundCache.put(type, clip);
                }
            } catch (Exception e) {
                /* if a sound fails to load, just skip it
                 * game should still work without sounds
                 */
                System.err.println("Could not load sound: " + type.getFileName());
            }
        }
    }

    /**
     * Plays a sound effect.
     *
     * <p>This is the main method you'll call throughout the game.
     * Just pass in what happened and I'll play the right sound.</p>
     *
     * @param type the type of sound to play
     */
    public void play(SoundType type) {
        if (!soundEnabled) {
            return;
        }

        AudioClip clip = soundCache.get(type);
        if (clip != null) {
            clip.play(masterVolume);
        }
    }

    /**
     * Sets the master volume for all sounds.
     *
     * @param volume value between 0.0 (mute) and 1.0 (full volume)
     */
    public void setMasterVolume(double volume) {
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    /**
     * Gets the current master volume.
     *
     * @return volume level between 0.0 and 1.0
     */
    public double getMasterVolume() {
        return masterVolume;
    }

    /**
     * Enables or disables all sounds.
     *
     * @param enabled true to enable sounds, false to mute
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    /**
     * Checks if sounds are currently enabled.
     *
     * @return true if sounds are enabled
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Toggles sound on/off.
     *
     * @return the new sound enabled state
     */
    public boolean toggleSound() {
        soundEnabled = !soundEnabled;
        return soundEnabled;
    }
}
