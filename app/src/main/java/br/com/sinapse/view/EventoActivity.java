package br.com.sinapse.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import br.com.sinapse.R;
import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;

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
        mostraEvento();
    }

    private void mostraEvento(){
        Evento e = FeedActivity.eventoId;
        tema.setText(e.getTema());
        descricao.setText(e.getDescricao());
        eventoId.setText("#"+String.valueOf(e.getId()));
        User u = MainActivity.dbHelper.buscarUser(e.getFkPalestrante());
        palestrante.setText(u.getNome());
        Instituicao i = MainActivity.dbHelper.buscaInstituicao(e.getFkInstituicao());
        instituicao.setText(i.getNome());
        data.setText(e.getData());
        duracao.setText(String.valueOf(e.getDuracao()));
        vagas.setText(String.valueOf(e.getnVagas()));
        horas.setText(String.valueOf(e.getQtdHora()));
        categoria.setText(e.getCategoria());

    }

    private void carregaObjetos(){
        tema = (TextView) findViewById(R.id.txtTemaEvent);
        data = (TextView) findViewById(R.id.txtDataEvent);
        duracao = (TextView) findViewById(R.id.txtDuracao);
        palestrante = (TextView) findViewById(R.id.txtPalestrante);
        vagas = (TextView) findViewById(R.id.txtVagas);
        descricao = (TextView) findViewById(R.id.txtDescricao);
        categoria = (TextView) findViewById(R.id.txtCategoria);
        instituicao = (TextView) findViewById(R.id.txtLocal);
        horas = (TextView) findViewById(R.id.txtHora);
        eventoId = (TextView) findViewById(R.id.txtEventId);
    }
}
