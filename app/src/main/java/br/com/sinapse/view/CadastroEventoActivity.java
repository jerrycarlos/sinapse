package br.com.sinapse.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import br.com.sinapse.R;
import br.com.sinapse.config.Config;
import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;

public class CadastroEventoActivity extends AppCompatActivity {
    private String array_spinner[];
    private Spinner s;
    private EditText txtEventoTema, txtEventoDescricao;
    private int eventoId;
    private int operacao = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);
        //array_spinner=new String[5];
        s = (Spinner) findViewById(R.id.spinnerInstituicao);
        buscaInstituicoes();
        carregaElementos();
    }

    private void preencheListaInstituicao(ArrayList<String> array_spinner){
        //ArrayList<String> array_spinner = MainActivity.dbHelper.buscaInstituicao();
        if(array_spinner != null) {
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, array_spinner);
            s.setAdapter(adapter);
        }else{
            Toast.makeText(getApplicationContext(),"Não há instituições cadastradas no momento.",Toast.LENGTH_LONG).show();
            Intent i = new Intent(CadastroEventoActivity.this, FeedActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }

    private void inserirEventoDB(Evento e){
        Toast.makeText(getApplicationContext(),MainActivity.dbHelper.addUser(e),Toast.LENGTH_LONG).show();
        if(FeedActivity.result != -1) {
            Intent i = new Intent(CadastroEventoActivity.this, FeedActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }
    public void cadastrarEvento(View v){
        String tema, descricao;
        int fkPalestrante, fkInstituicao;
        if(txtEventoTema.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Insira um tema para o evento!",Toast.LENGTH_SHORT).show();
            return;
        }else if(txtEventoTema.getText().toString().length() < 4){
            Toast.makeText(getApplicationContext(),"Tema com nome muito curto.",Toast.LENGTH_LONG).show();
            return;
        }
        tema = txtEventoTema.getText().toString();

        if(txtEventoDescricao.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Insira uma descrição para o evento!",Toast.LENGTH_SHORT).show();
            return;
        }else if(txtEventoDescricao.getText().toString().length() < 4){
            Toast.makeText(getApplicationContext(),"Descrição muito curta.",Toast.LENGTH_LONG).show();
            return;
        }
        descricao = txtEventoDescricao.getText().toString();

        fkPalestrante = MainActivity.userLogado.getId();
        fkInstituicao = s.getSelectedItemPosition()+1;
        Evento evento = new Evento(tema,descricao,fkPalestrante,fkInstituicao);
        evento.setCategoria("categoria");
        evento.setData("01/01/0001");
        evento.setnVagas(100);
        evento.setQtdHora(55);
        //inserirEventoDB(evento);
        cadastroEntidade(evento);
    }

    private void cadastroEntidade(Evento ev){
        JSONObject postData = new JSONObject();
        try {
            operacao = 1;
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

    private void buscaInstituicoes(){
        JSONObject postData = new JSONObject();
        try {
            operacao = 2;
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

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(CadastroEventoActivity.this);

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
                if(operacao > 1){
                    JSONArray jsonArray = new JSONArray(result);
                    ArrayList<String> listInstituicao = null;
                    if (jsonArray.length() > 0) {
                        listInstituicao = new ArrayList<String>();
                        for(int i =0 ; i<jsonArray.length(); i++){
                            JSONObject xx = jsonArray.getJSONObject(i);
                            Instituicao inst = new Instituicao();
                            inst.setId(xx.getInt("id"));
                            inst.setNome((xx.getString("nome")));
                            listInstituicao.add(inst.getNome());

                        }
                    }
                    preencheListaInstituicao(listInstituicao);
                }else{
                    JSONObject json = new JSONObject(result);
                    codigo = json.getLong("status");
                    msg = json.getString("msg");
                    if (codigo > 0) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        mudaTela();
                    }else {
                        String titulo = "Erro";
                        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroEventoActivity.this);

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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void mudaTela(){
        Intent i = new Intent(CadastroEventoActivity.this, FeedActivity.class);
        startActivity(i);
        finishAffinity();
    }

    private void carregaElementos(){
        txtEventoTema = (EditText) findViewById(R.id.txtEventoTema);
        txtEventoDescricao = (EditText) findViewById(R.id.txtEventoDesc);
    }


}
