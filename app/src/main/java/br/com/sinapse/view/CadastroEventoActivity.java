package br.com.sinapse.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.sinapse.R;
import br.com.sinapse.model.Evento;

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
        if(array_spinner != null) {
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, array_spinner);
            s.setAdapter(adapter);
        }else{
            Toast.makeText(getApplicationContext(),"Não há instituições cadastradas no momento.",Toast.LENGTH_LONG).show();
            Intent i = new Intent(CadastroEventoActivity.this, FeedActivity.class);
            startActivity(i);
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
