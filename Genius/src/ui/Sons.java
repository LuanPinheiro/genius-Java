package ui;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;

public class Sons {
	private Timer timer;

    public void somPerdeu() {
        String audio = "fail.wav";
        play(audio);
    }
    
    public void somFimDeJogo() {
    	String audio = "gameover.wav";
    	play(audio);
    }
    
    public void somIniciar() {
    	String audio = "iniciar.wav";
    	play(audio);
    }
    
    public void somMostrarSequencia() {
    	String audio = "mostrarSequencia.wav";
    	play(audio);
    }
    
    public void tocaCor(JLabel label, int idLabel) {
    	this.timer = new Timer();
		String sons[] = {"somVerde.wav", "somAmarelo.wav", "somVermelho.wav", "somAzul.wav"};
		
		this.timer.schedule(
			new java.util.TimerTask() {
	            @Override
	            public void run() {
	            	play(sons[idLabel]);
	            }
			},
			0
		);
	}

    private void play(String audio) {
        File audioFile = new File("Sounds/" + audio);

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            
            Thread.sleep(clip.getMicrosecondLength() / 500);
            
            clip.close();
            audioStream.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}