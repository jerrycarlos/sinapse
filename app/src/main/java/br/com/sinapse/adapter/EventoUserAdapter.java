package br.com.sinapse.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.sinapse.R;
import br.com.sinapse.model.User;
import br.com.sinapse.holder.EventoUserHolder;

public class EventoUserAdapter extends RecyclerView.Adapter<EventoUserHolder> {
    private final List<User> listParticipantes;

    public EventoUserAdapter(ArrayList users) {
        listParticipantes = users;
    }


    public EventoUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventoUserHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_list, parent, false));
    }


    public void onBindViewHolder(EventoUserHolder holder, int position) {
        holder.nome.setText(listParticipantes.get(position).getNome());
        holder.instituicao.setText(listParticipantes.get(position).getInstituicao());
        holder.curso.setText(String.valueOf(listParticipantes.get(position).getCurso()));
    }

    public int getItemCount() {
        return listParticipantes != null ? listParticipantes.size() : 0;
    }

    public void updateList(User user) {
        insertItem(user);
    }

    public void clearList(){
        this.listParticipantes.clear();
    }

    // Método responsável por inserir um novo usuário na lista
    //e notificar que há novos itens.

    private void insertItem(User user) {
        this.listParticipantes.add(user);
        notifyDataSetChanged();
    }

    private void updateItem(int position) {
        User user = this.listParticipantes.get(position);
        //Toast.makeText(,"Registro efetuado!",Toast.LENGTH_LONG).show();

        notifyItemChanged(position);
    }

    public User getUser(int position){
        return listParticipantes.get(position);
    }
}
