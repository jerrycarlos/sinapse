package br.com.sinapse.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.sinapse.DBHelper.DBComands;
import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;
import br.com.sinapse.view.CadastroActivity;
import br.com.sinapse.view.FeedActivity;
import br.com.sinapse.view.MainActivity;

/**
 * Created by Jerry Jr on 26/03/2018.
 */

public class DBControl {

    private SQLiteDatabase db;
    private DatabaseHelper banco;

    public DBControl(Context context){
        banco = new DatabaseHelper(context);
        this.db = banco.getWritableDatabase();
        banco.onUpgrade(db, DBComands.DATABASE_VERSION,2);
    }
    /**
     * This method is to create user record
     *
     * @param user
     */
    public String addUser(User user) {
        long result = -1;
        SQLiteDatabase db = banco.getReadableDatabase();
        String[] retorno = {"*"};
        String clause = DBComands.COLUMN_USER_EMAIL + " = ?";
        String[] clauses = { user.getEmail() };
        Cursor c = db.query(DBComands.TABLE_USER,retorno,clause,clauses,null,null,null);
        //verifica se email ja existe
        if(c.getCount()>0)
            return "Email já cadastrado!";
        clause = DBComands.COLUMN_USER_LOGIN + " = ?";
        clauses[0] = user.getLogin();
        c = db.query(DBComands.TABLE_USER,retorno,clause,clauses,null,null,null);
        //verifica se login ja existe
        if(c.getCount()>0)
            return "Login já existe!";
        clause = DBComands.COLUMN_USER_TEL + " = ?";
        clauses[0] = String.valueOf(user.getTelefone());
        c = db.query(DBComands.TABLE_USER,retorno,clause,clauses,null,null,null);
        //verifica se telefone ja existe
        if(c.getCount()>0)
            return "Telefone já cadastrado!";

        db = banco.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DBComands.COLUMN_USER_NAME, user.getNome());
            values.put(DBComands.COLUMN_USER_EMAIL, user.getEmail());
            values.put(DBComands.COLUMN_USER_PASSWORD, user.getSenha());
            values.put(DBComands.COLUMN_USER_LOGIN, user.getLogin());
            values.put(DBComands.COLUMN_USER_TEL, user.getTelefone());
            values.put(DBComands.COLUMN_USER_OCUP, user.getOcupacao());
            values.put(DBComands.COLUMN_USER_INSTITUICAO, user.getInstituicao());
            values.put(DBComands.COLUMN_USER_CURSO, user.getCurso());
            values.put(DBComands.COLUMN_USER_PERIODO, user.getPeriodo());
            CadastroActivity.result = db.insert(DBComands.TABLE_USER, null, values);
            if (CadastroActivity.result != -1)
                db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            return "Registro efetuado com sucesso!";
        }
    }

    public String addUser(Instituicao inst) {
        long result = -1;
        SQLiteDatabase db = banco.getReadableDatabase();
        String[] retorno = {"*"};
        String clause = DBComands.COLUMN_PJ_CNPJ + " = ?";
        String[] clauses = { inst.getCnpj() };
        Cursor c = db.query(DBComands.TABLE_INSTITUICAO,retorno,clause,clauses,null,null,null);
        //verifica se cnpj ja existe
        if(c.getCount()>0)
            return "CNPJ já cadastrado!";
        //verifica se email ja existe
        clause = DBComands.COLUMN_PJ_EMAIL + " = ?";
        clauses[0] = inst.getEmail();
        c = db.query(DBComands.TABLE_INSTITUICAO,retorno,clause,clauses,null,null,null);
        if(c.getCount()>0)
            return "Email já cadastrado!";
        //verifica se login ja existe
        clause = DBComands.COLUMN_PJ_LOGIN + " = ?";
        clauses[0] = inst.getLogin();
        c = db.query(DBComands.TABLE_INSTITUICAO,retorno,clause,clauses,null,null,null);
        if(c.getCount()>0)
            return "Login já existe!";

        db = banco.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DBComands.COLUMN_PJ_CNPJ, inst.getCnpj());
            values.put(DBComands.COLUMN_PJ_NOME, inst.getNome());
            values.put(DBComands.COLUMN_PJ_EMAIL, inst.getEmail());
            values.put(DBComands.COLUMN_PJ_PASSWORD, inst.getSenha());
            CadastroActivity.result = db.insert(DBComands.TABLE_INSTITUICAO, null, values);
            if (CadastroActivity.result != -1)
                db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            return "Registro efetuado com sucesso!";
        }
    }

    public String addUser(Evento event) {
        long result = -1;
        SQLiteDatabase db = banco.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DBComands.COLUMN_EVENTO_TEMA, event.getTema());
            values.put(DBComands.COLUMN_EVENTO_DESCRICAO, event.getDescricao());
            values.put(DBComands.COLUMN_EVENTO_PALESTRANTE, event.getFkPalestrante());
            values.put(DBComands.COLUMN_EVENTO_INSTITUICAO, event.getFkInstituicao());
            values.put(DBComands.COLUMN_EVENTO_CATEGORIA, event.getCategoria());
            values.put(DBComands.COLUMN_EVENTO_DATA, event.getData());
            values.put(DBComands.COLUMN_EVENTO_DURACAO, event.getDuracao());
            values.put(DBComands.COLUMN_EVENTO_HORAS, event.getQtdHora());
            values.put(DBComands.COLUMN_EVENTO_VAGAS, event.getnVagas());
            FeedActivity.result = db.insert(DBComands.TABLE_EVENT, null, values);
            if (FeedActivity.result != -1)
                db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            if(FeedActivity.result != -1)
                return "Evento cadastrado com sucesso!";
            else return "Erro ao cadastrar o evento!";
        }
    }

    /*
        COLUMN_USER_ID,        COLUMN_USER_NAME,        COLUMN_USER_EMAIL,        COLUMN_USER_PASSWORD,        COLUMN_USER_LOGIN,        COLUMN_USER_TEL,       COLUMN_USER_OCUP,        COLUMN_USER_INSTITUICAO,        COLUMN_USER_CURSO,        COLUMN_USER_PERIODO
    */

    public User buscarUser(String email, String senha, Context context){
        User user = null;
        //seta a variavel db que o banco vai ser lido
        SQLiteDatabase db = banco.getReadableDatabase();
        //valores que quero retornar da consulta
        String[] projection = {
                DBComands.COLUMN_USER_ID,
                DBComands.COLUMN_USER_NAME,
                DBComands.COLUMN_USER_EMAIL,
                DBComands.COLUMN_USER_PASSWORD,
                DBComands.COLUMN_USER_LOGIN,
                DBComands.COLUMN_USER_TEL, DBComands.COLUMN_USER_OCUP, DBComands.COLUMN_USER_INSTITUICAO,
                DBComands.COLUMN_USER_CURSO, DBComands.COLUMN_USER_PERIODO
        };

        // Filter results WHERE coluna_user = 'valor a ser comparado'
        //ex: where email = email
        //clausuras do where, quais atributos quero filtrar
        String whereClause = DBComands.COLUMN_USER_EMAIL + " = ?";
        //valores que quero comprar cm as colunas que estao no where
        String[] whereValues = { email };

// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_USER_ID + " ASC";
        //execuçao da consulta no banco
        Cursor c = db.query(
                DBComands.TABLE_USER,                     // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        //verifica se houve retorno na consulta
        if(c.getCount()<=0){
            Toast.makeText(context,"Email não cadastrado!",Toast.LENGTH_LONG).show();
            return null;
        }
        //se houver retorno na consulta, verifica se senha esta correta
        c.moveToFirst();
        String pwd = c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_PASSWORD));
        if(!senha.equals(pwd)){
            Toast.makeText(context,"Senha incorreta!",Toast.LENGTH_LONG).show();
            return null;
        }

        //se senha estiver correta, faz a consulta do usuario
