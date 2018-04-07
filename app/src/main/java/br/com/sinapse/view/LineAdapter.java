package br.com.sinapse.view;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.sinapse.R;
import br.com.sinapse.model.Evento;

public class LineAdapter  extends RecyclerView.Adapter<LineHolder> {
    private final List<Evento> evento;

    public LineAdapter(ArrayList event) {
        evento = event;
    }


    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_list, parent, false));
    }


    public void onBindViewHolder(LineHolder holder, int position) {
        holder.title.setText(evento.get(position).getTema());
        holder.descricao.setText(evento.get(position).getDescricao());
        holder.eventId.setText("#" + String.valueOf(evento.get(position).getId()));
        holder.btInfo.setOnClickListener(view -> getEvento(position));
    }

    public int getItemCount() {
        return evento != null ? evento.size() : 0;
    }

    public void updateList(Evento evento) {
        insertItem(evento);
    }

    // Método responsável por inserir um novo usuário na lista
    //e notificar que há novos itens.

    private void insertItem(Evento evento) {
        this.evento.add(evento);
        notifyDataSetChanged();
    }

    private void updateItem(int position) {
        Evento evento = this.evento.get(position);
        //Toast.makeText(,"Registro efetuado!",Toast.LENGTH_LONG).show();

        notifyItemChanged(position);
    }

    public Evento getEvento(int position){
        return evento.get(position);
    }
}
