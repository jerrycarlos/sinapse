package br.com.sinapse.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.sinapse.R;

public class LineHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView descricao;
    public TextView eventId;
    public ImageButton btInfo;

    public LineHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.txtTitle);
        descricao = itemView.findViewById(R.id.txtDescricao);
        eventId = itemView.findViewById(R.id.eventId);
        btInfo = itemView.findViewById(R.id.imgList);
    }
}
