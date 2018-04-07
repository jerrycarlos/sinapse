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
    public static final String COLUMN_PJ_ID = "id";
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

    public static final String CREATE_TABLE_INSTITUICAO = "CREATE TABLE IF NOT EXISTS " + TABLE_INSTITUICAO + "(" + COLUMN_PJ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PJ_CNPJ + " TEXT NOT NULL UNIQUE, " + COLUMN_PJ_NOME + " TEXT NOT NULL UNIQUE, " + COLUMN_PJ_EMAIL + " TEXT NOT NULL UNIQUE, " + COLUMN_PJ_LOGIN + " TEXT UNIQUE, " + COLUMN_PJ_PASSWORD + " TEXT NOT NULL, " + COLUMN_PJ_CEP + " INTEGER, " + COLUMN_PJ_LOGADOURO + " TEXT, " + COLUMN_PJ_BAIRRO + " TEXT, " + COLUMN_PJ_NUMERO + " INTEGER, " + COLUMN_PJ_CIDADE + " TEXT, " + COLUMN_PJ_ESTADO + " TEXT, " + COLUMN_PJ_PAIS + " TEXT, " + COLUMN_PJ_TELEFONE + " TEXT, " + COLUMN_PJ_SITE + " TEXT, " + COLUMN_PJ_CEO + " TEXT" + ")";

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

    public static final String CREATE_TABLE_EVENTO = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT + "(" + COLUMN_EVENTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EVENTO_TEMA + " TEXT NOT NULL, " + COLUMN_EVENTO_DESCRICAO + " TEXT NOT NULL, " + COLUMN_EVENTO_PALESTRANTE + " INTEGER, " + COLUMN_EVENTO_INSTITUICAO + " INTEGER, " + COLUMN_EVENTO_DATA + " TEXT NOT NULL, " + COLUMN_EVENTO_DURACAO + " INTEGER NOT NULL, " + COLUMN_EVENTO_VAGAS + " INTEGER NOT NULL, " + COLUMN_EVENTO_CATEGORIA + " TEXT, " + COLUMN_EVENTO_HORAS + " INTEGER, " + "FOREIGN KEY (" + COLUMN_EVENTO_PALESTRANTE + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
            "FOREIGN KEY (" + COLUMN_EVENTO_INSTITUICAO + ") REFERENCES " + TABLE_INSTITUICAO + "(" + COLUMN_PJ_ID + ")" + ")";

    public static final String COLUMN_EVENTO_USER_ID = "id";
    public static final String COLUMN_EVENTO_USER_FKUSER = "fk_usuario";
    public static final String COLUMN_EVENTO_USER_FKEVENTO = "fk_evento";

    public static final String CREATE_TABLE_EVENTO_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT_USER + "(" + COLUMN_EVENTO_USER_ID + " INTEGER PRIMARY KEY, " + COLUMN_EVENTO_USER_FKUSER + " INTEGER, " + COLUMN_EVENTO_USER_FKEVENTO + " INTEGER, " + "FOREIGN KEY (" + COLUMN_EVENTO_USER_FKUSER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " + "FOREIGN KEY (" + COLUMN_EVENTO_USER_FKEVENTO + ") REFERENCES " + TABLE_EVENT + "(" + COLUMN_EVENTO_ID + ")" + ")";

}
