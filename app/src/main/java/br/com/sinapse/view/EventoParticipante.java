
package br.com.sinapse.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sinapse.R;
import br.com.sinapse.controller.DBControl;
import br.com.sinapse.model.Evento;

public class EventoParticipante extends AppCompatActivity {
    ListView listUser;
    TextView txtTemaEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_participante);
        listUser = (ListView) findViewById(R.id.listUserEvent);
        txtTemaEvento = (TextView) findViewById(R.id.lblEvento);
        txtTemaEvento.setText("Lista de participantes do Evento " + EventoActivity.e.getTema());
        carregaListaUsuarios();
    }

    private void carregaListaUsuarios(){
        ArrayAdapter<String> itensList;
        ArrayList<String> usuarios = MainActivity.dbHelper.listaUsuariosEvento(EventoActivity.e.getId());
        if(!usuarios.isEmpty()) {
            itensList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usuarios);
            listUser.setAdapter(itensList);
        }else {
            listUser.setVisibility(View.GONE);

        }
    }
}
