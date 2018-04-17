package br.com.sinapse.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import br.com.sinapse.R;
import br.com.sinapse.controller.EventoControl;
import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;

public class EventoActivity extends AppCompatActivity {
    private String _id = "";
    private TextView tema,data,duracao,palestrante,vagas,descricao,categoria,instituicao,horas,eventoId;
    public static Evento e;
    private AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //Intent it = getIntent();
        //_id = it.getStringExtra("evento");
        carregaObjetos();
        this.e = FeedActivity.eventoId;
        mostraEvento();
    }

    private void mostraEvento(){
        tema.setText(this.e.getTema());
        descricao.setText(this.e.getDescricao());
        eventoId.setText("#"+String.valueOf(this.e.getId()));
        User u = MainActivity.dbHelper.buscarUser(this.e.getFkPalestrante());
        palestrante.setText(u.getNome());
        Instituicao i = MainActivity.dbHelper.buscaInstituicao(this.e.getFkInstituicao());
        instituicao.setText(i.getNome());
        data.setText(this.e.getData());
        duracao.setText(String.valueOf(this.e.getDuracao()));
        vagas.setText(String.valueOf(this.e.getnVagas()));
        horas.setText(String.valueOf(this.e.getQtdHora()));
        categoria.setText(this.e.getCategoria());
    }

    private void confirmaInscricao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja se inscrever no evento "+this.e.getTema()+" ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        retornoInscricao();

                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        this.alert = builder.create();
        alert.show();
    }

    private void retornoInscricao(){
        if(!MainActivity.dbHelper.verificaUsuarioPalestranteEvento(MainActivity.userLogado.getId(),e.getId())) {
            if (!MainActivity.dbHelper.verificaUsuarioNoEvento(MainActivity.userLogado.getId(),e.getId())) {
                if (!MainActivity.dbHelper.inscreveUsuarioEvento(MainActivity.userLogado.getId(), e.getId())) {
                    Toast.makeText(getApplicationContext(), "Inscrição efetuada com sucesso!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EventoActivity.this, FeedActivity.class));
                    finishAffinity();
                } else
                    Toast.makeText(getApplicationContext(), "Não foi possível realizar inscrição.", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getApplicationContext(), "Usuário já cadastrado no evento.", Toast.LENGTH_LONG).show();
        }else Toast.makeText(getApplicationContext(), "Você já é palestrante do evento.", Toast.LENGTH_LONG).show();
    }

    public void clickInscricao(View v){
        confirmaInscricao();
    }

    public void abreListaUsuariosEvento(View v){
        startActivity(new Intent(EventoActivity.this,EventoParticipante.class));
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
