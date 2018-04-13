package br.com.sinapse.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.R;
import br.com.sinapse.controller.DBControl;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;


public class MainActivity extends AppCompatActivity {
    public static DBControl dbHelper;
    private final AppCompatActivity activity = MainActivity.this;
    private TextView userEmail, userSenha;
    public static User userLogado;
    public static Instituicao instLogado;
    public static long result = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        dbHelper = new DBControl(activity);
        initObjects();
        //borda da imagem logo
        criarBordaImagem();

    }

    private boolean validarLogin(){
        String email = userEmail.getText().toString();
        String senha = userSenha.getText().toString();
        MainActivity.userLogado = dbHelper.buscarUser(email, senha, activity);
        if(MainActivity.userLogado != null)
            return true;
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
        if(validarLogin()) {
            Intent i = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(i);
            finishAffinity();
        }//else Toast.makeText(getApplicationContext(), "Credenciais inválidas!", Toast.LENGTH_LONG).show();
        //finish();
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
        userSenha = (TextView) findViewById(R.id.txtPasswordMain);
    }
}
