
package br.com.sinapse.view;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
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
import br.com.sinapse.adapter.EventoUserAdapter;
import br.com.sinapse.config.Config;
import br.com.sinapse.model.User;

public class EventoParticipante extends AppCompatActivity {
    EventoUserAdapter mUserAdapter;
    private ArrayList<User> listUser;
    RecyclerView mListView;
    TextView txtTemaEvento, userCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_participante);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        mListView = (RecyclerView) findViewById(R.id.listUserEvent);
        mUserAdapter = new EventoUserAdapter(new ArrayList<>(0));
        setupList();
        txtTemaEvento = (TextView) findViewById(R.id.lblEvento);
        txtTemaEvento.setText("Lista de participantes do Evento " + EventoActivity.e.getTema());
        listaParticipantes();
        userCount = (TextView) findViewById(R.id.txtCount);

    }

    private void setupList() {
        // Criando o StaggeredGridLayoutManager com duas colunas, descritas no primeiro argumento
        // e no sentido vertical (como uma lista).
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView = new RecyclerView(getApplicationContext());
        mListView.setLayoutManager(layoutManager);
        // Adiciona o adapter que irá anexar os objetos à lista.
        //mAdapter = new LineAdapter(new ArrayList<>(0));
        mListView.setAdapter(mUserAdapter);
    }


    private void listaParticipantes(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("idEvento",EventoActivity.e.getId());
            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/buscaUserEvento.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private  class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(EventoParticipante.this);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage("Carregando paticipantes...");
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

            JSONArray json = null;
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                if(result.length() > 64){
                    json = new JSONArray(result);
                    if (json.length() > 0) {
                        listUser = new ArrayList<User>();
                        mUserAdapter.clearList();
                        for(int i =0 ; i<json.length(); i++){
                            JSONObject xx = json.getJSONObject(i);
                            User u = new User();
                            u.setNome(xx.getString("nome"));
                            u.setInstituicao(xx.getString("instituicao"));
                            u.setCurso(xx.getString("curso"));
                            mUserAdapter.updateList(u);
                        }
                        userCount.setText(Integer.toString(mUserAdapter.getItemCount()));
                    }else{
                        Toast.makeText(getApplicationContext(), "Não há participantes até o momento.", Toast.LENGTH_LONG).show();
                    }
                }else {
                    JSONObject x = new JSONObject(result);
                    Toast.makeText(getApplicationContext(),x.getString("msg"),Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
