package usjt.esc.escalonador;

public class Processo {

	private Integer id;
	private String nome;
	private Integer tempoChegada;
	private Integer tempoRestante;
	private Integer duracaoUU;
	public Processo(Integer id, String nome, Integer tempoChegada, Integer duracaoUU) {
		super();
		this.id = id;
		this.nome = nome;
		this.tempoChegada = tempoChegada;
		this.duracaoUU = duracaoUU;
	}
	public Processo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getTempoChegada() {
		return tempoChegada;
	}
	public void setTempoChegada(Integer tempoChegada) {
		this.tempoChegada = tempoChegada;
	}
	public Integer getTempoRestante() {
		return tempoRestante;
	}
	public void setTempoRestante(Integer tempoRestante) {
		this.tempoRestante = tempoRestante;
	}
	public Integer getDuracaoUU() {
		return duracaoUU;
	}
	public void setDuracaoUU(Integer duracaoUU) {
		this.duracaoUU = duracaoUU;
	}
	@Override
	public String toString() {
		return "Processo [id=" + id + ", nome=" + nome + ", tempoChegada=" + tempoChegada + ", tempoRestante="
				+ tempoRestante + ", duracaoUU=" + duracaoUU + "]";
	}
	
	
}
