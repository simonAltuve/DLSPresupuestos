package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button b_actualizarPrecios;
    private Button b_generarPresupuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Poner icono en Action Bar
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        b_actualizarPrecios = (Button) findViewById(R.id.b_actualizarPrecios);
        b_generarPresupuesto = (Button) findViewById(R.id.b_actualizarPrecios);

    }
    public void actualizarPrecios(View view){
        //enviar a actualizar
        Intent log = new Intent(this, LogActivity.class);
        startActivity(log);
    }
    public void generarPresupuesto(View view){
        //enviar a generar presupuesto
        Intent gen = new Intent(this, GenerateBudgetActivity.class);
        startActivity(gen);
    }
}