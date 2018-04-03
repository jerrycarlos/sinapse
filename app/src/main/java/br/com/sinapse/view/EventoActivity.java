package br.com.sinapse.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import br.com.sinapse.R;
import br.com.sinapse.model.Evento;

public class EventoActivity extends AppCompatActivity {
    private String _id = "";
    private TextView tema,data,duracao,palestrante,vagas,descricao,categoria,instituicao,horas,eventoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //Intent it = getIntent();
        //_id = it.getStringExtra("evento");
        carregaObjetos();
        //mostraEvento();
    }

    private void mostraEvento(){
        Evento e = FeedActivity.eventoId;
        tema.setText(e.getTema());
        descricao.setText(e.getDescricao());
        eventoId.setText(e.getId());
    }

    private void carregaObjetos(){
        tema = (TextView) findViewById(R.id.txtTema);
        data = (TextView) findViewById(R.id.txtData);
        duracao = (TextView) findViewById(R.id.txtDuracao);
        palestrante = (TextView) findViewById(R.id.txtPalestrante);
        vagas = (TextView) findViewById(R.id.txtVagas);
        descricao = (TextView) findViewById(R.id.txtDescricao);
        categoria = (TextView) findViewById(R.id.txtCategoria);
        instituicao = (TextView) findViewById(R.id.txtInstituicao);
        horas = (TextView) findViewById(R.id.txtHora);
        eventoId = (TextView) findViewById(R.id.eventId);
    }
}
