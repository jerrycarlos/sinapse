package br.com.sinapse.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

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
import br.com.sinapse.adapter.LineAdapter;
import br.com.sinapse.config.Config;
import br.com.sinapse.controller.CarregarImagem;
import br.com.sinapse.controller.UserControl;
import br.com.sinapse.firebase.UpdateTokenId;
import br.com.sinapse.model.Evento;

public class FeedActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefresh;
    private int id = 1;
    public LineAdapter mAdapter;
    public static Evento eventoId;
    private ArrayList<Evento> listEvento;
    public static long result;
    private TextView labelUser, labelNoEvento;
    private RecyclerView recyclerListFeed;
    private static final String PREF_NAME = "MainActivityPreferences";
    private static Context activity;
    public static ImageView imgProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //mRecyclerView = new RecyclerView();
        //loadEvento();
        activity = FeedActivity.this;
        imgProfile = (ImageView) findViewById(R.id.imgUser);
        UpdateTokenId.updateTokenUser(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_feedlayout);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshContent();
            }
        });
        this.result = MainActivity.result;
        mAdapter = new LineAdapter(new ArrayList<>(0));
        setupRecycler();
        labelUser = (TextView)findViewById(R.id.labelUser);
        labelNoEvento = (TextView) findViewById(R.id.txtNoEvents);
        recyclerListFeed = (RecyclerView) findViewById(R.id.recycler_list);
        labelUser.setText("Olá " + MainActivity.userLogado.getNome());
        //loadEvento();
        listaEvento();
    }

    private void refreshContent(){
        listaEvento();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefresh.setRefreshing(false);
    }


    /**
     * Abre informacao do evento selecionado
     * @param v
     */
    public void infoEvento(View v){
        Intent i = new Intent(FeedActivity.this, EventoActivity.class);
        // Cria um Bundle que vai passar as informações de uma tela para a outra
        Bundle infos = new Bundle();
        eventoId = mAdapter.getEvento(mRecyclerView.getChildAdapterPosition(v));
        //Toast.makeText(getApplicationContext(),e.getTema(),Toast.LENGTH_LONG).show();
        //eventoId = mAdapter.getEvento(pos);
// Adiciona a informação que quer passar informando uma chave e a String
        //Evento e = (Evento)adapter.getAdapter().getItem(pos);
        //eventoId = e;
        //String _id = e.getId();
        //infos.putString("evento",_id);

// Adiciona o Bundle a intent que vai ser chamada
        i.putExtras(infos);
        startActivity(i);
    }


    public void abreCadastroEvento(View v){
        Intent i = new Intent(FeedActivity.this, CadastroEventoActivity.class);
        startActivity(i);
        //finishAffinity();
    }

    public void btLogout(View v){
        esqueceUsuario();
        Intent i = new Intent(FeedActivity.this, MainActivity.class);
        startActivity(i);
        finishAffinity();
    }

    private void salvarLogin(){
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", MainActivity.userLogado.getLogin());
        editor.putString("senha", MainActivity.userLogado.getSenha());
        editor.commit();
    }

    private void esqueceUsuario(){
        UpdateTokenId.updateTokenUser(false);

        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", "");
        editor.putString("senha", "");
        editor.commit();
    }

    private void setupRecycler() {
        // Criando o StaggeredGridLayoutManager com duas colunas, descritas no primeiro argumento
        // e no sentido vertical (como uma lista).
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView = new RecyclerView(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        // Adiciona o adapter que irá anexar os objetos à lista.
        //mAdapter = new LineAdapter(new ArrayList<>(0));
        mRecyclerView.setAdapter(mAdapter);
    }

    private long backPressedTime = 0;

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 segundos para sair
            backPressedTime = t;
            Toast.makeText(this, "Clique 2x voltar para sair!",
                    Toast.LENGTH_SHORT).show();
        } else {    // se pressionado novamente encerrar app
            // clean up
            finish();//super.onBackPressed();       // bye
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(UserControl.imagem != null) {
            String url = Config.ip_servidor + "/profiles/" + UserControl.imagem + ".png";
            CarregarImagem.baixarImagem(MainActivity.userLogado.getId(), url, activity);
        }
        listaEvento();
    }


    private void listaEvento(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("email","teste");
            postData.put("senha","teste");
            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/buscaEvento.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private  class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(FeedActivity.this);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage("Buscando eventos...");
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
                json = new JSONArray(result);
                if (json.length() > 0) {
                    listEvento = new ArrayList<Evento>();
                    labelNoEvento.setVisibility(View.GONE);
                    recyclerListFeed.setVisibility(View.VISIBLE);
                    mAdapter.clearList();
                    for(int i =0 ; i<json.length(); i++){
                        JSONObject xx = json.getJSONObject(i);
                        Evento ev = new Evento();
                        ev.setId(xx.getInt("id"));
                        ev.setDescricao(xx.getString("descricao"));
                        ev.setTema(xx.getString("tema"));
                        ev.setFkInstituicao(xx.getInt("fk_instituicao"));
                        ev.setFkPalestrante(xx.getInt("fk_palestrante"));
                        ev.setLocal(xx.getString("local"));
                        ev.setPalestrante(xx.getString("palestrante"));
                        mAdapter.updateList(ev);
                    }
                    //Swipe load complete
                    onItemsLoadComplete();
                }else{
                    recyclerListFeed.setVisibility(View.GONE);
                    labelNoEvento.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
