package usjt.esc.escalonador;

import java.util.ArrayList;
import java.util.List;

import usjt.esc.memoria.Memoria;
import usjt.esc.memoria.Particao;

public class Processador {

	private static Integer TAMANHO_FIXO_MEMORIA = 100;
	private static Integer QUANTUM = 5;
	private static Integer QTDE_NIVEIS = 3;

	protected static void processaMultinivel(Memoria memoria) {
		// esse metodo tem a responsabilidade de recuperar todos os processos da memoria
		// e emular o escalonador

		List<Processo> procs = new ArrayList<>();
		for (Particao p : memoria.getParticoes()) { // recupera os processos das particoes para serem processados
			if (!p.getProcessos().isEmpty()) {
				procs.addAll(p.getProcessos());
			}
		}

		Processo processoAtivo = procs.get(0);
		Integer validaQuantum = 0;
		Integer fimNivel = 0;
		for (Processo p : procs) {
			
			if((p.getDuracaoUU() - QUANTUM) < 0) {				
				fimNivel += QUANTUM;
			}else {
				fimNivel += p.getDuracaoUU();
			}
			if (p.getTempoChegada() < processoAtivo.getTempoChegada()) {
				processoAtivo = p;
			}
		}

		System.out.println("\n Iniciando o processamento da memoria ... \n");
		for (int j = 1; j < QTDE_NIVEIS; j++) {
			System.out.println("\n---------- NIVEL " + j + "----------");

			for (int i = 1; i <= TAMANHO_FIXO_MEMORIA; i++) {
				if (i != fimNivel) {
					System.out.println("uu[" + i + "]- ");

					if (validaQuantum != QUANTUM) {
						++validaQuantum;
						for (Processo p : procs) {
							if (p.getTempoChegada().equals(i)) {
								processoAtivo = p;
								if (processoAtivo == p) {
									System.out.println(" Processo " + p.getNome() + " iniciado!");
								}

							}
						}

					} else {
						System.out.println(" -Quantum encerrado ");
						validaQuantum = 0;
					}
				} else {
					break;
				}
			}
		}

		// TODO montar o ROUDING ROBING
	}

}
