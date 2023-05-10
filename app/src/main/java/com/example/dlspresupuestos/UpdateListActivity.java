package com.example.dlspresupuestos;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class UpdateListActivity extends AppCompatActivity {

    private FloatingActionButton cargaArchivo;
    private MaterialTextView tv_nombreArchivo;
    private Button b_buscarProducto;
    private int REQUEST_CODE = 200;
    private String archivoCarga,nombreProducto,posicionProducto;
    String list;
    SharedPreferences preferences;

    TextInputEditText txt_nombreProducto,txt_costoProducto,txt_precioProducto;
    TextInputEditText txt_tEntregaProducto, txt_tipoProducto;
    TextInputLayout til_nombreProducto, til_costoProducto, til_precioProducto;
    TextInputLayout til_tEntregaProducto, til_tipoProducto;
    Button b_eliminar;
    private DecimalFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list);

        preferences = getSharedPreferences("DB", Context.MODE_PRIVATE);
        list = preferences.getString("list","");
        cargaArchivo = (FloatingActionButton) findViewById(R.id.b_cargaArchivo);
        tv_nombreArchivo = (MaterialTextView) findViewById(R.id.tv_nombreArchivo);
        txt_nombreProducto = (TextInputEditText) findViewById(R.id.txt_nombreProducto);
        txt_costoProducto = (TextInputEditText) findViewById(R.id.txt_costoProducto);
        txt_precioProducto = (TextInputEditText) findViewById(R.id.txt_precioProducto);
        txt_tEntregaProducto = (TextInputEditText) findViewById(R.id.txt_tEntregaProducto);
        txt_tipoProducto = (TextInputEditText) findViewById(R.id.txt_tipoProducto);
        til_nombreProducto = (TextInputLayout) findViewById(R.id.til_nombreProducto);
        til_costoProducto = (TextInputLayout) findViewById(R.id.til_costoProducto);
        til_precioProducto = (TextInputLayout) findViewById(R.id.til_precioProducto);
        til_tEntregaProducto = (TextInputLayout) findViewById(R.id.til_tEntregaProducto);
        til_tipoProducto = (TextInputLayout) findViewById(R.id.til_tipoProducto);
        archivoCarga = getIntent().getStringExtra("archivoNuevo");
        nombreProducto = getIntent().getStringExtra("nombreProducto");
        posicionProducto = getIntent().getStringExtra("posicionProducto");
        b_buscarProducto = (Button) findViewById(R.id.b_buscarProducto);
        b_eliminar = (Button) findViewById(R.id.b_eliminar);
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        df = new DecimalFormat("0.00",simbolo);

        if(!archivoCarga.equals("")){
            tv_nombreArchivo.setText(archivoCarga);
            tv_nombreArchivo.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(this,nombreProducto+"-Copiado", Toast.LENGTH_LONG).show();
        if(!nombreProducto.equals("") && !list.equals("")){
            //Toast.makeText(this,nombreProducto+"-recibido "+posicionProducto, Toast.LENGTH_LONG).show();
            cargarProducto(nombreProducto,posicionProducto);
        }
        //click en el icono de la lupa
        b_buscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirecciono a buscar el producto
                Intent bp = new Intent(UpdateListActivity.this, BuscarProductoActivity.class);
                startActivity(bp);
            }
        });
        //formato con dos decimales
        txt_costoProducto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    double c = Double.valueOf(txt_costoProducto.getText().toString());
                    txt_costoProducto.setText(df.format(c));
                }
            }
        });
        //formato con dos decimales
        txt_precioProducto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    double c = Double.valueOf(txt_precioProducto.getText().toString());
                    txt_precioProducto.setText(df.format(c));
                }
            }
        });
        //click en el float action button
        cargaArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(UpdateListActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                   //Tiene permisos para trabajar con la memoria
                    //Intent la = new Intent(UpdateListActivity.this, ListaArchivosActivity.class);
                    //startActivity(la);
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("text/plain");
                    chooseFile = Intent.createChooser(chooseFile,"Seleccione un archivo");
                    takeTXT.launch(chooseFile);
                }else{
                    requestStoragePermission();
                }
            }
        });
        txt_nombreProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_nombreProducto.setErrorEnabled(false);
            }
        });
        txt_costoProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_costoProducto.setErrorEnabled(false);
            }
        });
        txt_precioProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_precioProducto.setErrorEnabled(false);
            }
        });
        txt_tEntregaProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_tEntregaProducto.setErrorEnabled(false);
            }
        });
        txt_tipoProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_tipoProducto.setErrorEnabled(false);
            }
        });
        b_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombreProducto.equals("") && !posicionProducto.equals("")){
                    eliminarProducto(nombreProducto, posicionProducto);
                    new MaterialAlertDialogBuilder(UpdateListActivity.this)
                            .setTitle("Operación Exitosa")
                            .setMessage("Producto eliminado satisfactoriamente.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    overridePendingTransition(0,0);
                                    Intent intent = getIntent();
                                    intent.putExtra("archivoNuevo", "");
                                    intent.putExtra("posicionProducto", "");
                                    intent.putExtra("nombreProducto", "");
                                    startActivity(intent);
                                    overridePendingTransition(0,0);
                                    limpiarFormulario();
                                }
                            })
                            .create().show();
                }
            }
        });
    }

    ActivityResultLauncher<Intent> takeTXT = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode()==RESULT_OK && result.getData()!=null){
                    borrarLista();
                    Uri uri = result.getData().getData();
                    BufferedReader reader = null;
                    try{
                        InputStream in = getContentResolver().openInputStream(uri);
                        reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        StringBuilder builder = new StringBuilder();
                        while((line=reader.readLine())!=null){
                            builder.append(line);
                        }
                        new MaterialAlertDialogBuilder(UpdateListActivity.this)
                                .setTitle("Confirmar")
                                .setMessage("¿Está seguro que desea cargar el archivo seleccionado?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        limpiarFormulario();
                                        dialogInterface.dismiss();
                                        String texto = builder.toString();
                                        texto = texto.replace("\n", "");
                                        texto = limpiarArchivo(texto);
                                        guardarLista(texto);
                                        dialogInterface.dismiss();
                                        new MaterialAlertDialogBuilder(UpdateListActivity.this)
                                                .setTitle("Operación Exitosa")
                                                .setMessage("Lista modificada exitosamente")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).create().show();
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
    });

    //funcion que elimina el producto seleccionado de la lista
    private void eliminarProducto(String nombre, String posicion) {
        int p = Integer.parseInt(posicion)*5;
        String[] cadenas = list.split("---");
        String nuevaLista = "";
        for(int i=0;i<cadenas.length;i++){
            if(i<p || i>(p+4)){
                nuevaLista = nuevaLista+cadenas[i]+"---";
            }
        }
        guardarLista(nuevaLista);
    }

    //funcion que se ejecuta al presionar el boton actualizar de la activity
    public void actualizar(View view){
        if(!archivoCarga.equals("")){
            File ruta = Environment.getExternalStorageDirectory();
            File f = new File(ruta.getAbsolutePath()+"/Download/"+archivoCarga);

            try {
                BufferedReader buffer = new BufferedReader(new FileReader(f));
                String archivo = "";
                String linea = "";
                int i = 0;

                while ((linea = buffer.readLine())!=null){
                    archivo = archivo + linea;
                    i++;
                }

                archivo = archivo.replace("\n", "");
                archivo = limpiarArchivo(archivo);

                //Almacena la lista en el shared preferences file
                guardarLista(archivo);
                new AlertDialog.Builder(this)
                        .setTitle("Operación Exitosa")
                        .setMessage("El archivo fue cargado exitosamente.")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent main = new Intent(UpdateListActivity.this, MainActivity.class);
                                startActivity(main);
                            }
                        })
                        .create().show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(archivoCarga.equals("")){
            actualizarProducto(posicionProducto);
        }
    }

    //funcion que toma informacion del formulario y actualiza o crea un nuevo producto
    private void actualizarProducto(String posicionProducto) {
        boolean error = false;
        if(txt_nombreProducto.getText().toString().equals("")){
            til_nombreProducto.setError("Obligatorio");
            error = true;
        }
        if(txt_costoProducto.getText().toString().equals("")){
            til_costoProducto.setError("Obligatorio");
            error = true;
        }
        if(txt_precioProducto.getText().toString().equals("")){
            til_precioProducto.setError("Obligatorio");
            error = true;
        }
        if(txt_tEntregaProducto.getText().toString().equals("")){
            til_tEntregaProducto.setError("Obligatorio");
            error = true;
        }
        if(txt_tipoProducto.getText().toString().equals("")){
            til_tipoProducto.setError("Obligatorio");
            error = true;
        }

        if(posicionProducto.equals("") && !error){
            //crear un nuevo producto
            String lista = list+txt_nombreProducto.getText().toString()+"---"
                    +txt_costoProducto.getText().toString()+"---"
                    +txt_precioProducto.getText().toString()+"---"
                    +txt_tEntregaProducto.getText().toString()+"---"
                    +txt_tipoProducto.getText().toString()+"---";
            guardarLista(lista);
            new MaterialAlertDialogBuilder(UpdateListActivity.this)
                    .setTitle("Operación Exitosa")
                    .setMessage("Producto agregado satisfactoriamente.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            limpiarFormulario();
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        if(!posicionProducto.equals("") && !error){
            //actualiza producto existente
            String[] cadenas = list.split("---");
            int p = Integer.parseInt(posicionProducto)*5;
            cadenas[p] = txt_nombreProducto.getText().toString();
            cadenas[p+1] = txt_costoProducto.getText().toString();
            cadenas[p+2] = txt_precioProducto.getText().toString();
            cadenas[p+3] = txt_tEntregaProducto.getText().toString();
            cadenas[p+4] = txt_tipoProducto.getText().toString();
            String nuevaLista = "";
            for(int i=0;i<cadenas.length;i++){
                nuevaLista = nuevaLista+cadenas[i]+"---";
            }
            guardarLista(nuevaLista);
            new MaterialAlertDialogBuilder(UpdateListActivity.this)
                    .setTitle("Operación Exitosa")
                    .setMessage("Producto modificado satisfactoriamente.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //recarga la activity sin que salga en blanco
                            finish();
                            overridePendingTransition(0,0);
                            Intent intent = getIntent();
                            intent.putExtra("archivoNuevo", "");
                            intent.putExtra("posicionProducto", "");
                            intent.putExtra("nombreProducto", "");
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            limpiarFormulario();
                        }
                    })
                    .create().show();
        }
    }

    //funcion que reinicia el formulario de la activity
    public void limpiarFormulario(){
        txt_nombreProducto.setText("");
        txt_costoProducto.setText("0.00");
        txt_precioProducto.setText("0.00");
        txt_tEntregaProducto.setText("");
        txt_tipoProducto.setText("");
    }

    //funcion para solocitar permisos de acceso a almacenamiento interno
    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permiso Necesario")
                    .setMessage("Es necesario los permisos para poder cargar archivo.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UpdateListActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(UpdateListActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }

    //funcion que elimina los caracteres no imprimibles del archivo txt leido
    public String limpiarArchivo(String texto){
        String textoLimpio="";
        for (int i=0;i< texto.length();i++){
            if(((texto.charAt(i)>31 && (texto.charAt(i)<127))&&
                    (texto.charAt(i)!=34 || texto.charAt(i)!=39))){
                textoLimpio = textoLimpio + texto.charAt(i);
            }
        }
        return textoLimpio;
    }

    //funcion que carga los valores del producto seleccionado al formulario
    public void cargarProducto(String nombreProducto, String posicion){
        String [] cadenas = list.split("---");
        String texto = "";
        int p = Integer.parseInt(posicion)*5;
        double costo = Double.valueOf(cadenas[p+1]);
        double precio = Double.valueOf(cadenas[p+2]);
        if(cadenas[p].equals(nombreProducto)){
            txt_nombreProducto.setText(nombreProducto);
            txt_costoProducto.setText(df.format(costo));
            txt_precioProducto.setText(df.format(precio));
            txt_tEntregaProducto.setText(cadenas[p+3]);
            txt_tipoProducto.setText(cadenas[p+4]);
        }
    }

    public void irPrincipal(View view){
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }
    //funcion que guarda la lista de productos enviada por parametros
    public void guardarLista(String lista){
        SharedPreferences.Editor obj_editor = preferences.edit();
        obj_editor.putString("list", lista);
        obj_editor.commit();
    }
    public void borrarLista(){
        SharedPreferences.Editor obj_editor = preferences.edit();
        obj_editor.putString("list", "");
        obj_editor.commit();
    }

}