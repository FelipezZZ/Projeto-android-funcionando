package com.example.senaitcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.senaitcc.R;

public class TypeUserActivity extends AppCompatActivity {

    Button btnUsuario;
    Button btnEstagiario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_user);

        btnUsuario = findViewById(R.id.btn_usuario);
        btnEstagiario = findViewById(R.id.btn_estagiario);

        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeUserActivity.this, CadastroActivity.class);
                intent.putExtra("type", "usuario");
                startActivity(intent);
            }
        });

        btnEstagiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeUserActivity.this, CadastroActivity.class);
                intent.putExtra("type", "estagiario");
                startActivity(intent);
            }
        });

    }
}
