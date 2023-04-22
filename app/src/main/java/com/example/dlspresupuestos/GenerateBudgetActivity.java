package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class GenerateBudgetActivity extends AppCompatActivity {

    private String[] lista;
    private String[] cadenas;
    private String presupuesto;
    private String list;
    private ListView lv1;
    private TextView tv_budget;
    private ClipboardManager clipboardManager;
    private RadioButton rb1;
    private RadioButton rb2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_budget);

        SharedPreferences preferences = getSharedPreferences("DB", Context.MODE_PRIVATE);
        list = preferences.getString("list","");
        lv1 = (ListView) findViewById(R.id.lv_lista_pruebas);
        tv_budget = (TextView) findViewById(R.id.tv_budget);
        rb1 = (RadioButton) findViewById(R.id.rb_rutina);
        rb2 = (RadioButton) findViewById(R.id.rb_especiales);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        presupuesto = "";

        if(!list.equals("")){
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    UpdateBudget(i);
                    String cadena = FormatoPresupuesto(presupuesto);
                    tv_budget.setText(cadena);
                }
            });
        }//if

    }

    public void SelectRoutine(View view){
        String[] tipoLista = list.split("-----");
        String[] items = new String[0];
        if(rb1.isChecked()){
            items = tipoLista[0].split("---");
        }else if(rb2.isChecked()){
            items = tipoLista[1].split("---");
        }

        lista = new String[items.length/5];
        cadenas = new String[items.length/5];
        String linea = "";
        int j, posVector = 0, control = 0;

        for(j=0;j<items.length;j++){
            if(control==0){
                cadenas[posVector] = items[j];
            }
            if((control%5)==0 && control>0){
                //agregamos linea a DB
                lista[posVector] = linea;
                control=0;
                linea = "";
                posVector++;
                j--;
            }else{
                linea = linea+items[j]+"---";
                control++;
            }
        }
        lista[posVector] = linea;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_pruebas, cadenas);
        lv1.setAdapter(adapter);
    }

    public void BudgetCopy(View view){
        if(!presupuesto.equals("")){
            ClipData data = ClipData.newPlainText("texto", tv_budget.getText().toString());
            clipboardManager.setPrimaryClip(data);
            Toast.makeText(this,"Copiado", Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdateBudget(int seleccion){
        String[] listaActualizada = presupuesto.split("---");
        String pruebaSeleccionada = cadenas[seleccion];
        String actualizacionLista = "";
        //buscar en cadenas el elemento seleccion
        if(!presupuesto.equals("")) {
            boolean band = false;
            for (int y = 0; y < listaActualizada.length; y++) {
                if (y == 0 || (y % 2) == 0) {
                    if (pruebaSeleccionada.equals(listaActualizada[y]) && !band) {
                        //eliminar prueba
                        band = true;
                    }else{
                        actualizacionLista = actualizacionLista + listaActualizada[y]+
                                "---" + listaActualizada[y+1] + "---";
                    }
                }
            }
            if(!band){
                actualizacionLista = actualizacionLista + pruebaSeleccionada + "---" +
                        buscarPrecio(pruebaSeleccionada) + "---";
            }
        }else{
            actualizacionLista = pruebaSeleccionada + "---" + buscarPrecio(pruebaSeleccionada) + "---";
        }
        presupuesto = actualizacionLista;
    }

    public String buscarPrecio(String prueba){
        String precio = "0";
        for(int c=0;c< lista.length;c++){
            String[] itemsLinea = lista[c].split("---");
            if(itemsLinea[0].equals(prueba)){
                precio = itemsLinea[2];
                c = lista.length;
            }
        }
        return precio;
    }
    public String FormatoPresupuesto(String formatear){
        if(formatear.equals("")){
            return "Seleccione las pruebas...";
        }
        String definitivo = "Presupuesto:\n";
        String[] itemsBudget = formatear.split("---");
        float total = 0;
        for(int c=0;c< itemsBudget.length;c++){
            if(c==0 || (c%2)==0){
                definitivo = definitivo + itemsBudget[c]+"\t ";
            }else{
                float aux = Float.parseFloat(itemsBudget[c]);
                total = total + aux;
                definitivo = definitivo + String.format("%.2f",aux)+"\n";
            }
        }
        if(total > 0){
            definitivo = definitivo +"\n"+"Total: "+String.format("%.2f",total)+"$";
        }
        return definitivo;
    }

    public void BorrarPresupuesto(View view){
        presupuesto = "";
        tv_budget.setText("Seleccione las pruebas...");
    }
}