// Define a projection that specifies which columns from the database
// you will actually use after this query.
            user = new User();
            c.moveToFirst();
            user.setId(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_ID)));
            user.setNome(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_NAME)));
            user.setEmail(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_EMAIL)));
            user.setSenha(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_PASSWORD)));
            user.setLogin(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_LOGIN)));
            user.setTelefone(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_TEL)));
            user.setInstituicao(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_INSTITUICAO)));
            user.setCurso(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_CURSO)));
            user.setOcupacao(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_OCUP)));
            user.setPeriodo(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_PERIODO)));

        return user;
    }

    public User buscarUser(int id){
        User user = null;
        //seta a variavel db que o banco vai ser lido
        SQLiteDatabase db = banco.getReadableDatabase();
        //valores que quero retornar da consulta
        String[] projection = {
                DBComands.COLUMN_USER_ID,
                DBComands.COLUMN_USER_NAME,
                DBComands.COLUMN_USER_EMAIL,
                DBComands.COLUMN_USER_PASSWORD,
                DBComands.COLUMN_USER_LOGIN,
                DBComands.COLUMN_USER_TEL, DBComands.COLUMN_USER_OCUP, DBComands.COLUMN_USER_INSTITUICAO,
                DBComands.COLUMN_USER_CURSO, DBComands.COLUMN_USER_PERIODO
        };

        // Filter results WHERE coluna_user = 'valor a ser comparado'
        //ex: where email = email
        //clausuras do where, quais atributos quero filtrar
        String whereClause = DBComands.COLUMN_USER_ID + " = ?";
        //valores que quero comprar cm as colunas que estao no where
        String[] whereValues = { String.valueOf(id) };

// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_USER_ID + " ASC";
        //execuçao da consulta no banco
        Cursor c = db.query(
                DBComands.TABLE_USER,                     // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        //se senha estiver correta, faz a consulta do usuario
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        user = new User();
        c.moveToFirst();
        user.setId(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_ID)));
        user.setNome(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_NAME)));
        user.setEmail(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_EMAIL)));
        user.setSenha(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_PASSWORD)));
        user.setLogin(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_LOGIN)));
        user.setTelefone(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_TEL)));
        user.setInstituicao(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_INSTITUICAO)));
        user.setCurso(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_CURSO)));
        user.setOcupacao(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_OCUP)));
        user.setPeriodo(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_PERIODO)));

        return user;
    }

    /**
     *
     * @return lista de instituicao cadastradas no banco
     */
    public ArrayList<String> buscaInstituicao(){
        String[] projection = {
                DBComands.COLUMN_PJ_NOME
        };
// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_PJ_ID + " ASC";

        Cursor c = db.query(
                DBComands.TABLE_INSTITUICAO,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        ArrayList<String> inst = null;
        if(c.getCount() > 0) {
            inst = new ArrayList<String>();
            c.moveToFirst();
            do {
                inst.add(this.preencherListString(c,DBComands.COLUMN_PJ_NOME));
            }while(c.moveToNext());
        }
        return inst;
    }

    public Instituicao buscaInstituicao(int id){
        String[] projection = {
                "*"
        };

        //clausuras do where, quais atributos quero filtrar
        String whereClause = DBComands.COLUMN_PJ_ID + " = ?";
        //valores que quero comprar cm as colunas que estao no where
        String[] whereValues = { String.valueOf(id) };
// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_PJ_ID + " ASC";

        Cursor c = db.query(
                DBComands.TABLE_INSTITUICAO,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        Instituicao inst = null;
        if(c.getCount() > 0) {
            inst = new Instituicao();
            c.moveToFirst();
            inst.setCnpj(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_PJ_CNPJ)));
            inst.setEmail(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_PJ_EMAIL)));
            inst.setId(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_PJ_ID)));
            inst.setNome(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_PJ_NOME)));
            inst.setLogin(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_PJ_LOGIN)));
        }
        return inst;
    }

    /**
     *
     * @return lista de instituicao cadastradas no banco
     */
    public ArrayList<Evento> buscaListEvento(){
        String[] projection = {
                "*"
        };
// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_EVENTO_ID + " ASC";

        Cursor c = db.query(
                DBComands.TABLE_EVENT,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        ArrayList<Evento> event = null;
        if(c.getCount() > 0) {
            event = new ArrayList<Evento>();
            c.moveToFirst();
            do {
                event.add(this.preencherListEvent(c));
            }while(c.moveToNext());
        }
        return event;
    }

    private String preencherListString(Cursor resultSet, String coluna) {
        return resultSet.getString(resultSet.getColumnIndexOrThrow(coluna));
    }

    private Evento preencherListEvent(Cursor c){
        Evento e = new Evento();
        //c.moveToFirst();
        e.setId(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_ID)));
        e.setTema(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_TEMA)));
        e.setDescricao(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_DESCRICAO)));
        e.setFkInstituicao(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_INSTITUICAO)));
        e.setFkPalestrante(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_PALESTRANTE)));
        return e;
    }


    /**
     * busca id da instituicao com o nome
     * @param nome
     * @return
     */
    public int buscaIdInstituicao(String nome){
        String[] projection = {
                DBComands.COLUMN_PJ_ID
        };

        String whereClause = DBComands.COLUMN_PJ_NOME + " = ? ";
        String[] clauses = { nome };
// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_PJ_ID + " ASC";

        Cursor c = db.query(
                DBComands.TABLE_INSTITUICAO,                     // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                clauses,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        if(c.getCount() > 0) {
            c.moveToFirst();
            return c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_PJ_ID));
        }
        return -1;
    }

}
