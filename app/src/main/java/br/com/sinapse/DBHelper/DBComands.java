package br.com.sinapse.DBHelper;

public class DBComands {

    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "sinapsedb";

    // User table name
    public static final String TABLE_USER = "usuario";
    public static final String TABLE_EVENT = "evento";
    public static final String TABLE_INSTITUICAO = "instituicao";
    public static final String TABLE_EVENT_USER = "evento_participante";
    // User Table Columns names
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "nome";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "senha";
    public static final String COLUMN_USER_LOGIN = "login";
    public static final String COLUMN_USER_TEL = "telefone";
    public static final String COLUMN_USER_OCUP = "ocupacao";
    public static final String COLUMN_USER_INSTITUICAO = "instituicao";
    public static final String COLUMN_USER_CURSO = "curso";
    public static final String COLUMN_USER_PERIODO = "periodo";
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT NOT NULL,"
            + COLUMN_USER_EMAIL + " TEXT NOT NULL unique," + COLUMN_USER_PASSWORD + " TEXT NOT NULL," + COLUMN_USER_LOGIN + " TEXT UNIQUE," + COLUMN_USER_TEL + " TEXT UNIQUE," + COLUMN_USER_OCUP + " TEXT," + COLUMN_USER_INSTITUICAO + " TEXT not null," + COLUMN_USER_CURSO + " TEXT not null," + COLUMN_USER_PERIODO + " INTEGER" + ")";

    //Instituicao Table Columns names
    public static final String COLUMN_PJ_CNPJ = "cnpj";
    public static final String COLUMN_PJ_NOME = "nome";
    public static final String COLUMN_PJ_PASSWORD = "senha";
    public static final String COLUMN_PJ_LOGIN = "login";
    public static final String COLUMN_PJ_CEP = "cep";
    public static final String COLUMN_PJ_LOGADOURO = "logadouro";
    public static final String COLUMN_PJ_BAIRRO = "bairro";
    public static final String COLUMN_PJ_NUMERO = "numero";
    public static final String COLUMN_PJ_CIDADE = "cidade";
    public static final String COLUMN_PJ_ESTADO = "estado";
    public static final String COLUMN_PJ_PAIS = "pais";
    public static final String COLUMN_PJ_TELEFONE = "telefone";
    public static final String COLUMN_PJ_SITE = "site";
    public static final String COLUMN_PJ_CEO = "ceo";
    public static final String COLUMN_PJ_EMAIL = "email";

    //Evento Table Columns names
    public static final String COLUMN_EVENTO_ID = "id";
    public static final String COLUMN_EVENTO_TEMA = "tema";
    public static final String COLUMN_EVENTO_DATA = "data_evento";
    public static final String COLUMN_EVENTO_DURACAO = "duracao";
    public static final String COLUMN_EVENTO_PALESTRANTE = "fk_palestrante";
    public static final String COLUMN_EVENTO_VAGAS = "nr_vagas";
    public static final String COLUMN_EVENTO_DESCRICAO = "descricao";
    public static final String COLUMN_EVENTO_CATEGORIA = "categoria";
    public static final String COLUMN_EVENTO_INSTITUICAO = "fk_instituicao";
    public static final String COLUMN_EVENTO_HORAS = "qtd_hora";


    // create table sql query


    public static final String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT + "(" + ")";
}
