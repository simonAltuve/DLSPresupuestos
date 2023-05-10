package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CambiarContraseniaActivity extends AppCompatActivity {

    private TextInputLayout til_contraseniaActual, til_contraseniaNueva, til_contraseniaConfirma;
    private TextInputEditText txt_conActual, txt_conNueva, txt_conConfirma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasenia);

        til_contraseniaActual = (TextInputLayout) findViewById(R.id.til_contraseniaActual);
        til_contraseniaNueva = (TextInputLayout) findViewById(R.id.til_contraseniaNueva);
        til_contraseniaConfirma = (TextInputLayout) findViewById(R.id.til_contraseniaConfirma);
        txt_conActual = (TextInputEditText) findViewById(R.id.txt_conActual);
        txt_conNueva = (TextInputEditText) findViewById(R.id.txt_conNueva);
        txt_conConfirma = (TextInputEditText) findViewById(R.id.txt_conConfirma);

        txt_conActual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_contraseniaActual.setErrorEnabled(false);
            }
        });
        txt_conNueva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_contraseniaNueva.setErrorEnabled(false);
            }
        });
        txt_conConfirma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_contraseniaConfirma.setErrorEnabled(false);
            }
        });

    }

    public void cambiarContrasenia(View view){
        String nuevaContrasenia = txt_conNueva.getText().toString();
        String conActualIngresada = txt_conActual.getText().toString();
        String confirmaConNueva = txt_conConfirma.getText().toString();
        int errorLongitud = 0;

        SharedPreferences preferencias = getSharedPreferences("DB", Context.MODE_PRIVATE);
        String conAlmacenada = preferencias.getString("contrasenia","");
        //si la contraseña almacenada no se ha modificado se toma 1234
        //de lo contrario se verifica con la contraseña almacenada
        if(conActualIngresada.length()<4){
            til_contraseniaActual.setError("Deben ser 4 digitos.");
            errorLongitud = 1;
        }else if(nuevaContrasenia.length()<4){
            til_contraseniaNueva.setError("Deben ser 4 digitos");
            errorLongitud = 2;
        }else if(confirmaConNueva.length()<4){
            til_contraseniaConfirma.setError("Deben ser 4 digitos");
            errorLongitud = 3;
        }
        if(errorLongitud==0){
            if(nuevaContrasenia.equals(confirmaConNueva)) {
                if((conAlmacenada.equals("") && conActualIngresada.equals("1234")) ||
                    (!conAlmacenada.equals("") && conActualIngresada.equals(conAlmacenada))){
                    //Almacena la lista en el shared preferences file
                    SharedPreferences.Editor obj_editor = preferencias.edit();
                    obj_editor.putString("contrasenia", nuevaContrasenia);
                    obj_editor.commit();
                    new MaterialAlertDialogBuilder(CambiarContraseniaActivity.this)
                            .setTitle("Operación Exitosa")
                            .setMessage("Contraseña modificada satisfactoriamente.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent upd = new Intent(CambiarContraseniaActivity.this, LogActivity.class);
                                    startActivity(upd);
                                }
                            })
                            .create().show();
                    //Toast.makeText(this, "Contraseña modificada.",Toast.LENGTH_SHORT).show();
                }else{
                    til_contraseniaActual.setError("Contraseña incorrecta");
                }
            }else{
                til_contraseniaConfirma.setError("Contraseñas no coinciden.......");
            }
        }

    }
    public void irPrincipal(View view){
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }

}