package br.com.sinapse.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.sinapse.model.User;
import br.com.sinapse.view.CadastroActivity;

public class CadastroUserControl {
    Context activity;
    public static String servidor = "http://192.168.0.21";
    private String msgOperacao = "";
    public CadastroUserControl(Context context){
        this.activity = context;
    }

    public void cadastroEntidade(User u) {
        msgOperacao = "Registrando usuario...";
        JSONObject postData = new JSONObject();
        try {
            postData.put("nome",u.getNome());
            postData.put("email",u.getEmail());
            postData.put("senha",u.getSenha());
            postData.put("login",u.getLogin());
            postData.put("telefone",u.getTelefone());
            postData.put("instituicao",u.getInstituicao());
            postData.put("curso",u.getCurso());
            postData.put("periodo",u.getPeriodo());
            postData.put("ocupacao",u.getOcupacao());

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute("http://192.168.0.21/cadastroUsuario.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
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


            JSONObject json = null;
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null;
            Log.i("result",result);
            try {
                json = new JSONObject(result);
                codigo = json.getLong("status");
                msg = json.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String titulo = "Sucesso";

            if(codigo == 1){
                CadastroActivity.result = true;

            }
            if( codigo < 1){
                titulo  = "Erro";
                CadastroActivity.result = false;
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
