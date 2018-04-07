package br.com.sinapse.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;

import br.com.sinapse.DBHelper.DBComands;
import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.model.Evento;
import br.com.sinapse.view.FeedActivity;

public class EventoControl {

    public static String addEvento(Evento event, DatabaseHelper banco) {
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

    public static ArrayList<Evento> buscaListEvento(DatabaseHelper banco){
        SQLiteDatabase db = banco.getWritableDatabase();
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
                event.add(preencherListEvent(c));
            }while(c.moveToNext());
        }
        return event;
    }

    private static Evento preencherListEvent(Cursor c){
        Evento e = new Evento();
        //c.moveToFirst();
        e.setId(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_ID)));
        e.setTema(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_TEMA)));
        e.setDescricao(c.getString(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_DESCRICAO)));
        e.setFkInstituicao(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_INSTITUICAO)));
        e.setFkPalestrante(c.getInt(c.getColumnIndexOrThrow(DBComands.COLUMN_EVENTO_PALESTRANTE)));
        return e;
    }
}
