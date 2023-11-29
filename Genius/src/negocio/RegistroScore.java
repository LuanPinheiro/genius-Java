package negocio;

import java.io.Serializable;

public class RegistroScore implements Serializable{
	private String nomeJogador; // Nome do jogador desse registro
	private String nickJogador;
	private int pontos; // Quantos pontos o jogador fez
	private String status; // Indica se o jogador venceu a partida registrada
	private long jogadaMaisRapida;
	private long totalJogadas;
	
	public RegistroScore(String nome, String nick, int pontos, String status, long jogadaMaisRapida, long totalJogadas) {
		super();
		this.nomeJogador = nome;
		this.nickJogador = nick;
		this.pontos = pontos;
		this.status = status;
		this.jogadaMaisRapida = jogadaMaisRapida;
		this.totalJogadas = totalJogadas;
	}

	public String getNomeJogador() {
		return this.nomeJogador;
	}

	public int getPontos() {
		return this.pontos;
	}

	public long getjogadaMaisRapida() {
		return this.jogadaMaisRapida;
	}

	public long getTotalJogadas() {
		return this.totalJogadas;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public String getNick() {
		return this.nickJogador;
	}

	@Override
	public String toString() {
		String texto = "";
		
		texto += "\n  Nick do Jogador: " + this.nickJogador;
		texto += "\n  Pontos Totais: " + this.pontos;
		texto += "\n  Status: " + status;
		texto += "\n  Jogada mais Rápida: " + tempoTransform(jogadaMaisRapida);
		texto += "\n  Tempo Total de Jogadas: " + tempoTransform(totalJogadas);
		
		return texto;
	}
	
	public String tempoTransform(long tempo) {
		String texto = "";
		long segundos = tempo / 1000;
		long milis = tempo % 1000;
		
		if(segundos > 0) {
			texto += segundos + "s:" + milis + "ms";
		}
		else {
			texto += tempo + "ms";
		}
		
		return texto;
	}
}
