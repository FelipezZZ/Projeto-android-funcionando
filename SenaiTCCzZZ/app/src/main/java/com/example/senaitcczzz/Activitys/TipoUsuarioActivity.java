package com.example.senaitcczzz.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.senaitcczzz.R;

public class TipoUsuarioActivity extends AppCompatActivity {

    Button btnPaciente;
    Button btnEstagiario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_usuario);

        btnPaciente = findViewById(R.id.btn_paciente);
        btnEstagiario = findViewById(R.id.btn_estagiario);

        btnPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TipoUsuarioActivity.this, CadastroActivity.class);
                intent.putExtra("type", "paciente");
                startActivity(intent);
            }
        });

        btnEstagiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TipoUsuarioActivity.this, CadastroActivity.class);
                intent.putExtra("type", "estagiario");
                startActivity(intent);
            }
        });
    }
}
