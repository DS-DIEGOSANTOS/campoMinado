package br.com.diego.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	
	private final int LINHA;
	private final int COLUNA;
	
	private boolean aberto = false;
	private boolean marcado = false;
	private boolean minado = false;
	
	private List<Campo> vizinhos = new ArrayList<Campo>();
	private List<CampoObservador> observadores = new ArrayList<>() ;
	
	
	public Campo(int linha, int coluna) {
		LINHA = linha;
		COLUNA = coluna;
	}
	
	public void registrarObservador(CampoObservador observador) {
		this.observadores.add(observador);
	}
	
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	void adicionaVizinhos(Campo vizinho){
		
		boolean verificado = vizinhos.stream().anyMatch(v -> (v.LINHA == vizinho.LINHA) && (v.COLUNA == vizinho.COLUNA));
		
		
		if(!verificado) {
			boolean linhaDiferente = LINHA != vizinho.LINHA;
			boolean colunaDiferente = COLUNA != vizinho.COLUNA;
			boolean Diagonal = linhaDiferente && colunaDiferente;
			
			int deltaLinha = Math.abs(LINHA - vizinho.LINHA);
			int deltaColuna = Math.abs(COLUNA - vizinho.COLUNA);
			int deltaGeral = deltaLinha + deltaColuna;
			
			if(deltaGeral == 1 && !Diagonal) {
				vizinhos.add(vizinho);
			}else if(deltaGeral == 2 && Diagonal) {
				vizinhos.add(vizinho);
			}	
		}
		
	}
	
	public boolean abrir() {
		
		if(!aberto && !marcado) {
			aberto = true;
			
			if(minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			if(verificaVizinhos()) {
			vizinhos.stream().forEach(v -> v.abrir());
			}
			
			return true;
		}else {
			return false;
		}
			
	}
	
	public boolean verificaVizinhos() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	
	public void alternaMarcacao() {
		if(!aberto) {
			marcado = !marcado;
			
			if(marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			}else {
				notificarObservadores(CampoEvento.DESCMARCAR);
			}
		}
	}
	
	
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(aberto) {
			notificarObservadores(CampoEvento.ABRIR);
		}
		
	}

	int getLINHA() {
		return LINHA;
	}

	boolean isMarcado() {
		return marcado;
	}

	int getCOLUNA() {
		return COLUNA;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
	public int qtdeMinas() {
		
		long qtdDeMinas = vizinhos.stream()
						.filter(V -> V.minado)
						.count();
		return (int) qtdDeMinas;
	}
	
	public boolean mina() {
		if(!minado) {
			minado = !minado;
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		
		notificarObservadores(CampoEvento.REINICIAR);
	}


}
