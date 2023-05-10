package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LogActivity extends AppCompatActivity {

    private String contrasenia;
    private TextInputEditText txt_pass;
    private TextInputLayout til_contraseniaAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        SharedPreferences preferences = getSharedPreferences("DB", Context.MODE_PRIVATE);
        contrasenia = preferences.getString("contrasenia","");
        txt_pass = (TextInputEditText) findViewById(R.id.txt_pass);
        til_contraseniaAct = (TextInputLayout) findViewById(R.id.til_contraseniaAct);

        txt_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                til_contraseniaAct.setErrorEnabled(false);
            }
        });

    }

    public void login(View view){
        String pass = txt_pass.getText().toString();
        //Toast.makeText(this, pass,Toast.LENGTH_SHORT).show();
        if((contrasenia.equals("") && pass.equals("1234")) || (!contrasenia.equals("") && contrasenia.equals(pass))){//caso en que se deba tomar contraseña por defecto de la app
            Intent upd = new Intent(this, UpdateListActivity.class);
            upd.putExtra("archivoNuevo", "");
            upd.putExtra("posicionProducto", "");
            upd.putExtra("nombreProducto", "");
            startActivity(upd);
        }else{
            til_contraseniaAct.setError("Contraseña incorrecta.");
        }
    }

    public void cambiarContrasenia(View view){
        Intent cambiarContrasenia = new Intent(this, CambiarContraseniaActivity.class);
        startActivity(cambiarContrasenia);
    }
    public void irPrincipal(View view){
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }
}