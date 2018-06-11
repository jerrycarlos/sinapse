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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.sinapse.R;
import br.com.sinapse.config.Config;
import br.com.sinapse.controller.UserControl;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;
// Tacio com voz de Cid Moreira para fazer a propaganda do Sinapse!!!

public class MainActivity extends AppCompatActivity {
    public final Context activity = MainActivity.this;
    public static final String PREF_NAME = "MainActivityPreferences";
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
        initObjects();
        //borda da imagem logo
        criarBordaImagem();
        SharedPreferences credenciais = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String login = credenciais.getString("login", "");
        String senha = credenciais.getString("senha", "");
        if(!login.equals("") && !senha.equals(""))
            UserControl.loginUser(login,senha,activity);
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
        UserControl.loginUser(userLogin,userSenha,activity);
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


}
