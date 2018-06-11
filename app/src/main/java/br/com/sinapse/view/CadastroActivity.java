package br.com.sinapse.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.sinapse.R;
import br.com.sinapse.controller.InstituicaoControl;
import br.com.sinapse.controller.UserControl;
import br.com.sinapse.model.User;

public class CadastroActivity extends AppCompatActivity {
    private EditText txtNome, txtEmail, txtLogin, txtSenha, txtOcup, txtCurso, txtInstituicao, txtPeriodo, txtFone;
    private EditText pjNome, pjEmail, pjLogin, pjSenha, pjCnpj;
    private LinearLayout layoutPessoaF, layoutPessoaJ;
    private RadioButton rdPF, rdPJ;
    public static Spinner s;
    private Button btRegistro;
    private Context context;
    private final AppCompatActivity activity = CadastroActivity.this;
    public static boolean result = false;
    private int operacao = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imgPerfil;
    private static Bitmap foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //databaseHelper = new DatabaseHelper(getApplicationContext());
        s = (Spinner) findViewById(R.id.spinnerInstituicaoPF);
        iniciarObjetos();

        InstituicaoControl.buscaInstituicoes(CadastroActivity.this,s);
        //initObjects();
    }

    /**
     *
     */
    private void registrod(){
        if(rdPF.isChecked()) {
            User user;
            String nome, email, login, senha, ocup, curso, inst, fone;
            int periodo, id;
            if(txtNome.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),"Insira um nome!",Toast.LENGTH_SHORT).show();
                return;
            }else if(txtNome.getText().toString().length() < 4){
                Toast.makeText(getApplicationContext(),"Nome muito curto, insira seu nome corretamente",Toast.LENGTH_LONG).show();
                return;
            }
            nome = txtNome.getText().toString();
            if(txtEmail.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),"Insira um email!",Toast.LENGTH_SHORT).show();
                return;
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()){
                Toast.makeText(getApplicationContext(),"Email fora dos padrões.",Toast.LENGTH_LONG).show();
                return;
            }
            //InputValidation.validaEmail(txtEmail);
            email = txtEmail.getText().toString();

            if (txtLogin.getText().toString().trim().equals("")) {
                login = email;
            } else login = txtLogin.getText().toString();

            if(txtSenha.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),"Insira uma senha!",Toast.LENGTH_SHORT).show();
                return;
            }else if(txtSenha.getText().toString().length() < 6){
                Toast.makeText(getApplicationContext(),"Senha fora dos padrões. (min. 6 caracteres)",Toast.LENGTH_SHORT).show();
                return;
            }
            senha = txtSenha.getText().toString();

            if(txtFone.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),"Insira uma telefone!",Toast.LENGTH_SHORT).show();
                return;
            }else if(txtFone.getText().toString().length() < 9){
                Toast.makeText(getApplicationContext(),"Favor inserir um numero válido. (Ex: xx xxxxx-xxxx)",Toast.LENGTH_SHORT).show();
                return;
            }
            fone = txtFone.getText().toString();
            if(s.getSelectedItemPosition()<=0){
                Toast.makeText(getApplicationContext(),"Escolha sua instituição!",Toast.LENGTH_SHORT).show();
                return;
            }
            inst = s.getSelectedItem().toString();

            if(txtCurso.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),"Insira uma curso!",Toast.LENGTH_SHORT).show();
                return;
            }else if(txtCurso.getText().toString().length() < 3){
                Toast.makeText(getApplicationContext(),"Curso com nome curto.",Toast.LENGTH_SHORT).show();
                return;
            }
            curso = txtCurso.getText().toString();

            if(txtPeriodo.getText().toString().trim().equals("") || Integer.parseInt(txtPeriodo.getText().toString()) > 12 || Integer.parseInt(txtPeriodo.getText().toString()) < 1) {
                Toast.makeText(getApplicationContext(), "Insira seu período atual. (De 1 à 12) ", Toast.LENGTH_SHORT).show();
                return;
            }
            else periodo = Integer.parseInt(txtPeriodo.getText().toString());
            ocup = txtOcup.getText().toString();
            user = new User(nome, email, login, senha, ocup, inst, curso, fone, periodo);
            MainActivity.userLogado = user;
            postDataToSQLite(user);
        }else{
            Toast.makeText(getApplicationContext(),"Escolha uma opção (Aluno ou Instituição)",Toast.LENGTH_LONG).show();
        }
    }

    public void registrar(View v){
        registrod();
        //finishAffinity();
    }


    /**
     * Cadastro de usuario no banco
     * @param user usuario a ser cadastrado no banco
     */
    private void postDataToSQLite(User user) {
        //Toast.makeText(getApplicationContext(), MainActivity.dbHelper.addUser(user), Toast.LENGTH_LONG).show();
            UserControl.cadastroEntidade(user, CadastroActivity.this);
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

    private void iniciarObjetos(){
        txtNome = (EditText) findViewById(R.id.pfNome);
        txtEmail = (EditText) findViewById(R.id.pfEmail);
        txtLogin = (EditText) findViewById(R.id.pfLogin);
        txtSenha = (EditText) findViewById(R.id.pfSenha);
        txtOcup = (EditText) findViewById(R.id.pfOcupacao);
        txtCurso = (EditText) findViewById(R.id.pfCurso);
        txtPeriodo = (EditText) findViewById(R.id.pfPeriodo);
        txtFone = (EditText) findViewById(R.id.pfFone);
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
        this.imgPerfil = (ImageView) findViewById(R.id.imgPerfil);
        this.imgPerfil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                baterFoto();
            }
        });
    }

    private void baterFoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.foto = imageBitmap;
            this.imgPerfil.setImageBitmap(imageBitmap);
        }
        else {
            Toast.makeText(getApplicationContext(),"Captura de foto não pôde ser realizada no momento, tente novamente.",Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap fotoUser(){
        return foto;
    }
}
