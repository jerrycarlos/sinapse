package br.com.sinapse.model;

public class Evento {
    private int id, fkPalestrante, nVagas, fkInstituicao, qtdHora, duracao;
    private String tema, descricao, data, categoria;

    public Evento(String tema, String descricao){
        this.tema = tema;
        this.descricao = descricao;
    }

    public Evento(int id, String tema, String descricao, int nVagas, int qtdHora, int duracao, String data, String categoria, int fkPalestrante, int fkInstituicao) {
        this.id = id;
        this.fkPalestrante = fkPalestrante;
        this.nVagas = nVagas;
        this.fkInstituicao = fkInstituicao;
        this.qtdHora = qtdHora;
        this.duracao = duracao;
        this.tema = tema;
        this.descricao = descricao;
        this.data = data;
        this.categoria = categoria;
    }

    public Evento(String tema, String descricao, int fkPalestrante, int fkInstituicao) {
        this.fkPalestrante = fkPalestrante;
        this.fkInstituicao = fkInstituicao;
        this.tema = tema;
        this.descricao = descricao;
    }

    public Evento(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFkPalestrante() {
        return fkPalestrante;
    }

    public void setFkPalestrante(int fkPalestrante) {
        this.fkPalestrante = fkPalestrante;
    }

    public int getnVagas() {
        return nVagas;
    }

    public void setnVagas(int nVagas) {
        this.nVagas = nVagas;
    }

    public int getFkInstituicao() {
        return fkInstituicao;
    }

    public void setFkInstituicao(int fkInstituicao) {
        this.fkInstituicao = fkInstituicao;
    }

    public int getQtdHora() {
        return qtdHora;
    }

    public void setQtdHora(int qtdHora) {
        this.qtdHora = qtdHora;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
