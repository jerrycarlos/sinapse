package br.com.sinapse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.view.CadastroActivity;
import br.com.sinapse.view.FeedActivity;
import br.com.sinapse.view.MainActivity;

public class CadastroEventoActivity extends AppCompatActivity {
    private String array_spinner[];
    private Spinner s;
    private EditText txtEventoTema, txtEventoDescricao;
    private int eventoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);
        //array_spinner=new String[5];
        s = (Spinner) findViewById(R.id.spinnerInstituicao);
        preencheListaInstituicao();
        carregaElementos();
    }

    private void preencheListaInstituicao(){
        ArrayList<String> array_spinner = MainActivity.dbHelper.buscaInstituicao();
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);
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
        tema = txtEventoTema.getText().toString();
        descricao = txtEventoDescricao.getText().toString();
        fkPalestrante = MainActivity.userLogado.getId();
        fkInstituicao = MainActivity.dbHelper.buscaIdInstituicao(s.getSelectedItem().toString());
        Evento evento = new Evento(tema,descricao,fkPalestrante,fkInstituicao);
        evento.setCategoria("categoria");
        evento.setData("01/01/0001");
        evento.setnVagas(100);
        evento.setQtdHora(55);
        inserirEventoDB(evento);
    }

    private void carregaElementos(){
        txtEventoTema = (EditText) findViewById(R.id.txtEventoTema);
        txtEventoDescricao = (EditText) findViewById(R.id.txtEventoDesc);
    }


}
