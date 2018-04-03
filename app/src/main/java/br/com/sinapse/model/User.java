package br.com.sinapse.model;

/**
 * Created by Jerry Jr on 24/03/2018.
 */

public class User {
    /*id int NOT NULL primary key AUTO_INCREMENT,
    nome varchar(150) NOT NULL,
    email varchar(150) NOT NULL unique,
    login varchar(30) unique,
    senha varchar(30) NOT NULL,
    telefone int (11) not null unique,
    ocupacao varchar(50),
    instituicao varchar(50),
    curso varchar(100),
    periodo tinyint*/
    private String nome, email, login, senha, ocupacao, instituicao, curso;
    private int id, telefone, periodo;

    public User(String nome, String email, String login, String senha, String ocupacao, String instituicao, String curso, int telefone, int periodo) {
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.ocupacao = ocupacao;
        this.instituicao = instituicao;
        this.curso = curso;
        this.telefone = telefone;
        this.periodo = periodo;
    }

    public User(){

    }

    public User(String nome, String email, String senha){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId(){
        return id;
    }
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public String getCurso() {
        return curso;
    }

    public int getTelefone() {
        return telefone;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setOcupacao(String ocupacao) {
        this.ocupacao = ocupacao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }
}
