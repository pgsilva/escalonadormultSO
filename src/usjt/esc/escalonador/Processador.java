package usjt.esc.escalonador;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import usjt.esc.memoria.Memoria;
import usjt.esc.memoria.Particao;

public class Processador {

	private static Integer TAMANHO_FIXO_MEMORIA = 100;
	private static Integer QUANTUM = 5;
	private Boolean memoriaOcupado = false;
	private Boolean hasEspera = false;
	private Processo processoAtivo;
	private Processo processoEspera;

	public Processador() {
		processoAtivo = new Processo();
		processoEspera = new Processo();
	}

	public Processo getProcessoAtivo() {
		return processoAtivo;
	}

	public void setProcessoAtivo(Processo processoAtivo) {
		this.processoAtivo = processoAtivo;
	}

	public Processo getProcessoEspera() {
		return processoEspera;
	}

	public void setProcessoEspera(Processo processoEspera) {
		this.processoEspera = processoEspera;
	}

	private void atualizaProcessoEspera(Processo p) {
		this.processoEspera.setDuracaoUU(p.getDuracaoUU());
		this.processoEspera.setId(p.getId());
		this.processoEspera.setNome(p.getNome());
		this.processoEspera.setTempoRestante(p.getTempoRestante());
		this.processoEspera.setTempoChegada(p.getTempoChegada());
		this.hasEspera = true;
	}

	private void atualizaProcessoAtivo(Processo p) {
		this.processoAtivo.setDuracaoUU(p.getDuracaoUU());
		this.processoAtivo.setId(p.getId());
		this.processoAtivo.setNome(p.getNome());
		this.processoAtivo.setTempoRestante(p.getTempoRestante());
		this.processoAtivo.setTempoChegada(p.getTempoChegada());
		this.memoriaOcupado = true;
	}

	private void ifEspera() {
		if (Boolean.TRUE.equals(hasEspera)) {
			this.processoAtivo.setDuracaoUU(this.processoEspera.getDuracaoUU());
			this.processoAtivo.setId(this.processoEspera.getId());
			this.processoAtivo.setNome(this.processoEspera.getNome());
			this.processoAtivo.setTempoRestante(this.processoEspera.getTempoRestante());
			this.processoAtivo.setTempoChegada(this.processoEspera.getTempoChegada());
			this.hasEspera = false;
		}
	}

	private void finalizaProcessoAtual() {
		this.processoAtivo.setDuracaoUU(null);
		this.processoAtivo.setId(null);
		this.processoAtivo.setNome(null);
		this.processoAtivo.setTempoRestante(null);
		this.processoAtivo.setTempoChegada(null);
	}

	protected void processaMultinivel(Memoria memoria) {
		// esse metodo tem a responsabilidade de recuperar todos os processos da memoria
		// e emular o escalonador
		List<Processo> procs2 = new ArrayList<>();
		try {
			List<Processo> procs = new ArrayList<>();
			for (Particao p : memoria.getParticoes()) { // recupera os processos das particoes para serem processados
				if (!p.getProcessos().isEmpty()) {
					procs.addAll(p.getProcessos());
				}
			}
			Integer flagFim = procs.size();
			Integer flagverificaFim = 0;

			System.out.println("\n Iniciando o processamento da memoria ... \n");
			System.out.println("\n---------- NIVEL 1 ----------");

			Integer validaQuantum = 0;
			for (int i = 0; i < TAMANHO_FIXO_MEMORIA; i++) {
				if (flagFim == flagverificaFim) {
					throw new ConcurrentModificationException();
				}
				System.out.println("uu[" + i + "]: ");
				if (this.processoAtivo.getId() != null && this.processoAtivo.getTempoChegada() > i) {
					continue;
				}

				for (Processo p : procs) {
					if (p.getTempoChegada() == i && (memoriaOcupado == false)) {
						atualizaProcessoAtivo(p);
					} else if (p.getTempoChegada() != i && (memoriaOcupado == false)) {
						continue;
					} else if ((p.getTempoChegada() == i) && (memoriaOcupado == true)
							&& (this.processoEspera.getId() == null)) {
						atualizaProcessoEspera(p);
					} else {
						continue;
					}
				}

				if ((validaQuantum != QUANTUM) && (this.processoAtivo.getId() != null)) {
					validaQuantum++;
					if (this.processoAtivo.getTempoRestante() >= 0) {
						Integer uu = this.processoAtivo.getTempoRestante() - 1;
						System.out.println(this.processoAtivo);
						this.processoAtivo.setTempoRestante(uu);
						if (this.processoAtivo.getTempoRestante() <= 0) {
							flagverificaFim++;
						}
					} else {
						for (Processo p : procs) {
							if (p.getId() == this.processoAtivo.getId()) {
								procs2.add(new Processo(this.processoAtivo.getId(), this.processoAtivo.getNome(),
										this.processoAtivo.getTempoRestante()));
								++flagverificaFim;
								finalizaProcessoAtual();
							}
						}
					}
				} else {
					validaQuantum = 1;
					if (this.processoAtivo.getId() != null) {
						if (this.processoAtivo.getTempoRestante() >= 0) {
							Integer uu = this.processoAtivo.getTempoRestante() - 1;
							System.out.println(this.processoAtivo + " ----Quantum finalizado!");
							this.processoAtivo.setTempoRestante(uu);
							if (this.processoAtivo.getTempoRestante() <= 0) {
								flagverificaFim++;
							}
							if (this.processoAtivo.getTempoRestante() > 0) {
								for (Processo p : procs) {
									if (p.getId() == this.processoAtivo.getId()) {
										procs2.add(new Processo(this.processoAtivo.getId(),
												this.processoAtivo.getNome(), this.processoAtivo.getTempoRestante()));
										++flagverificaFim;
										finalizaProcessoAtual();
									}
								}
							}
						} else {

							for (Processo p : procs) {
								if (p.getId() == this.processoAtivo.getId()) {
									++flagverificaFim;
									procs2.add(new Processo(this.processoAtivo.getId(), this.processoAtivo.getNome(),
											this.processoAtivo.getTempoRestante()));
									finalizaProcessoAtual();
								}
							}
						}
					}
					this.memoriaOcupado = false;
					ifEspera();
				}

			}

		} catch (ConcurrentModificationException e) {
			System.out.println("\n---------- NIVEL 1[FINALIZADO]----------");
			processaNv2(procs2);
		}

	}

