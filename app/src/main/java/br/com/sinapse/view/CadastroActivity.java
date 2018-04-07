package br.com.sinapse.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.InputValidation;
import br.com.sinapse.R;
import br.com.sinapse.controller.DBControl;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;

public class CadastroActivity extends AppCompatActivity {
    private EditText txtNome, txtEmail, txtLogin, txtSenha, txtOcup, txtCurso, txtInstituicao, txtPeriodo, txtFone;
    private EditText pjNome, pjEmail, pjLogin, pjSenha, pjCnpj;
    private LinearLayout layoutPessoaF, layoutPessoaJ;
    private RadioButton rdPF, rdPJ;
    private Button btRegistro;
    private Context context;
    private DBControl dbHelper;
    private final AppCompatActivity activity = CadastroActivity.this;
    public static long result = -1;
    //private DatabaseHelper databaseHelper;
    //private InputValidation inputValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //databaseHelper = new DatabaseHelper(getApplicationContext());
        iniciarObjetos();
        //initObjects();
    }

    private void registrod(){
        if(rdPF.isChecked()) {
            User user;
            String nome, email, login, senha, ocup, curso, inst, fone;
            int periodo, id;
            nome = txtNome.getText().toString();
            email = txtEmail.getText().toString();
            if (txtLogin.getText().toString().equals("")) {
                login = email;
            } else login = txtLogin.getText().toString();
            senha = txtSenha.getText().toString();
            ocup = txtOcup.getText().toString();
            curso = txtCurso.getText().toString();
            inst = txtInstituicao.getText().toString();
            fone = txtFone.getText().toString();
            if(txtPeriodo.getText().toString().equals(""))
                periodo = 0;
            else periodo = Integer.parseInt(txtPeriodo.getText().toString());
            user = new User(nome, email, login, senha, ocup, inst, curso, fone, periodo);
            MainActivity.userLogado = user;
            postDataToSQLite(user);
        }
        else if(rdPJ.isChecked()){
            Instituicao inst;
            String nome, email, cnpj, login, senha;
            nome = pjNome.getText().toString();
            email = pjEmail.getText().toString();
            cnpj = pjCnpj.getText().toString();
            if (pjLogin.getText().toString().equals("")) {
                login = email;
            } else login = pjLogin.getText().toString();
            senha = pjSenha.getText().toString();
            inst = new Instituicao(cnpj,nome,email,login,senha);
            MainActivity.instLogado = inst;
            postDataToSQLite(inst);
        }else{
            Toast.makeText(getApplicationContext(),"Escolha uma opção (Aluno ou Instituição)",Toast.LENGTH_LONG).show();
        }
    }

    public void registrar(View v){
        registrod();
        //finishAffinity();
    }

    private void postDataToSQLite(User user) {
        Toast.makeText(getApplicationContext(), MainActivity.dbHelper.addUser(user), Toast.LENGTH_LONG).show();
        if(MainActivity.result > 0){
            Intent i = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }

    private void postDataToSQLite(Instituicao inst){
        Toast.makeText(getApplicationContext(), MainActivity.dbHelper.addUser(inst), Toast.LENGTH_LONG).show();
        if(MainActivity.result > 0){
            Intent i = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }

    public void rdPessoa(View v){
        switch (v.getId()){
            case R.id.rdPF:
                layoutPessoaJ.setVisibility(View.GONE);
                layoutPessoaF.setVisibility(View.VISIBLE);
                break;
            case R.id.rdPJ:
                layoutPessoaF.setVisibility(View.GONE);
                layoutPessoaJ.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void initObjects() {
        dbHelper = new DBControl(activity);
        //databaseHelper = new DatabaseHelper(activity);
        //inputValidation = new InputValidation(activity);
    }

    private void iniciarObjetos(){
        txtNome = (EditText) findViewById(R.id.txtNome);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtOcup = (EditText) findViewById(R.id.txtOcupacao);
        txtCurso = (EditText) findViewById(R.id.txtCurso);
        txtInstituicao = (EditText) findViewById(R.id.txtInstituicao);
        txtPeriodo = (EditText) findViewById(R.id.txtPeriodo);
        txtFone = (EditText) findViewById(R.id.txtFone);
        btRegistro = (Button) findViewById(R.id.btRegistro);
        layoutPessoaF = (LinearLayout) findViewById(R.id.layoutPF);
        layoutPessoaJ = (LinearLayout) findViewById(R.id.layoutPJ);
        rdPF = (RadioButton) findViewById(R.id.rdPF);
        rdPJ = (RadioButton) findViewById(R.id.rdPJ);
        pjNome = (EditText) findViewById(R.id.pjNome);
        pjEmail = (EditText) findViewById(R.id.pjEmail);
        pjLogin = (EditText) findViewById(R.id.pjLogin);
        pjSenha = (EditText) findViewById(R.id.pjSenha);
        pjCnpj = (EditText) findViewById(R.id.pjCnpj);
    }
}
