package br.com.diego.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Tabuleiro implements CampoObservador {
	
	private final int LINHAS;	
	private final int  COLUNAS;
	private final int MINAS;
	
	private final List<Campo> campos = new ArrayList<Campo>();
	private final List<Consumer<Boolean>> observadores = new ArrayList<Consumer<Boolean>>();
	
	public Tabuleiro(int linhas, int colunas, int minas) {
	
		LINHAS = linhas;
		COLUNAS = colunas;
		MINAS = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();

	}
	
	public void paraCada(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
	public void registrarObservador( Consumer<Boolean> observador) {
		observadores.add(observador);
	}
	
	public void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(resultado));
	}
	
	public void abrir(int linha , int coluna) {
		
			campos.stream()
			.filter(v -> v.getLINHA() == linha) 
			.filter(v -> v.getCOLUNA() == coluna)
			.findFirst()
			.ifPresent(v -> v.abrir());;
		
			
	}
	
	private void mostrarMinas() {
					campos.stream()
					.filter(c -> c.isMinado())
					.filter(c -> !c.isMarcado())
					.forEach(c -> c.setAberto(true));
	}
	
	public void alteraMarcacao(int linha , int coluna) {
		campos.stream()
		.filter(v -> v.getLINHA() == linha) 
		.filter(v -> v.getCOLUNA() == coluna)
		.findFirst()
		.ifPresent(v -> v.alternaMarcacao());;
	}

	private void gerarCampos() {
		for(int l = 0 ; l<LINHAS;l++) {
			for(int c=0 ; c<COLUNAS; c++) {
				Campo campo = new Campo(l, c);
				campo.registrarObservador(this);
				campos.add(campo);
			}
		}
	}
	
	private void associarVizinhos() {
		for (Campo campoPrincipal : campos) {
			for (Campo campoSegundario : campos) {
				campoPrincipal.adicionaVizinhos(campoSegundario);
			}
		}
	}
	
	private void sortearMinas() {
		long qtd = campos.stream().filter(c-> c.isMinado()).count();
		while(qtd<MINAS) {
			int sorteio = (int) Math.abs(Math.random() * campos.size());
			campos.get(sorteio).mina();
			qtd = campos.stream().filter(c-> c.isMinado()).count();
		}
	}
	
	public boolean objetivoCompleto() {
		return campos.stream().allMatch(v-> v.objetivoAlcancado());
	}

	public void reiniciar() {
		campos.stream().forEach(c-> c.reiniciar());
		sortearMinas();
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			System.out.println("Perdeu");
			mostrarMinas();
			notificarObservadores(false);
		}else if(objetivoCompleto()) {
			System.out.println("Ganhou..!");
			notificarObservadores(true);
		}
	}

	public int getLINHAS() {
		return LINHAS;
	}

	public int getCOLUNAS() {
		return COLUNAS;
	}
	
	
	
}
