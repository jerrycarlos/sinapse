package br.com.sinapse.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.sinapse.R;
import br.com.sinapse.model.User;

public class TestActivity extends AppCompatActivity {
    private EditText tNome,tSenha,tEmail,tLogin,tPeriodo,tOcup,tInst,tFone,tCurso;
    public static User usuarioLogado = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initObjects();
    }

    public void btEnviarDados(View v){
        enviarDados();
    }

    private void enviarDados() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("name",tNome.getText().toString());
            postData.put("email",tEmail.getText().toString());
            postData.put("senha",tSenha.getText().toString());
            /*postData.put("login",tLogin.getText().toString());
            postData.put("senha",tSenha.getText().toString());
            postData.put("curso",tCurso.getText().toString());
            postData.put("periodo",tPeriodo.getText().toString());
            postData.put("instituicao",tInst.getText().toString());
            postData.put("ocupacao",tOcup.getText().toString());
            postData.put("fone",tFone.getText().toString());*/

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute("http://192.168.0.21/service.php", postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(TestActivity.this);

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

            String titulo = "";
            if( codigo == 1L){
                usuarioLogado = new User();
                usuarioLogado.setNome(tNome.getText().toString());
                usuarioLogado.setEmail(tEmail.getText().toString());
                usuarioLogado.setSenha(tSenha.getText().toString());
            }else{
                usuarioLogado = null;
            }



            AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);

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

    private void initObjects(){
        tNome = (EditText) findViewById(R.id.txtNomeTest);
        tEmail = (EditText) findViewById(R.id.txtEmailTest);
        tSenha = (EditText) findViewById(R.id.txtSenhaTest);
        tLogin = (EditText) findViewById(R.id.txtLoginTest);
        tInst = (EditText) findViewById(R.id.txtInstituicaoTest);
        tOcup = (EditText) findViewById(R.id.txtOcupacaoTest);
        tPeriodo = (EditText) findViewById(R.id.txtPeriodoTest);
        tFone = (EditText) findViewById(R.id.txtFoneTest);
        tCurso = (EditText) findViewById(R.id.txtCursoTest);
    }
}
