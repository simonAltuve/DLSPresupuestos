package com.example.dlspresupuestos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorListaGenerarPresupuesto extends RecyclerView.Adapter<AdaptadorListaGenerarPresupuesto.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private List<ElementoLista> mData;
    private LayoutInflater mInflater;
    private Context context;
    int row_index = -1;

    public AdaptadorListaGenerarPresupuesto(List<ElementoLista> itemLista, Context context, RecyclerViewInterface recyclerViewInterface){
        this.recyclerViewInterface = recyclerViewInterface;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemLista;
    }
    @Override
    public int getItemCount(){return mData.size();}

    @Override
    public AdaptadorListaGenerarPresupuesto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.elemento_lista_generar_presupuesto,null);
        return new AdaptadorListaGenerarPresupuesto.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaGenerarPresupuesto.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }
    public void setElementos(List<ElementoLista> elementos){
        mData = elementos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface){
            super(itemView);
            nombre = itemView.findViewById(R.id.productName);
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
            //img.setColorFilter(Color.parseColor(elemento.getColor()), PorterDuff.Mode.SRC_IN);
            nombre.setText(elemento.getNombre());
        }
    }
}
