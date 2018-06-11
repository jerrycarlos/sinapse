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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.sinapse.config.Config;
import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.view.CadastroActivity;
import br.com.sinapse.view.MainActivity;

public class InstituicaoControl {
    private static Context activity;
    private static Spinner s;
    private static void preencheListaInstituicao(ArrayList<String> array_spinner){
        //ArrayList<String> array_spinner = MainActivity.dbHelper.buscaInstituicao();
        if(array_spinner != null) {
            ArrayAdapter adapter = new ArrayAdapter(activity,
                    android.R.layout.simple_spinner_item, array_spinner);
            s.setAdapter(adapter);
        }
    }

    /**
     *
     * @param tela Context para obter controle da tela em que o usuário está
     * @param spinner Referência do spinner em que será preenchido as instituições cadastradas no banco
     */
    public static void buscaInstituicoes(Context tela, Spinner spinner){
        activity = tela;
        s = spinner;
        JSONObject postData = new JSONObject();
        try {
            postData.put("teste","teste");

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/buscaInstituicao.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(activity);

        protected void onPreExecute() {
            //display progress dialog.
           this.progress.setMessage("Carregando instituições...");
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

            JSONObject json = null;
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                    JSONArray jsonArray = new JSONArray(result);
                    ArrayList<String> listInstituicao = null;
                    if (jsonArray.length() > 0) {
                        listInstituicao = new ArrayList<String>();
                        listInstituicao.add("Qual a sua Instituição?");
                        for(int i =0 ; i<jsonArray.length(); i++){
                            JSONObject xx = jsonArray.getJSONObject(i);
                            Instituicao inst = new Instituicao();
                            inst.setId(xx.getInt("id"));
                            inst.setNome((xx.getString("nome")));
                            listInstituicao.add(inst.getNome());
                        }
                    }
                    preencheListaInstituicao(listInstituicao);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
