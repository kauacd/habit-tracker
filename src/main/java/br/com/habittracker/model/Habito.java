package br.com.habittracker.model;

public class Habito {
    private int id;
    private int usuarioId;
    private String nome;
    private String descricao;
    private boolean concluidoHoje;

    public Habito() { }

    public Habito(int id, int usuarioId, String nome, String descricao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isConcluidoHoje() { return concluidoHoje; }
    public void setConcluidoHoje(boolean concluidoHoje) { this.concluidoHoje = concluidoHoje; }
}
