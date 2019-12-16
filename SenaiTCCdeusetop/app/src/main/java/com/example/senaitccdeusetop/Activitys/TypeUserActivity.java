package com.example.senaitccdeusetop.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.senaitccdeusetop.R;

public class TypeUserActivity extends AppCompatActivity {

    Button btnPaciente;
    Button btnEstagiario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_user);

        btnPaciente = findViewById(R.id.btn_usuario);
        btnEstagiario = findViewById(R.id.btn_estagiario);

        btnPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeUserActivity.this, CadastroActivity.class);
                intent.putExtra("tipoUsuario", "paciente");
                startActivity(intent);
            }
        });

        btnEstagiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeUserActivity.this, CadastroActivity.class);
                intent.putExtra("tipoUsuario", "estagiario");
                startActivity(intent);
            }
        });
    }
}
