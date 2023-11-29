package negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Partida implements Serializable{

	private String titulo; // Identificador de partida
	private int dificuldadeAtual; // Indice no array de dificuldade que identifica a dificuldade atual
	private String dificuldades[] = {"Fácil", "Médio", "Difícil", "Pesadelo"}; // Array com dificuldades possíveis
	private List<Jogador> jogadoresCadastrados; // Todos os jogadores que foram cadastrados no início da partida
	private List<Jogador> jogadoresDisponiveis; // Todos os jogadores que foram cadastrados no início da partida
	private int velocidade;
	private Scoreboard relatorio; // Relatório com todas as informações da partida
	private int jogadorAtual;
	private int indiceJogada;

	public Partida() {
		this.titulo = "";
		this.jogadoresCadastrados = new ArrayList<Jogador>();
		this.jogadoresDisponiveis = new ArrayList<Jogador>();
		this.dificuldadeAtual = 0;
		this.relatorio = null;
		this.indiceJogada = 0;
		this.jogadorAtual = 0;
		this.velocidade = 0;
	}

	public List<Jogador> getJogadoresDisponiveis() {
		return jogadoresDisponiveis;
	}

	public void addJogadaEmJogador(int id) {
		Random rand = new Random();
		String dificuldade = this.dificuldades[this.dificuldadeAtual];
		Jogador jogador = this.acharJogadorId(id);
		int pontos = jogador.getPontos();
		int jogada;
		
		if(dificuldade.equalsIgnoreCase("Fácil")) 
		{
			if(pontos % 4 == 0) //APÓS 4 RODADAS, EXISTE A MESMA CHANCE PARA QUALQUER COR SER ADICIONADA PARA O JOGADOR 
			{ 
				jogada = rand.nextInt(4);
			} else {
				jogada = rand.nextInt(2); //LIMITA AS CORES PARA O JOGADOR EM APENAS 2 CORES EM 80% DAS RODADAS QUE ELE FIZER
			}
		}
		
		else if(dificuldade.equalsIgnoreCase("Médio")) 
		{
			if(pontos % 2 == 0) {
				jogada = rand.nextInt(4);
			} else {
				jogada = rand.nextInt(3); //LIMITA EM 3 CORES PARA O JOGADOR
			}
		}
		
		else if(dificuldade.equalsIgnoreCase("Difícil")) 
		{
				jogada = rand.nextInt(4); //TOTALMENTE ALEATÓRIO. É POSSÍVEL CONTROLAR SE VAI TER JOGADAS REPETIDAS OU NÃO.
		}
		
		else //MODO PESADELO 
		{ 
				jogada = rand.nextInt(4); //TOTALMENTE ALEATÓRIO
		}
		
		jogador.addJogada(jogada);
		
		for (int play : jogador.getJogadasAtuais()) {
		}
	}

	public void setIndiceJogada(int indiceJogada) {
		this.indiceJogada = indiceJogada;
	}

	public int getJogadorAtual() {
		return this.jogadorAtual;
	}

	public int getIndiceJogada() {
		return this.indiceJogada;
	}

	public void mudaDificuldadeAtual() {
		this.dificuldadeAtual++;
		if(this.dificuldadeAtual > 3) {
			this.dificuldadeAtual = 0;
		}
	}
	
	public void mudaVelocidade() {
		this.velocidade++;
		if(this.velocidade > 2) {
			this.velocidade = 0;
		}
	}

	public int getDificuldadeAtual() {
		return this.dificuldadeAtual;
	}
	
	public String[] getDificuldades() {
		return this.dificuldades;
	}
	
	public int getVelocidade() {
		return this.velocidade;
	}

	public List<Jogador> getJogadoresCadastrados() {
		return jogadoresCadastrados;
	}

	public Scoreboard getRelatorio() {
		return relatorio;
	}

	public void addJogadorCadastrado(Jogador jogador) {
		this.jogadoresCadastrados.add(jogador);
		this.addJogadorDisponivel(jogador);
	}
	
	public void addJogadorDisponivel(Jogador jogador) {
		this.jogadoresDisponiveis.add(jogador);
	}

	public void removeJogadorCadastrado(Jogador jogador) {
		this.jogadoresCadastrados.remove(jogador);
		this.removeJogadorDisponivel(jogador);
	}

	public void removeJogadorDisponivel(Jogador jogador) {
		this.jogadoresDisponiveis.remove(jogador);
	}

	public Jogador acharJogador(String nome) {
		for (Jogador jogador : jogadoresCadastrados) {
			if (jogador.getNome().equalsIgnoreCase(nome)) {
				System.out.println("JOGADOR: " + jogador.getNome());
				return jogador;
			}
		}
		return null;
	}

	public Jogador acharJogadorId(int id) {
		return this.jogadoresDisponiveis.get(id);
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void gerarRelatorio() {
		// Vai gerar o Score da partida
		String dificuldade = this.getDificuldades()[this.getDificuldadeAtual()];
		String titulo = this.getTitulo();
		relatorio = new Scoreboard(dificuldade, titulo);
		int maiorPontuacao = this.jogadoresCadastrados.stream().mapToInt((n)->n.getPontos()).max().getAsInt();
		this.jogadoresCadastrados.stream().forEach((jogador)->{
			if(jogador.getPontos() == maiorPontuacao) {
				jogador.ganharPartida();
			}
		});
		int possiveisEmpatados = (int) this.jogadoresCadastrados.stream().filter((jogador)->jogador.getStatus().equalsIgnoreCase("Vencedor")).count();
		if(possiveisEmpatados > 1) {
			this.jogadoresCadastrados.stream().filter((jogador)->jogador.getStatus().equalsIgnoreCase("Vencedor")).forEach((jogador)->jogador.empatarPartida());;
		}
		
		this.jogadoresCadastrados.sort(Comparator.comparing(Jogador::getPontos).reversed());
		
		for (Jogador jogador : this.jogadoresCadastrados) {
			long menorJogada = jogador.getTempoJogadas().stream().mapToLong((n)->n).min().getAsLong();
			long totalJogadas = jogador.getTempoJogadas().stream().mapToLong((n)->n).sum();
			relatorio.getRegistros().add(new RegistroScore(jogador.getNome(), jogador.getNick(), jogador.getPontos(), jogador.getStatus(), menorJogada, totalJogadas ));
		}
	}

	public boolean verificaJogada(int id, int label) {
		Jogador jogador = this.jogadoresDisponiveis.get(id);
		ArrayList jogadas = (ArrayList) jogador.getJogadasAtuais();
		int ultimaJogada = (int) jogadas.get(this.indiceJogada);
		
		if (ultimaJogada == label) {
			// Jogador acertou a jogada. Segue o jogo
			this.indiceJogada++;
			return true;
		} else {
			// jogador errou a jogada. Sai da partida e verifica se ainda tem jogadores na
			// partida.
			return false;
		}

	}

	public void resetJogadorAtual() {
		this.jogadorAtual = 0;
		this.indiceJogada = 0;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int proxJogadorAtual() {
		this.jogadorAtual++;
		return this.jogadorAtual;
	}

	public void jogadorErrou(Jogador jogador) {
		int fimListaJogadores = this.getJogadoresDisponiveis().size();
		
		if(this.getDificuldadeAtual() != 3) {
			this.removeJogadorDisponivel(jogador);
			if(fimListaJogadores != 1) {
				this.addJogadaEmJogador(0);
				this.setIndiceJogada(0);
			}
		}
		else
		{	
			if (this.getJogadorAtual() == fimListaJogadores - 1) {
				this.resetJogadorAtual();
				if(fimListaJogadores > 1) {
					this.addJogadaEmJogador(0);
				}
			}
			else {
				this.addJogadaEmJogador(this.getJogadorAtual()+1);
				this.setIndiceJogada(0);
			}
			this.removeJogadorDisponivel(jogador);
		}
	}

	public void jogadorAcertou(Jogador jogador) {
		int jogadorAtualId = this.getJogadorAtual();
		int fimListaJogadores = this.getJogadoresDisponiveis().size();
		
		jogador.addPonto();
		if(this.getDificuldadeAtual() != 3) {
			if(this.getIndiceJogada() == jogador.getJogadasAtuais().size()) {
				this.setIndiceJogada(0);
				this.addJogadaEmJogador(this.getJogadorAtual());
			}
		}
		
		else { 
			if(this.getIndiceJogada() == jogador.getJogadasAtuais().size()) {
				if (this.getJogadorAtual() == fimListaJogadores - 1) {
					this.resetJogadorAtual();
					this.addJogadaEmJogador(0);
				} else {
					this.setIndiceJogada(0);
					this.addJogadaEmJogador(this.proxJogadorAtual());
				}
			}
		}
	}
	
	public List<Jogador> empatouPartida(){
		int maiorPontuacao = this.jogadoresCadastrados.stream().mapToInt((jogador)->jogador.getPontos()).max().getAsInt();
		List<Jogador> empatados = new ArrayList<Jogador>();
		
		for (Jogador jogador : this.jogadoresCadastrados) {
			if(jogador.getPontos() == maiorPontuacao) {
				empatados.add(jogador);
			}
		}
		
		if(empatados.size() == 1) {
			empatados.remove(0);
		}
		
		return empatados;
	}
}
