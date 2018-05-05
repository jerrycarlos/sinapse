package br.com.sinapse.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.sinapse.DBHelper.DBComands;
import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.model.User;
import br.com.sinapse.view.CadastroActivity;
import br.com.sinapse.view.MainActivity;

/**
 * Created by Jerry Jr on 26/03/2018.
 */

public class UserControl {
    private String msgOperacao = "";
    private static Context activity;


    public UserControl(Context c){
        activity = c;
    }

    public static String addUser(User user, DatabaseHelper banco) {
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
            MainActivity.result = db.insert(DBComands.TABLE_USER, null, values);
            if (MainActivity.result != -1)
                db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            return "Registro efetuado com sucesso!";
        }
    }

    /**
     *  Busca e retorna dados do usuario pelo id
     * @param id do usuario
     * @param banco ponteiro onde esta o banco que vou manipular
     * @return usuario consultado
     */
    public static User buscarUser(int id, DatabaseHelper banco){
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

    public static User buscarUser(String email, String senha, Context context, DatabaseHelper banco){
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
        String whereClause = DBComands.COLUMN_USER_EMAIL + " = ? OR " + DBComands.COLUMN_USER_LOGIN + " = ? ";
        //valores que quero comparar cm as colunas que estao no where
        String[] whereValues = { email, email };

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
            Toast.makeText(context,"Credencial não cadastrada!",Toast.LENGTH_LONG).show();
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


    public static boolean verificaUserEvento(int userId, int eventId, DatabaseHelper banco){
        SQLiteDatabase db = banco.getWritableDatabase();
        String[] clauses = {
                String.valueOf(userId), String.valueOf(eventId)
        };
// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_USER_ID + " ASC";

        Cursor c = db.rawQuery(DBComands.SELECT_USER_IN_EVENT,clauses);
        ArrayList<String> user = null;
        if(c.getCount() > 0) {
            return true;
        }
        return false;
    }

    public static boolean verificaUserEventoPalestrante(int userId, int eventId, DatabaseHelper banco){
        SQLiteDatabase db = banco.getWritableDatabase();
        String[] clauses = {
                String.valueOf(userId), String.valueOf(eventId)
        };
// How you want the results sorted in the resulting Cursor
        String sortOrder = DBComands.COLUMN_USER_ID + " ASC";

        Cursor c = db.rawQuery(DBComands.SELECT_USER_PALESTRANTE,clauses);
        ArrayList<String> user = null;
        if(c.getCount() > 0) {
            return true;
        }
        return false;
    }

    public void loginUser(String email, String senha){
        msgOperacao = "Aguarde...";
        JSONObject postData = new JSONObject();
        try {
            postData.put("email",email);
            postData.put("senha",senha);

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute("http://192.168.0.21/buscaUser.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private  class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(activity);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage(msgOperacao);
            this.progress.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                httpURLConnection.setReadTimeout(15000 /* milliseconds */);
                httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();


                //pega o codigo da requisicao http
                int responseCode=httpURLConnection.getResponseCode();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            if (progress.isShowing()) {
                progress.dismiss();
            }
            String titulo = "Sucesso";

            JSONObject json = null;
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null, login = null, instituicao = null,
            curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                json = new JSONObject(result);
                if (json.getLong("id") > 0) {
                        codigo = json.getLong("status");
                        msg = json.getString("msg");
                        id = json.getInt("id");
                        nome = json.getString("nome");
                        email = json.getString("email");
                        login = json.getString("login");
                        instituicao = json.getString("instituicao");
                        curso = json.getString("curso");
                        ocupacao = json.getString("ocupacao");
                        periodo = json.getInt("periodo");
                        telefone = json.getString("telefone");
                        User usr = new User(nome, email, login, senha, ocupacao, instituicao, curso, telefone, periodo);
                        usr.setId(id);
                        MainActivity.userLogado = usr;
                        msg = "Logado com sucesso.";
                }else{
                    titulo  = "Erro";
                    codigo = json.getLong("erro");
                    msg = json.getString("msg");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setMessage(msg)
                    .setTitle(titulo);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }

    }
}
