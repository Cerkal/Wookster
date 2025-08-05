package main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    Clip clip;
    Clip typeClip;
    Clip musicClip;
    List<URL> soundURL = new ArrayList<>();
    HashMap<String, URL> soundMap = new HashMap<>();

    public Sound() {
        loadAllSounds();
    }

    public void setFile(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.soundMap.get(soundName));
            this.clip = AudioSystem.getClip();
            this.clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        this.clip.start();
    }

    public void stop() {
        if (this.clip != null) {
            this.clip.stop();
        }
    }

    public void stopMusic() {
        if (this.musicClip != null) {
            this.musicClip.stop();
        }
    }

    public void playSoundEffect(String soundName) {
        setFile(soundName);
        play();
        this.clip.addLineListener(event -> {
            if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                this.clip.close();
            }
        });
    }

    public void playingTextEffect() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.soundMap.get(Constants.SOUND_TEXT));
            this.typeClip = AudioSystem.getClip();
            this.typeClip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.typeClip.start();
        this.typeClip.addLineListener(event -> {
            if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                this.typeClip.close();
            }
        });
    }

    public void playMusic(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.soundMap.get(file));
            this.musicClip = AudioSystem.getClip();
            this.musicClip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.musicClip.start();
        this.musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void loadAllSounds() {
        try {
            for (String soundFile : Constants.SOUND_LIST) {
                this.soundMap.put(soundFile, new File(soundFile).toURI().toURL());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
