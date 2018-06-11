package br.com.sinapse.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import br.com.sinapse.config.Config;
import br.com.sinapse.firebase.NotificationNewEvent;
import br.com.sinapse.model.Evento;
import br.com.sinapse.view.FeedActivity;

public class EventoControl {
    private static Context activity;
    private static Evento evNotify;
    /**
     *
     * @param ev Dados do evento que está sendo criado
     * @param tela Context para obter controle da tela em que o usuário está
     */
    public static void cadastroEntidade(Evento ev, Context tela){
        activity = tela;
        evNotify = ev;
        JSONObject postData = new JSONObject();
        try {
            postData.put("tema",ev.getTema());
            postData.put("descricao",ev.getDescricao());
            postData.put("fk_instituicao",ev.getFkInstituicao());
            postData.put("fk_palestrante",ev.getFkPalestrante());

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/cadastroEvento.php", postData.toString());
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
            this.progress.setMessage("Aguarde...");
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
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                    JSONObject json = new JSONObject(result);
                    codigo = json.getLong("status");
                    msg = json.getString("msg");
                    if (codigo > 0) {
                        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                        NotificationNewEvent.sendNotificationPush(evNotify);
                        mudaTela();
                    }else {
                        String titulo = "Erro";
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private static void mudaTela(){
        Intent i = new Intent(activity, FeedActivity.class);
        activity.startActivity(i);
        ((Activity) activity).finishAffinity();
    }
}
