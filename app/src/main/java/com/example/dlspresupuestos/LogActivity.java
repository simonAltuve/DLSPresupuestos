package com.example.dlspresupuestos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LogActivity extends AppCompatActivity {

    private TextView txt_user;
    private TextView txt_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        txt_user = (TextView) findViewById(R.id.txt_user);
        txt_pass = (TextView) findViewById(R.id.txt_pass);
    }

    public void login(View view){
        String user = txt_user.getText().toString();
        String pass = txt_pass.getText().toString();
        if(user.equals("root") && pass.equals("1415")){
            Intent upd = new Intent(this, UpdateListActivity.class);
            startActivity(upd);
        }else{
            Toast.makeText(this, "Usuario o contrasenia incorrecta",Toast.LENGTH_LONG).show();
        }

    }
}