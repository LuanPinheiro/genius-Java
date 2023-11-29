package ui;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Imagens {
	private Timer timer;
	private ImageIcon imagensDificuldade[];
	private ImageIcon imagensVelocidade[];
	private ImageIcon redLabel[];
	private ImageIcon greenLabel[];
	private ImageIcon yellowLabel[];
	private ImageIcon blueLabel[];

	public Imagens() {
		this.timer = new Timer();
		this.imagensDificuldade = new ImageIcon[4];
		this.imagensVelocidade = new ImageIcon[3];
		this.redLabel = new ImageIcon[2];
		this.greenLabel = new ImageIcon[2];
		this.yellowLabel = new ImageIcon[2];
		this.blueLabel = new ImageIcon[2];
		
		// Setando as imagens nos respectivos vetores
		try {
			this.imagensDificuldade[0] = new ImageIcon(ImageIO.read(new File("Botoes/dificuldade_facil.png")));
			this.imagensDificuldade[1] = new ImageIcon(ImageIO.read(new File("Botoes/dificuldade_medio.png")));
			this.imagensDificuldade[2] = new ImageIcon(ImageIO.read(new File("Botoes/dificuldade_dificil.png")));
			this.imagensDificuldade[3] = new ImageIcon(ImageIO.read(new File("Botoes/dificuldade_pesadelo.png")));
			this.imagensVelocidade[0] = new ImageIcon(ImageIO.read(new File("Botoes/velocidade_0.png")));
			this.imagensVelocidade[1] = new ImageIcon(ImageIO.read(new File("Botoes/velocidade_1.png")));
			this.imagensVelocidade[2] = new ImageIcon(ImageIO.read(new File("Botoes/velocidade_2.png")));
			this.redLabel[0] = new ImageIcon(ImageIO.read(new File("Cores/redColor.png")));
			this.redLabel[1] = new ImageIcon(ImageIO.read(new File("Cores/redColor_blink.png")));
			this.greenLabel[0] = new ImageIcon(ImageIO.read(new File("Cores/greenColor.jpg")));
			this.greenLabel[1] = new ImageIcon(ImageIO.read(new File("Cores/greenColor_blink.png")));
			this.yellowLabel[0] = new ImageIcon(ImageIO.read(new File("Cores/yellowColor.png")));
			this.yellowLabel[1] = new ImageIcon(ImageIO.read(new File("Cores/yellowColor_blink.png")));
			this.blueLabel[0] = new ImageIcon(ImageIO.read(new File("Cores/blueColor.png")));
			this.blueLabel[1] = new ImageIcon(ImageIO.read(new File("Cores/blueColor_blink.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Timer getTimer() {
		return this.timer;
	}

	public ImageIcon[] getImagensDificuldade() {
		return this.imagensDificuldade;
	}

	public ImageIcon[] getImagensVelocidade() {
		return this.imagensVelocidade;
	}

	public ImageIcon[] getRedLabel() {
		return this.redLabel;
	}

	public ImageIcon[] getGreenLabel() {
		return this.greenLabel;
	}

	public ImageIcon[] getYellowLabel() {
		return this.yellowLabel;
	}

	public ImageIcon[] getBlueLabel() {
		return this.blueLabel;
	}

	public void piscaCor(JLabel label, int idLabel, int velocidade) {
		ImageIcon imagens[];
		switch(idLabel) {
			case 0: imagens = this.greenLabel; break;
			case 1: imagens = this.yellowLabel; break;
			case 2: imagens = this.redLabel; break;
			case 3: imagens = this.blueLabel; break;
			default: imagens = null;
		}
		
		switch(velocidade) {
			case 0: velocidade = 1000; break;
			case 1: velocidade = 500; break;
			case 2: velocidade = 250; break;
			default: break;
		}
		
		label.setIcon(imagens[1]);
		this.timer.schedule(
			new java.util.TimerTask() {
	            @Override
	            public void run() {
	                label.setIcon(imagens[0]);
	            }
			},
			velocidade
		);
	}
}
