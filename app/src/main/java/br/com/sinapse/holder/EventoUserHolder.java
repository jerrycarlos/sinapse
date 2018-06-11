package br.com.sinapse.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.sinapse.R;

public class EventoUserHolder extends RecyclerView.ViewHolder  {
    public TextView nome;
    public TextView instituicao;
    public TextView curso;

    public EventoUserHolder(View itemView) {
        super(itemView);
        nome = itemView.findViewById(R.id.txtNomeList);
        instituicao = itemView.findViewById(R.id.txtInstituicaoList);
        curso = itemView.findViewById(R.id.txtCursoList);
    }
}
