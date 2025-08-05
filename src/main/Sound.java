package main;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class Sound {

    private Clip musicClip;

    public void playSoundEffect(String soundName) {
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

    public void playMusic(String soundName) {
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
}
