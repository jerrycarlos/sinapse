package br.com.sinapse.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.R;
import br.com.sinapse.config.Config;
import br.com.sinapse.controller.DBControl;
import br.com.sinapse.controller.JSONControl;
import br.com.sinapse.controller.UserControl;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;
// Tacio com voz de Cid Moreira para fazer a propaganda do Sinapse!!!

public class MainActivity extends AppCompatActivity {
    public static DBControl dbHelper;
    public static JSONControl jsonHelper;
    private final Context activity = MainActivity.this;
    private static final String PREF_NAME = "MainActivityPreferences";
    private TextView userEmail, userPass;
    private CheckBox chkManter;
    public static User userLogado;
    public static Instituicao instLogado;
    public static long result = -1;
    private String userLogin, userSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        dbHelper = new DBControl(activity);
        jsonHelper = new JSONControl(MainActivity.this);
        initObjects();
        //borda da imagem logo
        criarBordaImagem();
        SharedPreferences credenciais = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String login = credenciais.getString("login", "");
        String senha = credenciais.getString("senha", "");
        if(!login.equals("") && !senha.equals(""))
            loginUser(login,senha);
    }

    private boolean validarLogin(String login, String senha){
        //MainActivity.userLogado = dbHelper.buscarUser(login, senha, activity);
        loginUser(login,senha);
        if(MainActivity.userLogado != null) {
            return true;
        }
        //comentario teste
        return false;
    }


    public void registrar(View v){
        Intent i = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(i);
        //finishAffinity();
        //finish();
    }

    private void criarBordaImagem(){
        ImageView mimageView = (ImageView) findViewById(R.id.imgMain);

        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.img_main)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);// Round Image Corner 100 100 100 100
        mimageView.setImageBitmap(imageRounded);
    }

    public void abrirFeed(View v){
        this.userLogin = userEmail.getText().toString();
        this.userSenha = userPass.getText().toString();
        if(userLogin.trim().equals("")){
            Toast.makeText(getApplicationContext(),"Insira seu login!", Toast.LENGTH_SHORT).show();
            return;
        }else if(userSenha.trim().equals("")){
            Toast.makeText(getApplicationContext(),"Insira seu senha!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(chkManter.isChecked())
            salvarLogin();
        loginUser(userLogin,userSenha);
    }

    private void validaUsuarioParaLogar(String login, String senha){
        if(validarLogin(login, senha)) {
            if(chkManter.isChecked())
                salvarLogin();
            Intent i = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }

    private void salvarLogin(){
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", this.userLogin);
        editor.putString("senha", this.userSenha);
        editor.commit();
    }

    private long backPressedTime = 0;
    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 segundos para sair
            backPressedTime = t;
            Toast.makeText(this, "Clique 2x voltar para sair!",
                    Toast.LENGTH_SHORT).show();
        } else {    // se pressionado novamente encerrar app
            // clean up
            finish();//super.onBackPressed();       // bye
        }
    }

    private void initObjects(){
        userEmail = (TextView) findViewById(R.id.txtSingin);
        userPass = (TextView) findViewById(R.id.txtPasswordMain);
        chkManter = (CheckBox) findViewById(R.id.checkBoxLembrar);
    }

    private void loginUser(String email, String senha){
        JSONObject postData = new JSONObject();
        try {
            postData.put("email",email);
            postData.put("senha",senha);

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/buscaUser.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private  class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(activity);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage("Entrando...");
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
            String titulo = "Sucesso";

            JSONObject json = null;
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                json = new JSONObject(result);
                if (json.length() > 2) {
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
                    msg = "Logado com sucesso.";
                    Toast.makeText(activity,msg,Toast.LENGTH_LONG).show();
                    mudaTela();
                }else{
                    titulo  = "Erro";
                    codigo = json.getLong("erro");
                    msg = json.getString("msg");
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void mudaTela(){
        Intent i = new Intent(MainActivity.this, FeedActivity.class);
        startActivity(i);
        finishAffinity();
    }
}
