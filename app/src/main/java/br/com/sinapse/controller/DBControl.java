package br.com.sinapse.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import br.com.sinapse.DBHelper.DBComands;
import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.model.User;
import br.com.sinapse.view.CadastroActivity;
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

    public User verificaUser(User u, Context context){
        User user = null;
        //teste commit
        SQLiteDatabase db = banco.getReadableDatabase();
        String[] retorno = {DBComands.COLUMN_USER_PASSWORD};
        String clause = DBComands.COLUMN_USER_EMAIL + " = ? ";
        String[] clauses = { DBComands.COLUMN_USER_EMAIL };
        Cursor c = db.query(DBComands.TABLE_USER,retorno,clause,clauses,null,null,null);
        if(c.getCount()>0){
            c.moveToFirst();
            String pwd = c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_PASSWORD));
            if(!u.getSenha().equals(pwd)){
                //Toast.makeText(context, "Senha incorreta!",Toast.LENGTH_LONG).show();
                return null;
            }
        }else{
            //Toast.makeText(context,"Email não cadastrado!",Toast.LENGTH_LONG).show();
            return null;
        }
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DBComands.COLUMN_USER_ID,
                DBComands.COLUMN_USER_NAME,
                DBComands.COLUMN_USER_EMAIL,
                DBComands.COLUMN_USER_PASSWORD,
                DBComands.COLUMN_USER_LOGIN,
                DBComands.COLUMN_USER_TEL, DBComands.COLUMN_USER_OCUP, DBComands.COLUMN_USER_INSTITUICAO,
                DBComands.COLUMN_USER_CURSO, DBComands.COLUMN_USER_PERIODO
        };

// Filter results WHERE "title" = 'My Title'
        String whereClause = DBComands.COLUMN_USER_EMAIL + " = ? AND " + DBComands.COLUMN_USER_PASSWORD + " = ? ";
        String[] whereValues = { u.getEmail(), u.getSenha() };

// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_USER_ID + " ASC";

        c = db.query(
                DBComands.TABLE_USER,                     // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        if(c.getCount() > 0) {
            user = new User();
            c.moveToFirst();
            user.setId(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_ID)));
            user.setEmail(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_EMAIL)));
            user.setSenha(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_PASSWORD)));
            user.setLogin(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_LOGIN)));
            user.setTelefone(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_TEL)));
            user.setInstituicao(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_INSTITUICAO)));
            user.setCurso(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_CURSO)));
            user.setOcupacao(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_OCUP)));
            user.setPeriodo(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_USER_PERIODO)));
        }

        return user;
    }
}
