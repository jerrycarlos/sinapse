package br.com.sinapse.model;

public class Instituicao {
    /*id integer primary key AUToiNCREMENT,
    cnpj varchar(14) not null unique,
    nome varchar(150) NOT NULL,
    senha varchar(30) not null,
    login varchar(30) unique,
    cep integer(8) not null,
    logadouro varchar(50) not null,
    bairro varchar(50) not null,
    numero integer(8) not null,
    cidade varchar(50) not null,
    estado varchar(50) not null,
    pais varchar(50) not null,
    telefone varchar(14) not null,
    site varchar(50) not null,
    ceo varchar(50) not null,
    email varchar(150) not null unique */
    private String cnpj, nome, senha, login, rua, bairro, cidade, estado, pais, telefone, site, ceo, email;
    private int id, numero;

    public Instituicao(String cnpj, String nome, String senha, String login, String rua, String bairro, String cidade, String estado, String pais, String telefone, String site, String ceo, String email, int id, int numero) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.senha = senha;
        this.login = login;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.telefone = telefone;
        this.site = site;
        this.ceo = ceo;
        this.email = email;
        this.id = id;
        this.numero = numero;
    }

    public Instituicao(String cnpj, String nome, String email, String login, String senha) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.senha = senha;
        this.login = login;
        this.email = email;
    }

    public Instituicao(){

    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }
}
