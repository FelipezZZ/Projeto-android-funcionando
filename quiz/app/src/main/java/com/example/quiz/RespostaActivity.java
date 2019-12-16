package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class RespostaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta);
        getSupportActionBar().hide();

        ImageView imgResposta = (ImageView)findViewById(R.id.imgResposta);
        TextView resposta = (TextView)findViewById(R.id.resposta);

        Intent intent = getIntent();
        int pontos = intent.getIntExtra("pontos", 0);
        boolean acertou = intent.getBooleanExtra("acertou", true);
        if(acertou){
            imgResposta.setImageResource(R.drawable.acertou);
            resposta.setText("Acertou! Pontos: " + pontos);
        }
        else{
            imgResposta.setImageResource(R.drawable.errou);
            resposta.setText("Errou! Pontos: " + pontos);;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        thread.start();
    }
}
