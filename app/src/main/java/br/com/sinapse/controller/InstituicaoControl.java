package br.com.sinapse.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;

import br.com.sinapse.DBHelper.DBComands;
import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.view.CadastroActivity;
import br.com.sinapse.view.MainActivity;

public class InstituicaoControl {

    public static String addInstituicao(Instituicao inst, DatabaseHelper banco) {
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
            MainActivity.result = db.insert(DBComands.TABLE_INSTITUICAO, null, values);
            if (MainActivity.result != -1)
                db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            return "Registro efetuado com sucesso!";
        }
    }

    public static ArrayList<String> buscaInstituicao(DatabaseHelper banco){
        SQLiteDatabase db = banco.getWritableDatabase();
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
                inst.add(preencherListString(c,DBComands.COLUMN_PJ_NOME));
            }while(c.moveToNext());
        }
        return inst;
    }

    public static Instituicao buscaInstituicao(int id, DatabaseHelper banco){
        SQLiteDatabase db = banco.getWritableDatabase();
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
     * busca id da instituicao com o nome
     * @param nome
     * @return
     */
    public static int buscaIdInstituicao(String nome, DatabaseHelper banco){
        SQLiteDatabase db = banco.getWritableDatabase();
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

    private static String preencherListString(Cursor resultSet, String coluna) {
        return resultSet.getString(resultSet.getColumnIndexOrThrow(coluna));
    }
}
