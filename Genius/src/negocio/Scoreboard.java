package negocio;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Scoreboard implements Serializable{
	private String dificuldade; // Dificuldade em que a partida foi jogada
	private List<RegistroScore> registros; // Registro de cada jogador na partida
	private Date data; // Data em que a partida aconteceu
	private String titulo;
	
	
	public Scoreboard(String dificuldade, String titulo) {
		this.dificuldade = dificuldade;
		this.registros = new ArrayList<RegistroScore>();
		this.data = new Date();
		this.titulo = titulo;
	}

	public String getData() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(this.data);
	}

	public String getDificuldade() {
		return this.dificuldade;
	}

	public List<RegistroScore> getRegistros() {
		return this.registros;
	}
	
	public int getTotalPontos() {
		int total = 0;
		for (RegistroScore registroScore : this.registros) {
			total += registroScore.getPontos();
		}
		return total;
	}
	
	public String getTitulo() {
		return this.titulo;
	}

	public String getRelatorioToString() {
		String texto = "";
		
		texto += "Título: " + this.titulo;
		texto += "\nData: " + this.getData();
		texto += "\nDificuldade: " + this.dificuldade;
		texto += "\nSomatório de Pontos: " + this.getTotalPontos();
		texto += "\n\nJogadores:";
		
		int index = 0;
		for (RegistroScore registro : this.registros) {
			texto += "\nJogador " + registro.getNomeJogador() + ":";
			texto += registro.toString();
		}
		
		return texto;
	}
}
