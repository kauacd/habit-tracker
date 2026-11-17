package br.com.habittracker.service;

/** Dados da tela de histórico de um hábito (US04). */
public class HistoricoHabito {
    private final int id;
    private final String nome;
    private final String descricao;
    private final Resumo7Dias resumo;
    private final int totalConclusoes;

    public HistoricoHabito(int id, String nome, String descricao, Resumo7Dias resumo, int totalConclusoes) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.resumo = resumo;
        this.totalConclusoes = totalConclusoes;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Resumo7Dias getResumo() { return resumo; }
    public int getTotalConclusoes() { return totalConclusoes; }

    /** Nenhuma conclusão registrada até hoje: ainda não há o que mostrar. */
    public boolean isSemDados() { return totalConclusoes == 0; }
}
