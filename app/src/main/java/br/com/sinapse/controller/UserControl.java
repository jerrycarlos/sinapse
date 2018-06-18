package br.com.sinapse.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.sinapse.config.Config;
import br.com.sinapse.firebase.UpdateTokenId;
import br.com.sinapse.model.User;
import br.com.sinapse.view.CadastroActivity;
import br.com.sinapse.view.FeedActivity;
import br.com.sinapse.view.MainActivity;

/**
 * Created by Jerry Jr on 26/03/2018.
 */

public class UserControl extends Activity{
    public static Context activity;
    public static String servidor = "http://192.168.0.21";
    private static String msgOperacao = "";
    public static String imagem = null;
    private static final String PREF_NAME = "MainActivityPreferences";




    /**
     *
     * @param u Dados do usuário que esta pedindo solicitação
     * @param tela Context para obter controle da tela em que o usuário está
     */
    public static void cadastroEntidade(User u, Context tela) {
        activity = tela;
        msgOperacao = "Registrando usuario...";
        Bitmap foto = CadastroActivity.fotoUser();
        if( foto != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            foto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            JSONObject postData = new JSONObject();
            try {
                postData.put("nome", u.getNome());
                postData.put("email", u.getEmail());
                postData.put("senha", u.getSenha());
                postData.put("login", u.getLogin());
                postData.put("telefone", u.getTelefone());
                postData.put("instituicao", u.getInstituicao());
                postData.put("curso", u.getCurso());
                postData.put("periodo", u.getPeriodo());
                postData.put("ocupacao", u.getOcupacao());
                postData.put("foto",encoded);
                SendDeviceDetails t = new SendDeviceDetails();
                t.execute(Config.ip_servidor + "/cadastroUsuario.php", postData.toString());
                //ip externo http://179.190.193.231/cadastro.php
                //ip interno 192.168.0.21 minha casa
                //ip interno hotspot celular 192.168.49.199[
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Context context = activity;
            CharSequence text = "Por favor tire uma foto antes de enviar";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public static void loginUser(String email, String senha, Context context){
        activity = context;
        msgOperacao = "Entrando...";
        JSONObject postData = new JSONObject();
        try {
            postData.put("email",email);
            postData.put("senha",senha);

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/buscaUser.php", postData.toString());
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
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                json = new JSONObject(result);
                codigo = json.getLong("status");
                msg = json.getString("msg");
                if (codigo > 0) {
                    id = json.getInt("id");
                    nome = json.getString("nome");
                    email = json.getString("email");
                    login = json.getString("login");
                    instituicao = json.getString("instituicao");
                    curso = json.getString("curso");
                    ocupacao = json.getString("ocupacao");
                    periodo = json.getInt("periodo");
                    telefone = json.getString("telefone");
                    imagem = json.getString("imagem");
                    if(imagem != null) {
                        String url = Config.ip_servidor + "/profiles/" + imagem + ".png";
                        CarregarImagem.baixarImagem(id, url, activity);
                    }
                    User usr = new User(nome, email, login, senha, ocupacao, instituicao, curso, telefone, periodo);
                    usr.setId(id);
                    MainActivity.userLogado = usr;
                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                    mudaTela();
                } else {
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
        ((Activity)activity).finishAffinity();
    }
}
