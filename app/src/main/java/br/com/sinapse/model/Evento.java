package br.com.sinapse.model;

public class Evento {
    private String id;
    private String tema;
    private String descricao;

    public Evento(String tema, String descricao){
        this.tema = tema;
        this.descricao = descricao;
    }

    public Evento(){

    }
    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTema() {
        return tema;
    }

    public String getDescricao() {
        return descricao;
    }
}