	protected void processaNv2(List<Processo> procs2) {
		List<Processo> procs3 = new ArrayList<>();
		try {
			Integer flagFim = procs2.size();
			Integer flagverificaFim = 0;
			Integer contProcessoAtual = 0;
			Boolean processoAndamento = true;
			System.out.println("\n---------- NIVEL 2----------");

			Integer validaQuantum = 0;
			for (int i = 1; i <= TAMANHO_FIXO_MEMORIA; i++) {
				if (flagFim == flagverificaFim) {
					throw new ConcurrentModificationException();
				}
				System.out.println("uu[" + i + "]: ");

				if (procs2.get(contProcessoAtual).getId() != null && processoAndamento) {
					atualizaProcessoAtivo(procs2.get(contProcessoAtual));
					processoAndamento = false;
				}

				if ((validaQuantum != QUANTUM) && (this.processoAtivo.getId() != null)) {
					validaQuantum++;
					if (this.processoAtivo.getTempoRestante() >= 0) {
						Integer uu = this.processoAtivo.getTempoRestante() - 1;
						System.out.println(this.processoAtivo);
						this.processoAtivo.setTempoRestante(uu);
						if (this.processoAtivo.getTempoRestante() <= 0) {
							validaQuantum = 1;
							processoAndamento = true;
							contProcessoAtual++;
							continue;
						}
					} else if (this.processoAtivo.getTempoRestante() == 0) {

						for (Processo p : procs2) {
							if (p.getId() == this.processoAtivo.getId()) {
								++flagverificaFim;
								procs3.add(new Processo(this.processoAtivo.getId(), this.processoAtivo.getNome(),
										this.processoAtivo.getTempoRestante()));
								finalizaProcessoAtual();
							}
						}
						contProcessoAtual++;
						processoAndamento = true;
					} else {
						for (Processo p : procs2) {
							if (p.getId() == this.processoAtivo.getId()) {
								++flagverificaFim;
								procs3.add(new Processo(this.processoAtivo.getId(), this.processoAtivo.getNome(),
										this.processoAtivo.getTempoRestante()));
								finalizaProcessoAtual();
							}
						}
						contProcessoAtual++;
						processoAndamento = true;
					}
				} else {
					validaQuantum = 1;
					if (this.processoAtivo.getId() != null) {
						if (this.processoAtivo.getTempoRestante() >= 0) {
							Integer uu = this.processoAtivo.getTempoRestante() - 1;
							System.out.println(this.processoAtivo + " ----Quantum finalizado!");
							this.processoAtivo.setTempoRestante(uu);
							if (this.processoAtivo.getTempoRestante() > 0) {
								for (Processo p : procs2) {
									if (p.getId() == this.processoAtivo.getId()) {
										++flagverificaFim;
										procs3.add(new Processo(this.processoAtivo.getId(),
												this.processoAtivo.getNome(), this.processoAtivo.getTempoRestante()));
										finalizaProcessoAtual();
									}
								}
								contProcessoAtual++;
								processoAndamento = true;
							}
						} else {

							for (Processo p : procs2) {
								if (p.getId() == this.processoAtivo.getId()) {
									++flagverificaFim;
									procs3.add(new Processo(this.processoAtivo.getId(), this.processoAtivo.getNome(),
											this.processoAtivo.getTempoRestante()));
									finalizaProcessoAtual();

								}
							}
							contProcessoAtual++;
							processoAndamento = true;
						}
					}
					this.memoriaOcupado = false;
					ifEspera();
				}

			}

		} catch (ConcurrentModificationException e) {
			System.out.println("\n---------- NIVEL 2[FINALIZADO]----------");
			processaNv3(procs3);
		} catch (IndexOutOfBoundsException e1) {
			System.out.println("\n---------- NIVEL 2[FINALIZADO]----------");
			processaNv3(procs3);
		}

	}

