package br.com.sinapse.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.sinapse.R;
import br.com.sinapse.view.FeedActivity;

import static android.content.Context.MODE_PRIVATE;

public class CarregarImagem extends Activity{

    private static Context activity;
    private static String url;

    public static void baixarImagem(int id, String u, Context context){
        activity = context;
        url = u;
        DownloadImagemAsyncTask t = new DownloadImagemAsyncTask();
        t.execute(url);
    }


    static class DownloadImagemAsyncTask extends
            AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {
            String urlString = params[0];

            try {
                URL url = new URL(urlString);
                HttpURLConnection conexao = (HttpURLConnection)
                        url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setDoInput(true);
                conexao.connect();

                InputStream is = conexao.getInputStream();
                Bitmap imagem = BitmapFactory.decodeStream(is);
                return imagem;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                FeedActivity.imgProfile.setImageBitmap(result);
            } else {
                String msg = "Imagem do perfil não pôde ser baixada ou você ainda não possui foto de perfil";
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}