package br.com.sinapse.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.InputValidation;
import br.com.sinapse.R;
import br.com.sinapse.controller.DBControl;
import br.com.sinapse.model.User;

public class CadastroActivity extends AppCompatActivity {
    private EditText txtNome, txtEmail, txtLogin, txtSenha, txtOcup, txtCurso, txtInstituicao, txtPeriodo, txtFone;
    private Button btRegistro;
    private Context context;
    private DBControl dbHelper;
    private final AppCompatActivity activity = CadastroActivity.this;
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
        initObjects();
    }

    private void registrod(){
        User user;
        String nome, email, login, senha, ocup, curso, inst;
        int periodo, fone, id;
        nome = txtNome.getText().toString();
        email = txtEmail.getText().toString();
        login = txtLogin.getText().toString();
        senha = txtSenha.getText().toString();
        ocup = txtOcup.getText().toString();
        curso = txtCurso.getText().toString();
        inst = txtInstituicao.getText().toString();
        fone = Integer.parseInt(txtFone.getText().toString());
        periodo = Integer.parseInt(txtPeriodo.getText().toString());
        user = new User(nome,email,login,senha,ocup,inst,curso,fone,periodo);
        postDataToSQLite(user);
    }

    public void registrar(View v){
        registrod();
        //finishAffinity();
    }

    private void postDataToSQLite(User user) {
        //if (!databaseHelper.checkUser(txtEmail.getText().toString().trim())) {

            //user.setNome(txtNome.getText().toString().trim());
            //user.setEmail(txtEmail.getText().toString().trim());
            //user.setSenha(txtSenha.getText().toString().trim());

            long result = dbHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            if(result == -1) {
                Toast.makeText(getApplicationContext(), dbHelper.getClass().getName(), Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Registro efetuado com sucesso!",Toast.LENGTH_LONG).show();
                Intent i = new Intent(CadastroActivity.this, FeedActivity.class);
                startActivity(i);
                finishAffinity();
            }


        //} else {
       //     Toast.makeText(getApplicationContext(),"Insira os dados corretamente!",Toast.LENGTH_LONG).show();
       // }


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
    }
}
