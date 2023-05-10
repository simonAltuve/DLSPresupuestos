package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class AyudaActivity extends AppCompatActivity {

    TextView tv_texto;
    Button b_copiarEnlace;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        String texto = "\tLa aplicación presupuestos esta enfocada en atender con tiempos cortos de "+
                "respuesta, las solicitudes de información de los productos o servicios ofertados por"
                +" su empresa, tomando como punto de partida la necesidad de sus clientes de recibir "
                +"contestación inmediata.\n\n\tPresupuestos en el modulo de configuración trae por"
                +" defecto la contraseña 1234, la cual puede ser modificada en el mismo módulo. "
                +"Después de ingresar la contraseña se carga la pantalla donde puede crear, "
                +"modificar o borrar productos de la lista de precios, en la misma está la "
                +"opción de cargar una lista de precios desde un archivo .txt con el siguiente "
                +"formato:\n\nNOMBRE---PRECIO DE COSTO---PRECIO DE VENTA---TIEMPO DE ENTREGA---"
                +"TIPO DE PRODUCTO---\n\n"
                +"\tCada item de la linea va separado por tres guiones(---), de no mantenerse el "
                +"formato no se ejecutará la carga masiva de la lista de precios. "
                +"En el módulo de generar presupuesto podrá crear el presupuesto solicitado por el "
                +"cliente y compartirlo como un texto por cualquier aplicación del teléfono."
                +"\n\n\t En el siguiente enlace podrá descargar un archivo .txt con el ejemplo"
                +" de la lista de precios para la carga masiva.";
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        tv_texto = (TextView) findViewById(R.id.tv_texto);
        b_copiarEnlace = (Button) findViewById(R.id.b_copiarEnlace);
        tv_texto.setText(texto);

        b_copiarEnlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enlace = "https://drive.google.com/file/d/1nwJAhrEcDO5E_L54X6MTa6inURtwJ2DC/view?usp=share_link";
                ClipData data = ClipData.newPlainText("enlace", enlace);
                clipboardManager.setPrimaryClip(data);
                //Toast.makeText(this,"Copiado", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.tv_texto),"Enlace copiado",Snackbar.LENGTH_LONG).show();
            }
        });
    }
    public void irPrincipal(View view){
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }
}