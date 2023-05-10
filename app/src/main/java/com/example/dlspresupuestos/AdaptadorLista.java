package com.example.dlspresupuestos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private List<ElementoLista> mData;
    private LayoutInflater mInflater;
    private Context context;

    public AdaptadorLista(List<ElementoLista> itemLista, Context context, RecyclerViewInterface recyclerViewInterface){
        this.recyclerViewInterface = recyclerViewInterface;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemLista;
    }
    @Override
    public int getItemCount(){return mData.size();}

    @Override
    public AdaptadorLista.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.elemento_lista,null);
        return new AdaptadorLista.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorLista.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }
    public void setElementos(List<ElementoLista> elementos){
        mData = elementos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView nombre;
        ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface){
            super(itemView);
            img = itemView.findViewById(R.id.imgFile);
            nombre = itemView.findViewById(R.id.fileName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.OnItemCLick(position);
                        }
                    }
                }
            });
        }

        void bindData(final ElementoLista elemento){
            img.setColorFilter(Color.parseColor(elemento.getColor()), PorterDuff.Mode.SRC_IN);
            nombre.setText(elemento.getNombre());
        }
    }
}
