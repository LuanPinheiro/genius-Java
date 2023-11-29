package controle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import negocio.Jogador;
import negocio.Partida;
import negocio.Scoreboard;

public class Genius implements Serializable{
	private List<Scoreboard> historico; // Histórico com todas as partidas jogadas
	private Partida partidaAtual; // Partida que está acontecendo atualmente
	
	public Genius() {
		this.historico = new ArrayList<Scoreboard>();
		this.partidaAtual = new Partida();
	}
	
	public List<Scoreboard> getHistorico() {
		return this.historico;
	}
	
	public Partida getPartidaAtual() {
		return this.partidaAtual;
	}
	
	// Encontra o histórico de uma partida dado um título
	public Scoreboard encontrarPartidaTitulo(String titulo) {
		for (Scoreboard partida : this.historico) {
			if(partida.getTitulo().equalsIgnoreCase(titulo)) {
				return partida;
			}
		}
		return null;
	}
	
	// Salva uma partida no historico e instancia uma nova partida
	public void novaPartida() {
		this.historico.add(this.partidaAtual.getRelatorio());
		
		this.partidaAtual = new Partida();
	}
	
	public void salvarJogo() {
		FileOutputStream fos = null;
	    ObjectOutputStream out = null;
	    JFileChooser jfc = new JFileChooser();
		jfc.showSaveDialog(jfc);
		
	    try {
	    	if(jfc.getSelectedFile() != null) {
	    		fos = new FileOutputStream(jfc.getSelectedFile(),false);
		        out = new ObjectOutputStream(fos);
		        out.writeObject(this);
		        out.close();
	    	}
	    	else {
	    		return;
	    	}
	    } catch (IOException ex) {
	    	JOptionPane.showMessageDialog(jfc, "Erro ao Salvar Arquivo", "ERRO", 2);
	    	return;
	    }
	    JOptionPane.showMessageDialog(jfc, "Jogo Salvo!");
	}
	
	public Genius carregarJogo() {
		Genius genius = this;
		FileInputStream fis;
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(jfc); 
		File arquivo= jfc.getSelectedFile();
		
        try {
        	if(arquivo != null) {
        		fis = new FileInputStream(arquivo.getAbsolutePath());
    			ObjectInputStream oos = new ObjectInputStream(fis);
    	        genius = (Genius) oos.readObject();
                fis.close();
        	}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(jfc, "Erro ao carregar arquivo", "ERRO", 0);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(jfc, "Tipo de arquivo incorreto", "ERRO", 0);
		}
        
        return genius;
	}
	
	public boolean podeApagarJogador() {
		int jogadoresCadastrados = this.partidaAtual.getJogadoresCadastrados().size();
		
		if (jogadoresCadastrados != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void apagarJogador(String nomeJogador) {
		Jogador jogador = this.partidaAtual.acharJogador(nomeJogador);
		this.partidaAtual.removeJogadorCadastrado(jogador);
	}
	
	public int cadastrarJogador(String nomeDigitado, String nickDigitado) {
		if(this.partidaAtual.getJogadoresCadastrados().size() != 10) {
			if(this.partidaAtual.acharJogador(nomeDigitado) == null) {
				Jogador jogador = new Jogador(nomeDigitado, nickDigitado);
				this.partidaAtual.addJogadorCadastrado(jogador);
				return 0;
			}
			return 1;
		}
		return 2;
	}
	
	public boolean fazerJogada(ZonedDateTime tempoAntes, int labelId) {
		int jogadorAtualId = this.partidaAtual.getJogadorAtual();
		Jogador jogadorAtual = this.partidaAtual.acharJogadorId(jogadorAtualId);
		boolean acertou = this.partidaAtual.verificaJogada(jogadorAtualId, labelId);
		jogadorAtual.tempoJogada(tempoAntes);
		
		return acertou;
	}
	
	public boolean acertouFimIndice() {
		Jogador jogadorAtual = this.partidaAtual.acharJogadorId(this.partidaAtual.getJogadorAtual());
		this.partidaAtual.jogadorAcertou(jogadorAtual);

		if (this.partidaAtual.getIndiceJogada() == 0) {
			return true;
		}
		return false;
	}
	
	public boolean empatouJogo() {
		Jogador jogadorAtual = this.partidaAtual.acharJogadorId(this.partidaAtual.getJogadorAtual());
		this.partidaAtual.jogadorErrou(jogadorAtual);

		if (this.partidaAtual.getJogadoresDisponiveis().size() == 0) {
			List<Jogador> empatados = this.partidaAtual.empatouPartida();
			
			if(empatados.size() != 0) {
				return true;
			}
		}
		return false;
	}
	
	public String getTextoJogadorAtual() {
		Jogador jogadorAtual = this.partidaAtual.acharJogadorId(this.partidaAtual.getJogadorAtual());
		return jogadorAtual.jogadorAtualTxt(this.partidaAtual.getIndiceJogada());
	}
	
	public String nomeJogadoresEmpatados() {
		List<Jogador> empatados = this.partidaAtual.empatouPartida();
		
		String jogadores = "";
		for (Jogador jogador : empatados) {
			this.partidaAtual.addJogadorDisponivel(jogador);
			jogadores += jogador.getNome() + "\n";
		}
		return jogadores;
	}
	
	public void desempate() {
		this.partidaAtual.addJogadaEmJogador(0);
		this.partidaAtual.setIndiceJogada(0);
	}
	
	public void iniciarJogo(String titulo) {
		this.partidaAtual.setTitulo(titulo);
		this.partidaAtual.addJogadaEmJogador(0);
	}
	
	public String getTextoTodosJogadores() {
		String texto = "";
		for (Jogador jogador : this.partidaAtual.getJogadoresCadastrados()) {
			texto += "\n " + jogador.getNome();
		}
		
		return texto;
	}
}