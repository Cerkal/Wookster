package main;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class Sound {

    public boolean mute = false;
    public boolean muteMusic = false;
    private Clip musicClip;
    private FloatControl musicVolumeControl;
    private float originalMusicVolume;

    public void playSoundEffect(String soundName) {
        if (this.mute) { return; }
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
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void playSoundEffectDamp(String soundName) {
        if (this.mute) { return; }
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

                lowerMusicVolumeTemporarily(-10.0f);

                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        restoreMusicVolume();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void toggleMusic() {
        this.muteMusic = !this.muteMusic;
        if (this.muteMusic) {
            stopMusic();
        }
    }

    public void toogleEffects() {
        this.mute = true;
    }

    public void playMusic(String soundName) {
        if (this.muteMusic) { return; }
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

    private void lowerMusicVolumeTemporarily(float changeDb) {
        if (musicVolumeControl != null) {
            musicVolumeControl.setValue(originalMusicVolume + changeDb);
        }
    }

    private void restoreMusicVolume() {
        if (musicVolumeControl != null) {
            musicVolumeControl.setValue(originalMusicVolume);
        }
    }
}
