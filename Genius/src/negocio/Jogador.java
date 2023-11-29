package negocio;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Jogador implements Serializable{
	private String nome; // Identificador de jogador
	private String nick;
	private int pontos; // Quantos pontos o jogador está fazendo na partida atual
	private List<Integer> jogadasAtuais; // Sequência de jogadas que o jogador precisa realizar
	private String status; // Confirma vitória do jogador em uma partida
	private List<Long> tempoJogadas;
	
	public Jogador(String nome, String nick) {
		this.nome = nome;
		this.pontos = 0;
		this.jogadasAtuais = new ArrayList<Integer>();
		this.status = "Perdedor";
		this.nick = nick;
		this.tempoJogadas = new ArrayList<Long>();
	}
	
	public String getNome() {
		return this.nome;
	}
	public String getNick() {
		return this.nick;
	}
	public int getPontos() {
		return this.pontos;
	}
	public List<Integer> getJogadasAtuais() {
		return this.jogadasAtuais;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public List<Long> getTempoJogadas() {
		return this.tempoJogadas;
	}

	public void addPonto() {
		this.pontos++;
	}
	
	public void addJogada(int jogada) {
		this.jogadasAtuais.add(jogada);
	}
	
	public void ganharPartida() {
		this.status = "Vencedor";
	}
	
	public void empatarPartida() {
		this.status = "Vencedor (Empatado)";
	}
	
	public String jogadorAtualTxt(int pontoAtual) {
		String texto = "\n Nome: " + this.nome;
		texto += "\n Nick: " + this.nick;
		texto += "\n Pontos Rodada: " + pontoAtual;
		texto += "\n Pontos: " + this.pontos;
		
		return texto;
	}
	
	public void tempoJogada(ZonedDateTime tempoAntes) {
		ZonedDateTime then = ZonedDateTime.now();
		Duration diff = Duration.between(tempoAntes, then);
		this.tempoJogadas.add(diff.toMillis());
	}
}
