package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BuscarProductoActivity extends AppCompatActivity implements RecyclerViewInterface {

    List<ElementoLista> elementos;
    private String list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);
        SharedPreferences preferences = getSharedPreferences("DB", Context.MODE_PRIVATE);
        list = preferences.getString("list","");

        init();

    }

    public void init(){
        elementos = new ArrayList<>();
        if(!list.equals("")) {
            cargarLista();
        }else{
            elementos.add(new ElementoLista("Esta vacia la lista", "#215FA6"));
        }
        AdaptadorListaGenerarPresupuesto listAdapter = new AdaptadorListaGenerarPresupuesto(elementos,BuscarProductoActivity.this, this);
        recyclerView = findViewById(R.id.listaRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void OnItemCLick(int position) {
        if(!list.equals("")) {
            Intent updL = new Intent(BuscarProductoActivity.this, UpdateListActivity.class);
            updL.putExtra("archivoNuevo", "");
            updL.putExtra("posicionProducto", String.valueOf(position));
            updL.putExtra("nombreProducto", elementos.get(position).getNombre());
            startActivity(updL);
        }
    }
    public void cargarLista(){
        int i=0;
        String[] cadenas = list.split("---");
        for(i=0;i< cadenas.length;i++){
            if(i==0 || (i%5)==0){
                //agregamos un nombre
                elementos.add(new ElementoLista(cadenas[i], "#215FA6"));
            }
        }
    }
    public void irPrincipal(View view){
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }
}