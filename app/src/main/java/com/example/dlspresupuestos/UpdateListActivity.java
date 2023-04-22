package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class UpdateListActivity extends AppCompatActivity {

    private EditText txt_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list);

        txt_list = (EditText) findViewById(R.id.txt_list);
        SharedPreferences preferences = getSharedPreferences("DB", Context.MODE_PRIVATE);
    }

    public void actualizar(View view){
        String list = txt_list.getText().toString();
        list = list.replace("\n", "");


        //Almacena la lista en el shared preferences file
        SharedPreferences preferencias = getSharedPreferences("DB", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("list", list);
        obj_editor.commit();
        Toast.makeText(this, "Lista Actualizada.",Toast.LENGTH_SHORT).show();

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
    int requestcode = 1;

    public void onActivityResult(int requestcode, int resulCode, Intent data){

        super.onActivityResult(requestcode,resulCode,data);
        Context context = getApplicationContext();
        if(requestcode == requestcode && resulCode == Activity.RESULT_OK){
            if(data == null){
                return;
            }
            Uri uri = data.getData();
            Toast.makeText(context, uri.getPath(),Toast.LENGTH_LONG).show();

        }
    }

    public void openFileChooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,requestcode);
    }
}