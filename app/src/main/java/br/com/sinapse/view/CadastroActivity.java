package br.com.sinapse.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import br.com.sinapse.controller.CadastroUserControl;
import br.com.sinapse.controller.DBControl;
import br.com.sinapse.controller.JSONControl;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;

public class CadastroActivity extends AppCompatActivity {
    private EditText txtNome, txtEmail, txtLogin, txtSenha, txtOcup, txtCurso, txtInstituicao, txtPeriodo, txtFone;
    private EditText pjNome, pjEmail, pjLogin, pjSenha, pjCnpj;
    private LinearLayout layoutPessoaF, layoutPessoaJ;
    private RadioButton rdPF, rdPJ;
    private Spinner s;
    private Button btRegistro;
    private Context context;
    private DBControl dbHelper;
    private final AppCompatActivity activity = CadastroActivity.this;
    public static boolean result = false;
    private int operacao = 0;
    //private DatabaseHelper databaseHelper;
    //private InputValidation inputValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //databaseHelper = new DatabaseHelper(getApplicationContext());
        s = (Spinner) findViewById(R.id.spinnerInstituicaoPF);
        iniciarObjetos();

        buscaInstituicoes();
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

            if(txtPeriodo.getText().toString().trim().equals(""))
                periodo = 0;
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
            cadastroEntidade(user);
    }


    private void mudaTela(){
        Intent i = new Intent(CadastroActivity.this, FeedActivity.class);
        startActivity(i);
        finishAffinity();
    }

    private void postDataToSQLite(Instituicao inst){
        Toast.makeText(getApplicationContext(), MainActivity.dbHelper.addUser(inst), Toast.LENGTH_LONG).show();
        if(MainActivity.result > 0){
            Intent i = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }

    private void cadastroEntidade(User u) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("nome",u.getNome());
            postData.put("email",u.getEmail());
            postData.put("senha",u.getSenha());
            postData.put("login",u.getLogin());
            postData.put("telefone",u.getTelefone());
            postData.put("instituicao",u.getInstituicao());
            postData.put("curso",u.getCurso());
            postData.put("periodo",u.getPeriodo());
            postData.put("ocupacao",u.getOcupacao());
            operacao = 1;
            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/cadastroUsuario.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void preencheListaInstituicao(ArrayList<String> array_spinner){
        //ArrayList<String> array_spinner = MainActivity.dbHelper.buscaInstituicao();
        if(array_spinner != null) {
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, array_spinner);
            s.setAdapter(adapter);
        }
    }

    private void buscaInstituicoes(){
        JSONObject postData = new JSONObject();
        try {
            operacao = 2;
            postData.put("teste","teste");

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/buscaInstituicao.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(activity);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage("Registrando usuário...");
            this.progress.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                httpURLConnection.setReadTimeout(15000 /* milliseconds */);
                httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();


                //pega o codigo da requisicao http
                int responseCode=httpURLConnection.getResponseCode();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            if (progress.isShowing()) {
                progress.dismiss();
            }

            JSONObject json = null;
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                if(operacao > 1){
                    JSONArray jsonArray = new JSONArray(result);
                    ArrayList<String> listInstituicao = null;
                    if (jsonArray.length() > 0) {
                        listInstituicao = new ArrayList<String>();
                        listInstituicao.add("Qual a sua Instituição?");
                        for(int i =0 ; i<jsonArray.length(); i++){
                            JSONObject xx = jsonArray.getJSONObject(i);
                            Instituicao inst = new Instituicao();
                            inst.setId(xx.getInt("id"));
                            inst.setNome((xx.getString("nome")));
                            listInstituicao.add(inst.getNome());

                        }
                    }
                    preencheListaInstituicao(listInstituicao);
                }else {
                    json = new JSONObject(result);
                    codigo = json.getLong("status");
                    msg = json.getString("msg");
                    if (codigo > 0) {
                        id = json.getInt("id");
                        nome = json.getString("nome");
                        email = json.getString("email");
                        login = json.getString("login");
                        instituicao = json.getString("instituicao");
                        curso = json.getString("curso");
                        ocupacao = json.getString("ocupacao");
                        periodo = json.getInt("periodo");
                        telefone = json.getString("telefone");
                        User usr = new User(nome, email, login, senha, ocupacao, instituicao, curso, telefone, periodo);
                        usr.setId(id);
                        MainActivity.userLogado = usr;
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        mudaTela();
                    } else {
                        String titulo = "Erro";
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setMessage(msg)
                                .setTitle(titulo);
                        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        // 3. Get the AlertDialog from create()
                        AlertDialog dialog = builder.create();

                        dialog.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    }
}
