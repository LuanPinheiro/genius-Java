package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controle.Genius;
import negocio.Jogador;
import negocio.Partida;
import negocio.RegistroScore;
import negocio.Scoreboard;

public class Janela implements Runnable{

	private JFrame frame;
	private JTextField txtNome;
	private Genius genius = new Genius();
	private boolean clicavel = false;
	private boolean clicavelGenius = true;
	private JTextField txtNick;
	private Imagens imagens;
	private Sons sons;
	private JTextArea txtJogadorAtual;
	private JTextArea txtTodosOsJogadores;
	private JComboBox<String> cbJogadoresCadastrados;
	private String txtJogadorAtualBase;
	private String txtTodosOsJogadoresBase;
	private JComboBox<String> cbScoreboard;
	private JLabel lb0 = new JLabel("LABEL 0");
	private JLabel lb1 = new JLabel("LABEL 1");
	private JLabel lb2 = new JLabel("LABEL 2");
	private JLabel lb3 = new JLabel("LABEL 3");
	private ZonedDateTime now;
	private JLabel lbDificuldade;
	private JLabel lbVelocidade;
	private Thread thread = new Thread(this);
	private JTextField txtTitulo;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Janela window = new Janela();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Janela() {
		txtJogadorAtual = new JTextArea();
		txtTodosOsJogadores = new JTextArea();
		txtJogadorAtualBase = "    JOGADOR ATUAL:";
		txtTodosOsJogadoresBase = "      JOGADORES:";
		cbJogadoresCadastrados = new JComboBox<String>();
		cbScoreboard = new JComboBox<String>();
		lbDificuldade = new JLabel();
		lbVelocidade = new JLabel();
		txtTitulo = new JTextField();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		imagens = new Imagens();
		sons = new Sons();

		frame = new JFrame();
		frame.setBounds(0, 0, 900, 720);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_Cadastro = new JPanel();
		panel_Cadastro.setBackground(Color.WHITE);
		panel_Cadastro.setForeground(Color.WHITE);
		tabbedPane.addTab("Cadastro", null, panel_Cadastro, null);
		panel_Cadastro.setLayout(null);

		JLabel lbNome = new JLabel("Nome:");
		lbNome.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lbNome.setForeground(Color.WHITE);
		lbNome.setBounds(28, 39, 72, 35);
		panel_Cadastro.add(lbNome);

		txtNome = new JTextField();
		txtNome.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtNome.setForeground(Color.WHITE);
		txtNome.setBackground(Color.BLACK);
		txtNome.setHorizontalAlignment(SwingConstants.CENTER);
		txtNome.setBounds(155, 36, 188, 45);
		panel_Cadastro.add(txtNome);
		txtNome.setColumns(10);

		cbJogadoresCadastrados.setModel(new DefaultComboBoxModel(new String[] { " " }));
		cbJogadoresCadastrados.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		cbJogadoresCadastrados.setForeground(Color.WHITE);
		cbJogadoresCadastrados.setBackground(Color.BLACK);
		cbJogadoresCadastrados.setBounds(154, 170, 188, 45);
		panel_Cadastro.add(cbJogadoresCadastrados);

		JButton btApagarJogador = new JButton("Deletar");
		btApagarJogador.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		btApagarJogador.setForeground(Color.WHITE);
		btApagarJogador.setBackground(Color.BLACK);
		btApagarJogador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(clicavelGenius) {
					if (genius.podeApagarJogador()) {
						if(cbJogadoresCadastrados.getSelectedItem().toString().contentEquals(" ")) {
							return;
						}
						genius.apagarJogador(cbJogadoresCadastrados.getSelectedItem().toString());
						JOptionPane.showMessageDialog(btApagarJogador,
								"Jogador " + cbJogadoresCadastrados.getSelectedItem() + " Deletado");
						cbJogadoresCadastrados.removeItem(cbJogadoresCadastrados.getSelectedItem());
					} else {
						JOptionPane.showMessageDialog(btApagarJogador, "Não há jogadores cadastrados", "ERRO", 0);
					}
				}
			}
		});
		btApagarJogador.setBounds(28, 177, 88, 34);
		panel_Cadastro.add(btApagarJogador);

		// Button cadastrar da panel cadastro
		JButton btCadastrarJogador = new JButton("Cadastrar");
		btCadastrarJogador.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btCadastrarJogador.setBackground(Color.BLACK);
		btCadastrarJogador.setForeground(Color.WHITE);
		btCadastrarJogador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(clicavelGenius) {
					String nomeDigitado = txtNome.getText().trim();
					String nickDigitado = txtNick.getText().trim();
					
					if(!nomeDigitado.isEmpty()) {
						int retornoCadastro = genius.cadastrarJogador(nomeDigitado, nickDigitado);
						if(retornoCadastro == 2) {
							JOptionPane.showMessageDialog(btCadastrarJogador, "Limite de Jogadores atingido", "AVISO", 2);
							txtNome.setText(null);
							txtNick.setText(null);
						}
						else if(retornoCadastro == 1) {
							JOptionPane.showMessageDialog(btCadastrarJogador, "Jogador " + nomeDigitado + " já está na partida" , "ERRO", 0);
						}
						else{
							cbJogadoresCadastrados.addItem(nomeDigitado);
							JOptionPane.showMessageDialog(btCadastrarJogador, "Jogador " + nomeDigitado + " Cadastrado");
							txtNome.setText(null);
							txtNick.setText(null);
						}
					}
				}
			}
		});
		btCadastrarJogador.setBounds(577, 52, 188, 65);
		panel_Cadastro.add(btCadastrarJogador);

		txtNick = new JTextField();
		txtNick.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtNick.setBackground(Color.BLACK);
		txtNick.setForeground(Color.WHITE);
		txtNick.setHorizontalAlignment(SwingConstants.CENTER);
		txtNick.setBounds(155, 101, 188, 45);
		panel_Cadastro.add(txtNick);
		txtNick.setColumns(10);

		JLabel lbNick = new JLabel("Nick:");
		lbNick.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lbNick.setForeground(Color.WHITE);
		lbNick.setBounds(28, 105, 72, 32);
		panel_Cadastro.add(lbNick);

		JPanel panel_Jogo = new JPanel();
		tabbedPane.addTab("Genius", null, panel_Jogo, null);
		panel_Jogo.setLayout(null);

		txtTodosOsJogadores.setEditable(false);
		txtTodosOsJogadores.setFont(new Font("Monospaced", Font.BOLD, 13));
		txtTodosOsJogadores.setText("      JOGADORES:");
		txtTodosOsJogadores.setBounds(30, 53, 187, 236);
		panel_Jogo.add(txtTodosOsJogadores);

		txtJogadorAtual.setEditable(false);
		txtJogadorAtual.setFont(new Font("Monospaced", Font.BOLD, 13));
		txtJogadorAtual.setText("    JOGADOR ATUAL:");
		txtJogadorAtual.setBounds(30, 339, 187, 236);
		panel_Jogo.add(txtJogadorAtual);

		lb0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (clicavel) {
					sons.tocaCor(lb0, 0);
					imagens.piscaCor(lb0, 0, genius.getPartidaAtual().getVelocidade());
					boolean acertou = genius.fazerJogada(now, 0);
					now = ZonedDateTime.now();

					if (acertou) {
						if (genius.acertouFimIndice()) {
							thread.start();
						}
						String texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					} else {
						sons.somPerdeu();
						Jogador jogadorAtual = genius.getPartidaAtual().acharJogadorId(genius.getPartidaAtual().getJogadorAtual());
						JOptionPane.showMessageDialog(panel_Jogo, (jogadorAtual.getNick().isEmpty() ? jogadorAtual.getNome() : jogadorAtual.getNick()) + " Perdeu!!" , "ERRO", 0);
						
						if (genius.empatouJogo()) {
							int desempate = JOptionPane.showConfirmDialog(panel_Jogo, genius.nomeJogadoresEmpatados() + "Querem jogar o desempate?", "DESEMPATE", 0);
							
							if(desempate == 0) {
								genius.desempate();
								String texto = txtJogadorAtualBase + genius.getPartidaAtual().acharJogadorId(0).jogadorAtualTxt(genius.getPartidaAtual().getIndiceJogada());
								txtJogadorAtual.setText(texto);
							}
							else {
								sons.somFimDeJogo();
								JOptionPane.showMessageDialog(panel_Jogo, "Jogo encerrado");
								resetInterfaces();
								return;
							}
						}
						else if (genius.getPartidaAtual().getJogadoresDisponiveis().size() == 0){
							sons.somFimDeJogo();
							JOptionPane.showMessageDialog(panel_Jogo, "Jogo encerrado");
							resetInterfaces();
							return;
						}
						thread.start();
						
						String texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					}
				}
			}
		});
		lb0.setBounds(251, 59, 230, 230);
		panel_Jogo.add(lb0);

		lb1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (clicavel) {
					sons.tocaCor(lb1, 1);
					imagens.piscaCor(lb1, 1, genius.getPartidaAtual().getVelocidade());
					boolean acertou = genius.fazerJogada(now, 1);
					now = ZonedDateTime.now();

					if (acertou) {
						if (genius.acertouFimIndice()) {
							thread.start();
						}
						String texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					} else {
						sons.somPerdeu();
						Jogador jogadorAtual = genius.getPartidaAtual().acharJogadorId(genius.getPartidaAtual().getJogadorAtual());
						JOptionPane.showMessageDialog(panel_Jogo, (jogadorAtual.getNick().isEmpty() ? jogadorAtual.getNome() : jogadorAtual.getNick()) + " Perdeu!!" , "ERRO", 0);
						
						if (genius.empatouJogo()) {
							int desempate = JOptionPane.showConfirmDialog(panel_Jogo, genius.nomeJogadoresEmpatados() + "Querem jogar o desempate?", "DESEMPATE", 0);
							
							if(desempate == 0) {
								genius.desempate();
								String texto = txtJogadorAtualBase + genius.getPartidaAtual().acharJogadorId(0).jogadorAtualTxt(genius.getPartidaAtual().getIndiceJogada());
								txtJogadorAtual.setText(texto);
							}
							else {
								sons.somFimDeJogo();
								JOptionPane.showMessageDialog(panel_Jogo, "Jogo encerrado");
								resetInterfaces();
								return;
							}
						}
						else if (genius.getPartidaAtual().getJogadoresDisponiveis().size() == 0){
							sons.somFimDeJogo();
							JOptionPane.showMessageDialog(panel_Jogo, "Jogo encerrado");
							resetInterfaces();
							return;
						}
						thread.start();
						
						String texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					}
				}
			}
		});
		lb1.setBounds(580, 60, 230, 230);
		panel_Jogo.add(lb1);

		lb2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (clicavel) {
					sons.tocaCor(lb2, 2);
					imagens.piscaCor(lb2, 2, genius.getPartidaAtual().getVelocidade());
					boolean acertou = genius.fazerJogada(now, 2);
					now = ZonedDateTime.now();

					if (acertou) {
						if (genius.acertouFimIndice()) {
							thread.start();
						}
						String texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					} else {
						sons.somPerdeu();
						Jogador jogadorAtual = genius.getPartidaAtual().acharJogadorId(genius.getPartidaAtual().getJogadorAtual());
						JOptionPane.showMessageDialog(panel_Jogo, (jogadorAtual.getNick().isEmpty() ? jogadorAtual.getNome() : jogadorAtual.getNick()) + " Perdeu!!" , "ERRO", 0);
						
						if (genius.empatouJogo()) {
							int desempate = JOptionPane.showConfirmDialog(panel_Jogo, genius.nomeJogadoresEmpatados() + "Querem jogar o desempate?", "DESEMPATE", 0);
							
							if(desempate == 0) {
								genius.desempate();
								String texto = txtJogadorAtualBase + genius.getPartidaAtual().acharJogadorId(0).jogadorAtualTxt(genius.getPartidaAtual().getIndiceJogada());
								txtJogadorAtual.setText(texto);
							}
							else {
								sons.somFimDeJogo();
								JOptionPane.showMessageDialog(panel_Jogo, "Jogo encerrado");
								resetInterfaces();
								return;
							}
						}
						else if (genius.getPartidaAtual().getJogadoresDisponiveis().size() == 0){
							sons.somFimDeJogo();
							JOptionPane.showMessageDialog(panel_Jogo, "Jogo encerrado");
							resetInterfaces();
							return;
						}
						thread.start();
						
						String texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					}
				}
			}
		});
		lb2.setBounds(251, 309, 230, 230);
		panel_Jogo.add(lb2);

		lb3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (clicavel) {
					sons.tocaCor(lb3, 3);
					imagens.piscaCor(lb3, 3, genius.getPartidaAtual().getVelocidade());
					boolean acertou = genius.fazerJogada(now, 3);
					now = ZonedDateTime.now();

					if (acertou) {
						if (genius.acertouFimIndice()) {
							thread.start();
						}
						String texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					} else {
						sons.somPerdeu();
						Jogador jogadorAtual = genius.getPartidaAtual().acharJogadorId(genius.getPartidaAtual().getJogadorAtual());
						JOptionPane.showMessageDialog(panel_Jogo, (jogadorAtual.getNick().isEmpty() ? jogadorAtual.getNome() : jogadorAtual.getNick()) + " Perdeu!!" , "ERRO", 0);
						
						if (genius.empatouJogo()) {
							int desempate = JOptionPane.showConfirmDialog(panel_Jogo, genius.nomeJogadoresEmpatados() + "Querem jogar o desempate?", "DESEMPATE", 0);
							
							if(desempate == 0) {
								genius.desempate();
								String texto = txtJogadorAtualBase + genius.getPartidaAtual().acharJogadorId(0).jogadorAtualTxt(genius.getPartidaAtual().getIndiceJogada());
								txtJogadorAtual.setText(texto);
							}
							else {
								sons.somFimDeJogo();
								JOptionPane.showMessageDialog(panel_Jogo, "Jogo encerrado");
								resetInterfaces();
								return;
							}
						}
						else if (genius.getPartidaAtual().getJogadoresDisponiveis().size() == 0){
							sons.somFimDeJogo();
							JOptionPane.showMessageDialog(panel_Jogo, "Jogo encerrado");
							resetInterfaces();
							return;
						}
						thread.start();
						
						String texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					}
				}
			}
		});
		lb3.setBounds(580, 309, 230, 230);
		panel_Jogo.add(lb3);

		JLabel lbIniciar = new JLabel("");
		lbIniciar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lbIniciar.setForeground(new Color(255, 255, 255));
		lbIniciar.setHorizontalAlignment(SwingConstants.CENTER);
		lbIniciar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(clicavelGenius) {
					String texto = txtTodosOsJogadores.getText();
					int jogadores = genius.getPartidaAtual().getJogadoresCadastrados().size();
					
					if (jogadores == 0) {
						JOptionPane.showMessageDialog(lbIniciar, "Cadastre pelo menos 1 jogador para iniciar", "AVISO", 2);
						return;
					} else if(txtTitulo.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(lbIniciar, "Por favor, indique o título do campeonato", "AVISO", 2);
						return;
					}
					else {
						Scoreboard partida = genius.encontrarPartidaTitulo(txtTitulo.getText());
						if(partida != null) {
							JOptionPane.showMessageDialog(lbIniciar, "Partida com esse título já existe", "ERRO", 0);
							return;
						}
						
						clicavelGenius = false;
						genius.iniciarJogo(txtTitulo.getText());
						
						texto = txtTodosOsJogadoresBase + genius.getTextoTodosJogadores();
						txtTodosOsJogadores.setText(texto);
						
						sons.somIniciar();
						thread.start();

						texto = txtJogadorAtualBase + genius.getTextoJogadorAtual();
						txtJogadorAtual.setText(texto);
					}
				}
			}
		});
		lbIniciar.setBounds(493, 280, 75, 35);
		panel_Jogo.add(lbIniciar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(352, 401, 442, 241);
		panel_Cadastro.add(scrollPane);
		JTextArea txtScoreboard = new JTextArea();

		lbDificuldade.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (clicavelGenius) {
					Partida partidaAtual = genius.getPartidaAtual();
					partidaAtual.mudaDificuldadeAtual();
					ImageIcon imagem = imagens.getImagensDificuldade()[partidaAtual.getDificuldadeAtual()];
					lbDificuldade.setIcon(imagem);
				} else {
					// TO DO: Lançar som quando clicar estiver indisponível
				}
			}
		});
		lbDificuldade.setForeground(new Color(255, 255, 255));
		lbDificuldade.setBounds(493, 350, 75, 125);
		panel_Jogo.add(lbDificuldade);

		lbVelocidade.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (clicavelGenius) {
					Partida partidaAtual = genius.getPartidaAtual();
					partidaAtual.mudaVelocidade();
					ImageIcon imagem = imagens.getImagensVelocidade()[partidaAtual.getVelocidade()];
					lbVelocidade.setIcon(imagem);
				} else {
					// TO DO: Lançar som quando clicar estiver indisponível
				}
			}
		});
		lbVelocidade.setForeground(new Color(255, 255, 255));
		lbVelocidade.setBounds(493, 100, 75, 125);
		panel_Jogo.add(lbVelocidade);

		cbScoreboard.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		cbScoreboard.setForeground(Color.WHITE);
		cbScoreboard.setBackground(Color.BLACK);
		cbScoreboard.setBounds(640, 348, 129, 35);
		cbScoreboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String titulo = cbScoreboard.getSelectedItem().toString();
				Scoreboard partida = genius.encontrarPartidaTitulo(titulo);

				txtScoreboard.setText(partida.getRelatorioToString());
			}
		});
		panel_Cadastro.add(cbScoreboard);

		JLabel lbScoreboard = new JLabel("Scoreboard:");
		lbScoreboard.setForeground(Color.WHITE);
		lbScoreboard.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lbScoreboard.setBounds(474, 347, 156, 32);
		panel_Cadastro.add(lbScoreboard);
		
		JLabel lbTitulo = new JLabel("Título: ");
		lbTitulo.setForeground(Color.WHITE);
		lbTitulo.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lbTitulo.setBounds(28, 304, 72, 32);
		panel_Cadastro.add(lbTitulo);
		
		txtTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		txtTitulo.setForeground(Color.WHITE);
		txtTitulo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtTitulo.setColumns(10);
		txtTitulo.setBackground(Color.BLACK);
		txtTitulo.setBounds(155, 301, 188, 45);
		panel_Cadastro.add(txtTitulo);
		
		JButton btSalvarJogo = new JButton("Salvar Jogo");
		btSalvarJogo.setForeground(Color.WHITE);
		btSalvarJogo.setBackground(Color.BLACK);
		btSalvarJogo.setBounds(291, 576, 142, 23);
		btSalvarJogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				genius.salvarJogo();
			}
		});
		panel_Jogo.add(btSalvarJogo);
		
		JButton btCarregarJogo = new JButton("Carregar Jogo");
		btCarregarJogo.setForeground(Color.WHITE);
		btCarregarJogo.setBackground(Color.BLACK);
		btCarregarJogo.setBounds(620, 576, 142, 23);
		btCarregarJogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(clicavelGenius) {
					Genius antigo = genius;
					genius = genius.carregarJogo();
					
					// Mudando as imagens de dificuldade e velocidade
					ImageIcon imagem = imagens.getImagensVelocidade()[genius.getPartidaAtual().getVelocidade()];
					lbVelocidade.setIcon(imagem);
					imagem = imagens.getImagensDificuldade()[genius.getPartidaAtual().getDificuldadeAtual()];
					lbDificuldade.setIcon(imagem);
					
					if(antigo == genius) {
						return;
					}
					
					cbScoreboard.removeAllItems();
					txtScoreboard.setText("Criado por:\r\nCamila\r\nLuan\r\nYuri");
					for (Scoreboard partida : genius.getHistorico()) {
						cbScoreboard.addItem(partida.getTitulo());
					}
					
					Partida partidaAtual = genius.getPartidaAtual();
					JOptionPane.showMessageDialog(btCarregarJogo, "Jogo Carregado");
					
					if(partidaAtual.getJogadoresCadastrados().size() > 0) {
						cbJogadoresCadastrados.removeAllItems();
						cbJogadoresCadastrados.addItem(" ");
						for (Jogador jogador: partidaAtual.getJogadoresCadastrados()) {
							cbJogadoresCadastrados.addItem(jogador.getNome());
						}
					}
					
					if(partidaAtual.getJogadoresDisponiveis().size() > 0) {
						if(partidaAtual.getJogadoresDisponiveis().get(0).getJogadasAtuais().size() > 0)
						{
							clicavelGenius = false;
							// Adicionando todos os jogadores na txtArea de cima
							String texto = txtTodosOsJogadores.getText();
							for (Jogador jogador : partidaAtual.getJogadoresCadastrados()) {
								texto += "\n " + jogador.getNome();
							}
							txtTodosOsJogadores.setText(texto);
							
							// Adicionando o jogador atual na txtArea de baixo
							int jogadorAtualId = partidaAtual.getJogadorAtual();
							Jogador jogadorAtual = partidaAtual.acharJogadorId(jogadorAtualId);
							
							texto = txtJogadorAtualBase
									+ jogadorAtual.jogadorAtualTxt(partidaAtual.getIndiceJogada());
							txtJogadorAtual.setText(texto);
							
							thread.start();
						}
					}
					else {
						clicavelGenius = true;
						clicavel = false;
					}
				}
			}
		});
		panel_Jogo.add(btCarregarJogo);
		
		JLabel lbBackgroundJogo = new JLabel("");
		lbBackgroundJogo.setBounds(0, 0, 879, 656);
		panel_Jogo.add(lbBackgroundJogo);
		
		JLabel lbBackground = new JLabel("");
		lbBackground.setBounds(0, 0, 879, 656);
		panel_Cadastro.add(lbBackground);
		
		try {
			// Setando as labels em suas imagens iniciais
			lb0.setIcon(imagens.getGreenLabel()[0]);
			lb1.setIcon(imagens.getYellowLabel()[0]);
			lb2.setIcon(imagens.getRedLabel()[0]);
			lb3.setIcon(imagens.getBlueLabel()[0]);
			lbDificuldade.setIcon(imagens.getImagensDificuldade()[0]);
			lbVelocidade.setIcon(imagens.getImagensVelocidade()[0]);
			lbIniciar.setIcon(new ImageIcon(ImageIO.read(new File("Botoes/iniciar.png"))));

			lbBackground.setIcon(new ImageIcon(ImageIO.read(new File("background_cadastro.png"))));
			lbBackgroundJogo.setIcon(new ImageIcon(ImageIO.read(new File("Cores/blackColor.jpg"))));
			
			scrollPane.setViewportView(txtScoreboard);
			txtScoreboard.setEditable(false);
			txtScoreboard.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
			txtScoreboard.setForeground(Color.WHITE);
			txtScoreboard.setText("Criado por:\r\nCamila\r\nLuan\r\nYuri");
			txtScoreboard.setBackground(Color.BLACK);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void resetInterfaces() {
		Partida partidaAtual = genius.getPartidaAtual();
		clicavel = false;
		clicavelGenius = true;
		partidaAtual.gerarRelatorio();
		String texto = "";
		
		for (RegistroScore jogador : partidaAtual.getRelatorio().getRegistros()) {
			if(jogador.getStatus().contains("Vencedor")) {
				texto += (jogador.getNick().isEmpty() ? jogador.getNomeJogador() : jogador.getNick()) + "\n";
			}
		}
		JOptionPane.showMessageDialog(cbJogadoresCadastrados, "Vencedores:\n" + texto);
		
		cbJogadoresCadastrados.removeAllItems();
		cbJogadoresCadastrados.addItem("\n");
		txtJogadorAtual.setText("    JOGADOR ATUAL:");
		txtTodosOsJogadores.setText("      JOGADORES:");
		lbDificuldade.setIcon(imagens.getImagensDificuldade()[0]);
		lbVelocidade.setIcon(imagens.getImagensVelocidade()[0]);
		genius.novaPartida();
		cbScoreboard.addItem(partidaAtual.getRelatorio().getTitulo());
	}

	@Override
	public void run() {
		Partida partidaAtual = genius.getPartidaAtual();
		Jogador jogador = partidaAtual.acharJogadorId(partidaAtual.getJogadorAtual());
		Timer timer = new Timer();
		clicavel = false;
		try {
			this.thread.join(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int velocidadeEntrePiscar = 0; // Apenas iniciando a variavel
		
		switch(partidaAtual.getVelocidade()) {
			case 0: velocidadeEntrePiscar = 1250; break;
			case 1: velocidadeEntrePiscar = 750; break;
			case 2: velocidadeEntrePiscar = 500; break;
		}
		
		sons.somMostrarSequencia();
		
		try {
			this.thread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < jogador.getJogadasAtuais().size(); i++) {
			switch (jogador.getJogadasAtuais().get(i)) {
			case 0:
				sons.tocaCor(lb0, 0);
				imagens.piscaCor(lb0, 0, partidaAtual.getVelocidade());
				break;
			case 1:
				sons.tocaCor(lb1, 1);
				imagens.piscaCor(lb1, 1, partidaAtual.getVelocidade());
				break;
			case 2:
				sons.tocaCor(lb2, 2);
				imagens.piscaCor(lb2, 2, partidaAtual.getVelocidade());
				break;
			case 3:
				sons.tocaCor(lb3, 3);
				imagens.piscaCor(lb3, 3, partidaAtual.getVelocidade());
				break;
			}
			try {
				this.thread.join(velocidadeEntrePiscar);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		clicavel = true;
		now = ZonedDateTime.now();
		this.thread = new Thread(this);
	}
	
	
}
