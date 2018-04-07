package br.com.sinapse.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.sinapse.R;
import br.com.sinapse.model.Evento;

public class FeedActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private int id = 1;
    public LineAdapter mAdapter;
    public static Evento eventoId;
    public static long result;
    private TextView labelUser, labelNoEvento;
    private RecyclerView recyclerListFeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //mRecyclerView = new RecyclerView();
        //loadEvento();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        this.result = MainActivity.result;
        mAdapter = new LineAdapter(new ArrayList<>(0));
        setupRecycler();
        labelUser = (TextView)findViewById(R.id.labelUser);
        labelNoEvento = (TextView) findViewById(R.id.txtNoEvents);
        recyclerListFeed = (RecyclerView) findViewById(R.id.recycler_list);
        labelUser.setText("Olá " + MainActivity.userLogado.getNome());
        loadEvento();
    }

    private void loadEvento(){
        //Evento evento = EventoFactory.makeEvento();
        //evento.setId(id);
        id++;
        ArrayList<Evento> e = MainActivity.dbHelper.buscaListEvento();
        if(e!=null) {
            labelNoEvento.setVisibility(View.GONE);
            recyclerListFeed.setVisibility(View.VISIBLE);
            for (Evento evento : e)
                mAdapter.updateList(evento);
        }
        else{
            recyclerListFeed.setVisibility(View.GONE);
            labelNoEvento.setVisibility(View.VISIBLE);

        }
        //mAdapter.notifyDataSetChanged();

    }

    public void setupView(View view) {
        loadEvento();
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
        Intent i = new Intent(FeedActivity.this, MainActivity.class);
        startActivity(i);
        finishAffinity();
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
}
