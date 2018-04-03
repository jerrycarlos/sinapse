package br.com.sinapse.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.sinapse.DBHelper.DBComands;
import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.model.User;

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
    public long addUser(User user) {
        long result = -1;
        SQLiteDatabase db = banco.getWritableDatabase();
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
            result = db.insert(DBComands.TABLE_USER, null, values);
            if(result != -1)
                db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            return result;
        }
    }
}
