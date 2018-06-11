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
import br.com.sinapse.controller.EventoControl;
import br.com.sinapse.controller.InstituicaoControl;
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
        InstituicaoControl.buscaInstituicoes(CadastroEventoActivity.this,s);
        carregaElementos();
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
        EventoControl.cadastroEntidade(evento,CadastroEventoActivity.this);
    }

    private void carregaElementos(){
        txtEventoTema = (EditText) findViewById(R.id.txtEventoTema);
        txtEventoDescricao = (EditText) findViewById(R.id.txtEventoDesc);
    }


}
