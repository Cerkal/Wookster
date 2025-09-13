package main;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public class Sound {

    public boolean muteEffects = false;
    public boolean muteMusic = false;

    private Clip musicClip;
    private FloatControl musicVolumeControl;
    private float originalMusicVolume;

    private String musicFile;

    private int musicVolume = 5;
    private int effectsVolume = 5;

    public void setMusicVolume(int value) {
        this.musicVolume = Math.max(0, Math.min(10, value));
        applyMusicVolume();
    }

    public void setEffectsVolume(int value) {
        this.effectsVolume = Math.max(0, Math.min(10, value));
    }

    public void playSoundEffect(String soundName) {
        if (this.muteEffects || effectsVolume == 0) return;
        if (soundName == null) {
            System.err.println("Sound not found: " + soundName);
            return;
        }
        new Thread(() -> {
            try (
                InputStream raw = getClass().getResourceAsStream(soundName);
                BufferedInputStream bis = new BufferedInputStream(raw);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bis)
            ) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                // Apply effect volume
                setClipVolume(clip, effectsVolume);

                clip.start();
                // clip.addLineListener(event -> {
                //     if (event.getType() == LineEvent.Type.STOP) {
                //         clip.close();
                //     }
                // });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void playMusic(String soundName) {
        if (this.muteMusic) return;
        this.musicFile = soundName;
        stopMusic();
        if (soundName == null) {
            System.err.println("Music not found: " + soundName);
            return;
        }
        try (
            InputStream raw = getClass().getResourceAsStream(soundName);
            BufferedInputStream bis = new BufferedInputStream(raw);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bis)
        ) {
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);

            if (musicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                musicVolumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                originalMusicVolume = musicVolumeControl.getValue();
                applyMusicVolume();
            }

            musicClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
        }
    }

    private void setClipVolume(Clip clip, int sliderValue) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = sliderToDecibels(sliderValue);
            control.setValue(dB);
        }
    }

    private void applyMusicVolume() {
        if (musicVolumeControl != null) {
            musicVolumeControl.setValue(sliderToDecibels(musicVolume));
        }
    }

    private float sliderToDecibels(int sliderValue) {
        if (sliderValue <= 0) {
            return -80.0f;
        }
        // Scale 1–10 -> -30dB to 0dB range
        float fraction = sliderValue / 10.0f;
        return (float) (Math.log10(fraction) * 20.0);
    }

    private void lowerMusicVolumeTemporarily(float changeDb) {
        if (musicVolumeControl != null) {
            musicVolumeControl.setValue(originalMusicVolume + changeDb);
        }
    }

    private void restoreMusicVolume() {
        if (musicVolumeControl != null) {
            applyMusicVolume();
        }
    }

    public float getDecibelVolume(int value) {
        float decibels = (float)(Math.log10(value / 10.0) * 20.0);
        return decibels;
    }

    public void toggleMusic() {
        this.muteMusic = !this.muteMusic;
        if (this.muteMusic) {
            stopMusic();
        } else {
            if (this.musicFile != null) {
                playMusic(this.musicFile);
            }
        }
    }

    public void muteMusic() {
        stopMusic();
        this.muteMusic = true;
    }

    public void toggleEffects() {
        this.muteEffects = !this.muteEffects;
    }

    public void muteEffects() {
        this.muteEffects = true;
    }
}
