package br.com.sinapse.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;

public class EventoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String _id = "";
    private TextView tema, data, duracao, palestrante, vagas, descricao, categoria, instituicao, horas, eventoId;
    public static Evento e;
    private AlertDialog alert;
    private GoogleMap gmap;
    private SupportMapFragment mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapView.getMapAsync(this);

        //mapView.onCreate(savedInstanceState);
        //mapView.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Para o layout preencher toda tela do cel (remover a barra de tit.)
        getSupportActionBar().hide(); //esconder ActionBar
        //Intent it = getIntent();
        //_id = it.getStringExtra("evento");
        carregaObjetos();
        this.e = FeedActivity.eventoId;
        mostraEvento();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap.getUiSettings().setZoomControlsEnabled(true);
        LatLng ny = new LatLng(-5.8077710, -35.1986180);
        final CameraPosition pos = new CameraPosition.Builder()
                .target(ny) // Localização
                .bearing(0) // Direcao em graus que a camera aponta
                .tilt(0) // Angulo de posicionamento da camera
                .build();
        CameraUpdate updateCam = CameraUpdateFactory.newCameraPosition(pos);
        gmap.animateCamera(updateCam);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny).title(this.e.getLocal()).snippet("Verifique a rota do local do evento.");
        Marker marker = gmap.addMarker(markerOptions);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gmap.setMyLocationEnabled(true);
    }

    private void mostraEvento(){
        tema.setText(this.e.getTema());
        descricao.setText(this.e.getDescricao());
        eventoId.setText("#"+String.valueOf(this.e.getId()));
        palestrante.setText(e.getPalestrante());
        instituicao.setText(this.e.getLocal());

        data.setText(this.e.getData());
        duracao.setText(String.valueOf(this.e.getDuracao()));
        vagas.setText(String.valueOf(this.e.getnVagas()));
        horas.setText(String.valueOf(this.e.getQtdHora()));
        categoria.setText(this.e.getCategoria());
    }

    private void confirmaInscricao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja se inscrever no evento "+this.e.getTema()+" ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cadastroEntidade();

                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        this.alert = builder.create();
        alert.show();
    }

    public void clickInscricao(View v){
        confirmaInscricao();
    }

    public void abreListaUsuariosEvento(View v){
       startActivity(new Intent(EventoActivity.this,EventoParticipante.class));
    }

    private void cadastroEntidade(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("user",MainActivity.userLogado.getId());
            postData.put("evento",this.e.getId());
            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/cadastroUserEvento.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(EventoActivity.this);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage("Aguarde...");
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
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                JSONObject json = new JSONObject(result);
                codigo = json.getLong("status");
                msg = json.getString("msg");
                if(codigo > 0){
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    mudaTela();
                }else{
                    String titulo = "Erro";
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EventoActivity.this);
                    builder.setMessage(msg)
                                .setTitle(titulo);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    // 3. Get the AlertDialog from create()
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void mudaTela(){
        Intent i = new Intent(EventoActivity.this, FeedActivity.class);
        startActivity(i);
        finishAffinity();
    }

    private void carregaObjetos(){
        tema = (TextView) findViewById(R.id.txtTemaEvent);
        data = (TextView) findViewById(R.id.txtDataEvent);
        duracao = (TextView) findViewById(R.id.txtDuracao);
        palestrante = (TextView) findViewById(R.id.txtPalestrante);
        vagas = (TextView) findViewById(R.id.txtVagas);
        descricao = (TextView) findViewById(R.id.txtDescricao);
        categoria = (TextView) findViewById(R.id.txtCategoria);
        instituicao = (TextView) findViewById(R.id.txtLocal);
        horas = (TextView) findViewById(R.id.txtHora);
        eventoId = (TextView) findViewById(R.id.txtEventId);
    }
}
