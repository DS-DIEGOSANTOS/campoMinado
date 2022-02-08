package br.com.diego.cm.visao;

import javax.swing.JFrame;

import br.com.diego.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends  JFrame{
	
	public TelaPrincipal() {
		Tabuleiro tabuleiro = new Tabuleiro(16,30,50);
		add(new PainelTabuleiro(tabuleiro));
		setSize(690,430);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("campo Minado");
		setVisible(true);
		
	}
	

	public static void main(String[] args) {
		new TelaPrincipal();
	}
}