	protected void processaNv3(List<Processo> procs3) {
		List<Processo> procsrr = new ArrayList<>();
		try {
			Integer flagFim = procs3.size();
			Integer flagverificaFim = 0;
			Integer contProcessoAtual = 0;
			Boolean processoAndamento = true;
			System.out.println("\n---------- NIVEL 3----------");

			Integer validaQuantum = 0;
			for (int i = 1; i <= TAMANHO_FIXO_MEMORIA; i++) {
				if (flagFim == flagverificaFim) {
					throw new ConcurrentModificationException();
				}
				System.out.println("uu[" + i + "]: ");

				if (procs3.get(contProcessoAtual).getId() != null && processoAndamento) {
					atualizaProcessoAtivo(procs3.get(contProcessoAtual));
					processoAndamento = false;
				}

				if ((validaQuantum != QUANTUM) && (this.processoAtivo.getId() != null)) {
					validaQuantum++;
					if (this.processoAtivo.getTempoRestante() >= 0) {
						Integer uu = this.processoAtivo.getTempoRestante() - 1;
						System.out.println(this.processoAtivo);
						this.processoAtivo.setTempoRestante(uu);
						if (this.processoAtivo.getTempoRestante() <= 0) {
							validaQuantum = 1;
							processoAndamento = true;
							contProcessoAtual++;
							continue;
						}
					} else if (this.processoAtivo.getTempoRestante() == 0) {

						for (Processo p : procs3) {
							if (p.getId() == this.processoAtivo.getId()) {
								++flagverificaFim;
								procsrr.add(new Processo(this.processoAtivo.getId(), this.processoAtivo.getNome(),
										this.processoAtivo.getTempoRestante()));
								finalizaProcessoAtual();
							}
						}
						contProcessoAtual++;
						processoAndamento = true;
					} else {
						for (Processo p : procs3) {
							if (p.getId() == this.processoAtivo.getId()) {
								++flagverificaFim;
								procsrr.add(new Processo(this.processoAtivo.getId(), this.processoAtivo.getNome(),
										this.processoAtivo.getTempoRestante()));
								finalizaProcessoAtual();
							}
						}
						contProcessoAtual++;
						processoAndamento = true;
					}
				} else {
					validaQuantum = 1;
					if (this.processoAtivo.getId() != null) {
						if (this.processoAtivo.getTempoRestante() >= 0) {
							Integer uu = this.processoAtivo.getTempoRestante() - 1;
							System.out.println(this.processoAtivo + " ----Quantum finalizado!");
							this.processoAtivo.setTempoRestante(uu);
							if (this.processoAtivo.getTempoRestante() > 0) {
								for (Processo p : procs3) {
									if (p.getId() == this.processoAtivo.getId()) {
										++flagverificaFim;
										procsrr.add(new Processo(this.processoAtivo.getId(),
												this.processoAtivo.getNome(), this.processoAtivo.getTempoRestante()));
										finalizaProcessoAtual();
									}
								}
								contProcessoAtual++;
								processoAndamento = true;
							}
						} else {

							for (Processo p : procs3) {
								if (p.getId() == this.processoAtivo.getId()) {
									++flagverificaFim;
									procsrr.add(new Processo(this.processoAtivo.getId(), this.processoAtivo.getNome(),
											this.processoAtivo.getTempoRestante()));
									finalizaProcessoAtual();

								}
							}
							contProcessoAtual++;
							processoAndamento = true;
						}
					}
					this.memoriaOcupado = false;
					ifEspera();
				}

			}

		} catch (ConcurrentModificationException e) {
			System.out.println("\n---------- NIVEL 3[FINALIZADO]----------");
			processaRR(procsrr);
		} catch (IndexOutOfBoundsException e1) {
			System.out.println("\n---------- NIVEL 3[FINALIZADO]----------");
			processaRR(procsrr);
		}
	}

	protected void processaRR(List<Processo> procs) {
		System.out.println(procs);
	}
}