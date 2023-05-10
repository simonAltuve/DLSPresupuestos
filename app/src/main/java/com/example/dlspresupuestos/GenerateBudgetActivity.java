package com.example.dlspresupuestos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class GenerateBudgetActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ArrayList<Double> precios;
    private String list;
    private ClipboardManager clipboardManager;
    private TextView tv_budget;
    List<ElementoLista> elementos;
    List<ElementosPresupuesto> elementosPresupuestos;
    String cantidadDialog, precioDialog;
    RecyclerView recyclerView;
    private DecimalFormat df;
    private Button b_compartir, b_borrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_budget);

        SharedPreferences preferences = getSharedPreferences("DB", Context.MODE_PRIVATE);
        list = preferences.getString("list","");
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        tv_budget = (TextView) findViewById(R.id.tv_budget);
        b_compartir = (Button) findViewById(R.id.b_compartir);
        b_borrar = (Button) findViewById(R.id.b_clear);
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        df = new DecimalFormat("0.00",simbolo);
        cantidadDialog = "";
        precioDialog = "";

        init();
        b_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = tv_budget.getText().toString();
                if(!mensaje.equals("Seleccione los productos...")){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Información");
                    intent.putExtra(Intent.EXTRA_TEXT, mensaje);
                    startActivity(Intent.createChooser(intent,"Compartir"));
                }
            }
        });

        b_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementosPresupuestos.clear();
                ActualizarPresupuesto();
            }
        });

    }

    public void init(){
        elementos = new ArrayList<>();
        elementosPresupuestos = new ArrayList<>();
        precios = new ArrayList<>();
        if(!list.equals("")) {
            cargarLista();
        }else{
            elementos.add(new ElementoLista("Esta vacia la lista", "#215FA6"));
        }

        AdaptadorListaGenerarPresupuesto listAdapter = new AdaptadorListaGenerarPresupuesto(elementos,GenerateBudgetActivity.this, this);
        recyclerView = findViewById(R.id.listaRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void OnItemCLick(int position) {
        String nombreProducto = elementos.get(position).getNombre();
        View view1 = LayoutInflater.from(GenerateBudgetActivity.this).inflate(R.layout.dialog_producto_layout,null);
        TextInputEditText cantidad = view1.findViewById(R.id.txt_cantidadDialog);
        TextInputEditText precio = view1.findViewById(R.id.txt_precioDialog);
        int posicionEnPresupuesto = VerificarProductoPresupuesto(nombreProducto);
        if(posicionEnPresupuesto == -1){
            cantidad.setText("1.00");
            String p = df.format(precios.get(position));
            precio.setText(p.replace(',','.'));
        }else{
            cantidad.setText(df.format(elementosPresupuestos.get(posicionEnPresupuesto).getCantidad()));
            precio.setText(df.format(elementosPresupuestos.get(posicionEnPresupuesto).getPrecio()));
        }
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(GenerateBudgetActivity.this)
                .setTitle(nombreProducto)
                .setView(view1)
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String c = cantidad.getText().toString();
                        String p = precio.getText().toString();
                        String c2 = c.replace(".","");
                        String p2 = p.replace(".","");
                        //validamos que tenga un solo punto y no esten vacios
                        if(!c.equals("") && !p.equals("") && (c2.length()==(c.length()-1) ||
                                c2.length()==c.length()) && (p2.length()==(p.length()-1) ||
                                p2.length()==p.length())) {
                            AgregarProductoPresupuesto(position, nombreProducto, p,
                                    c, posicionEnPresupuesto);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(posicionEnPresupuesto != -1){
                            EliminarProductoPresupuesto(posicionEnPresupuesto);
                        }
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();

    }
    //funcion que carga la lista del recyclerView
    public void cargarLista(){
        int i=0;
        String[] cadenas = list.split("---");
        for(i=0;i< cadenas.length;i++){
            if(i==0 || (i%5)==0){
                //agregamos un nombre
                elementos.add(new ElementoLista(cadenas[i], "#215FA6"));
            }else if((i-2)==0 || ((i-2)%5)==0){
                //agregamos el precio de venta
                precios.add(new Double(Double.valueOf(cadenas[i])));
            }
        }
    }

    //funcion que agrega un producto al presupuesto
    public void AgregarProductoPresupuesto(int posicionLista, String nombre, String precio, String cantidad,
                                           int posicionEnPresupuesto){
        double p = Double.valueOf(precio);
        double c = Double.valueOf(cantidad);
        if(posicionEnPresupuesto == -1){
            elementosPresupuestos.add(new ElementosPresupuesto(nombre,p,c,(p*c)));
        }else{
            ElementosPresupuesto e = elementosPresupuestos.get(posicionEnPresupuesto);
            e.setCantidad(c);
            e.setPrecio(p);
            e.setSubtotal(p*c);
        }
        ActualizarPresupuesto();
    }

    //funcion que elimina un producto del presupuesto
    public void EliminarProductoPresupuesto(int posicion){
        elementosPresupuestos.remove(posicion);
        ActualizarPresupuesto();
    }

    //funcion que verifica si un producto esta agregado en el presupuesto
    public int VerificarProductoPresupuesto(String nombreProducto){
        int posicion = -1;

        for (int i = 0; i < elementosPresupuestos.size(); i++) {
            if (elementosPresupuestos.get(i).getNombre().equals(nombreProducto)) {
                posicion = i;
            }
        }
        return posicion;
    }

    //Funcion que actualiza la cadena del presupuesto en el textView
    public void ActualizarPresupuesto(){
        double total = 0;
        String presupuesto = "Presupuesto:\n\n"+"Cant. Descripción  Subtotal\n";
        for(int i=0;i<elementosPresupuestos.size();i++){
            double st = elementosPresupuestos.get(i).getSubtotal();
            String linea = df.format(elementosPresupuestos.get(i).getCantidad())+" "
                    +elementosPresupuestos.get(i).getNombre()+"  "
                    +df.format(st)+"\n";
            presupuesto = presupuesto + linea;
            total += st;
        }
        presupuesto = presupuesto +"\nTotal: "+ df.format(total)+"$";
        if(elementosPresupuestos.size()==0){
            presupuesto = "Seleccione los productos...";
        }
        tv_budget.setText(presupuesto);
    }

    public void CopiarPresupuesto(View view){
        if(elementosPresupuestos.size()>0){
            ClipData data = ClipData.newPlainText("texto", tv_budget.getText().toString());
            clipboardManager.setPrimaryClip(data);
            //Toast.makeText(this,"Copiado", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.scrollView2),"Presupuesto copiado",Snackbar.LENGTH_LONG).show();
        }
    }

    public void irPrincipal(View view){
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }

}