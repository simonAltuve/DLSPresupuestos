package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ListaArchivosActivity extends AppCompatActivity implements RecyclerViewInterface {

    List<ElementoLista> elementos;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_archivos);

        init();
    }

    public void init(){
        elementos = new ArrayList<>();

        File ruta = Environment.getExternalStorageDirectory();
        File f = new File(ruta.getAbsolutePath()+"/Download/");
        File[] files = listarArchivosDirectorio(f);
        if(files == null){
            f = new File(ruta.getAbsolutePath()+"/download/");
            files = listarArchivosDirectorio(f);
        }
        if(files==null){
            f = new File(ruta.getAbsolutePath()+"/DOWNLOAD/");
            files = listarArchivosDirectorio(f);
        }
        if(files==null){
            f = new File(ruta.getAbsolutePath()+"/Downloads/");
            files = listarArchivosDirectorio(f);
        }
        if(files==null){
            f = new File(ruta.getAbsolutePath()+"/downloads/");
            files = listarArchivosDirectorio(f);
        }
        if(files==null){
            f = new File(ruta.getAbsolutePath()+"/DOWNLOADS/");
            files = listarArchivosDirectorio(f);
        }


        for(int i=0; i<files.length;i++){
            File file = files[i];
            if(file.isDirectory()){

            }else{
                elementos.add(new ElementoLista(file.getName(), "#215FA6"));
            }
        }
        if(files.length == 0){
            elementos.add(new ElementoLista("No hay archivos", "#215FA6"));
        }

        AdaptadorLista listAdapter = new AdaptadorLista(elementos,this, this);
        recyclerView = findViewById(R.id.listaRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

    }

    @Override
    public void OnItemCLick(int position) {
        //Toast.makeText(this, elementos.get(position).getNombre(), Toast.LENGTH_SHORT).show();
        Intent updL = new Intent(ListaArchivosActivity.this, UpdateListActivity.class);
        updL.putExtra("archivoNuevo", elementos.get(position).getNombre());
        updL.putExtra("posicionProducto", "");
        updL.putExtra("nombreProducto", "");
        startActivity(updL);
    }
    public void irPrincipal(View view){
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }
    public File[] listarArchivosDirectorio(File f){
        File[] files = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });
        return files;
    }

}