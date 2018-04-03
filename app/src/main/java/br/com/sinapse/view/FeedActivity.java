package br.com.sinapse.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.sinapse.R;
import br.com.sinapse.model.Evento;
import br.com.sinapse.repository.EventoFactory;

public class FeedActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private int id = 1;
    public LineAdapter mAdapter;
    public static Evento eventoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //mRecyclerView = new RecyclerView();
        //loadEvento();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        mAdapter = new LineAdapter(new ArrayList<>(0));
        setupRecycler();
    }

    private void loadEvento(){
        Evento evento = EventoFactory.makeEvento();
        evento.setId("#"+id);
        id++;
        mAdapter.updateList(evento);
        //mAdapter.notifyDataSetChanged();

    }

    public void setupView(View view) {
        loadEvento();
    }

    public void infoEvento(View v){
        Intent i = new Intent(FeedActivity.this, EventoActivity.class);
        // Cria um Bundle que vai passar as informações de uma tela para a outra
        Bundle infos = new Bundle();

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
