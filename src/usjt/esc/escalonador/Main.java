package usjt.esc.escalonador;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import usjt.esc.memoria.Memoria;
import usjt.esc.memoria.Particao;

public class Main {

	private static Integer TAMANHO_FIXO_MEMORIA = 100;
	private static Integer TAMANHO_FIXO_P1 = 10;
	private static Integer TAMANHO_FIXO_P2 = 40;
	private static Integer TAMANHO_FIXO_P3 = 30;
	private static Integer TAMANHO_FIXO_P4 = 20;

	public static void main(String[] args) {
		List<Memoria> memorias = new ArrayList<>();

		Particao part1 = new Particao(TAMANHO_FIXO_P1);
		Particao part2 = new Particao(TAMANHO_FIXO_P2);
		Particao part3 = new Particao(TAMANHO_FIXO_P3);
		Particao part4 = new Particao(TAMANHO_FIXO_P4);
		List<Particao> particoes = new ArrayList<>();
		particoes.add(part1);
		particoes.add(part2);
		particoes.add(part3);
		particoes.add(part4);

		Memoria memoria1 = new Memoria(TAMANHO_FIXO_MEMORIA, particoes);
		memorias.add(memoria1);

		JOptionPane.showMessageDialog(null,
				"Tamanho: " + TAMANHO_FIXO_MEMORIA + "\n" + "Qtde Particoes: 4 \n" + "Tamanho p1: " + TAMANHO_FIXO_P1
						+ "\n" + "Tamanho p2: " + TAMANHO_FIXO_P2 + "\n" + "Tamanho p3: " + TAMANHO_FIXO_P3 + "\n"
						+ "Tamanho p4: " + TAMANHO_FIXO_P4 + "\n" + "Particao variavel: Worst_Fit ",
				"Memoria", JOptionPane.INFORMATION_MESSAGE);

		alocaProcessos(memoria1);
		if(memoria1 != null) {
			System.out.println("Worst Fit foi um sucesso !");
		}
	}

	public static void alocaProcessos(Memoria memoria) {
		// insere e separa os processos do usuario
		Integer qtde = Integer.parseInt(JOptionPane.showInputDialog("Insira a quantidade de processos: "));

		System.out.println("Recebendo processos ...");
		List<Processo> processos = new ArrayList<>();

		for (int i = 0; i < qtde; i++) {
			Integer id = i + 1 ;
			String nomep = JOptionPane.showInputDialog(null, "Digite o NOME do processo: ");
			Integer duracaop = Integer
					.parseInt(JOptionPane.showInputDialog(null, "Digite a DURACAO de uu do processo " + nomep + ": "));
			Integer chegadap = Integer.parseInt(JOptionPane.showInputDialog(null, "Digita a CHEGADA do processo: "));
			JOptionPane.showMessageDialog(null, "Processo Salvo !");

			Processo novo = new Processo(id, nomep, chegadap, duracaop);
			System.out.println(novo.toString());
			processos.add(novo);
		}
		
		System.out.println("Iniciando Worst Fit ...");
		memoria.worstFit(processos);
		Processador.processaMultinivel(memoria);
	}

}
