package com.example.dass21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DASS21Activity extends AppCompatActivity implements RecyclerViewAdapter.OnNoteListenner {

    TextView tvPergunta;
    RadioButton rbResposta0, rbResposta1, rbResposta2, rbResposta3, rb;
    RadioGroup rgRespostas;
    Button btnConcluir;
    int contador = 0;
    List<Pergunta> perguntas;
    private ArrayList<Integer> mNumeros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dass21);

        tvPergunta = findViewById(R.id.tvPergunta);
        rgRespostas = findViewById(R.id.rgRespostas);
        rbResposta0 = findViewById(R.id.rbResposta0);
        rbResposta1 = findViewById(R.id.rbResposta1);
        rbResposta2 = findViewById(R.id.rbResposta2);
        rbResposta3 = findViewById(R.id.rbResposta3);

        btnConcluir = findViewById(R.id.btnConcluir);

        for(int i = 1; i <= 21; i++){
            mNumeros.add(i);
        }

        perguntas = new ArrayList<Pergunta>() {
            {
                add(new Pergunta(3, "Tive dificuldade de me acalmar", 4));
                add(new Pergunta(1, "Minha boca ficou seca", 4));
                add(new Pergunta(2, "Nao tive nenhum sentimento positivo", 4));
                add(new Pergunta(1, "Em alguns momentos tive dificuldade de respirar (chiado e falta de ar sem esforço físico)", 4));
                add(new Pergunta(2, "Não consegui ter iniciativa para fazer as coisas", 4));
                add(new Pergunta(3, "Exagerei intencionalmente ao reagir a situações", 4));
                add(new Pergunta(1, "Tive tremedeira (por exemplo, nas mãos)", 4));
                add(new Pergunta(3, "Senti que estava sempre nervoso(a)", 4));
                add(new Pergunta(1, "Me preocupei com situações em que poderia entrar em pânico e parecer ridículo(a)", 4));
                add(new Pergunta(2, "Senti que não tinha vontade de nada", 4));
                add(new Pergunta(3, "Me senti inquieto(a)", 4));
                add(new Pergunta(3, "Tive dificuldade de relaxar", 4));
                add(new Pergunta(2, "Me senti deprimido e sem motivação", 4));
                add(new Pergunta(3, "Eu não conseguia tolerar as coisas que me impediam de continuar a fazer o que estava realizando", 4));
                add(new Pergunta(1, "Eu senti que ia entrar em pânico", 4));
                add(new Pergunta(2, "Nada me deixou entusiasmado", 4));
                add(new Pergunta(2, "Eu senti que era uma pessoa sem valor", 4));
                add(new Pergunta(3, "Eu senti que estava sendo muito sensível/emotivo", 4));
                add(new Pergunta(1, "Eu percebi uma mudança nos meus batimentos cardíacos embora não estivesse praticando exercício rigoroso (ex. batimento cardíaco acelerado ou irregular)", 4));
                add(new Pergunta(1, "Eu senti medo sem motivo", 4));
                add(new Pergunta(2, "Senti que a vida não tinha sentido", 4));
            }
        };

        initRecyclerView();
        carregarPergunta();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNumeros, this, perguntas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(int position) {
        adicionarResposta();
        contador = position;
        onRestart();
    }

    private void carregarPergunta() {
        tvPergunta.setText(perguntas.get(contador).getPergunta());

        if(perguntas.get(contador).getRes() == 0){
            rbResposta0.setChecked(true);
        }else
        if(perguntas.get(contador).getRes() == 1){
            rbResposta1.setChecked(true);
        }else
        if(perguntas.get(contador).getRes() == 2){
            rbResposta2.setChecked(true);
        }else
        if(perguntas.get(contador).getRes() == 3){
            rbResposta3.setChecked(true);
        }else {
            rgRespostas.clearCheck();
        }

        if(verificaRespondidas()){
            btnConcluir.setVisibility(View.VISIBLE);
        }

        print();
    }

    public void btnConcluirOnClick(View v){
        int a = 0 , d = 0, s = 0;

        adicionarResposta();

        for(int i =0; i <= 20 ; i++){
            if(perguntas.get(i).getTipoADS() == 1){
                a += perguntas.get(i).getRes();
            }else
            if(perguntas.get(i).getTipoADS() == 2){
                d += perguntas.get(i).getRes();
            }else
            if(perguntas.get(i).getTipoADS() == 3){
                s += perguntas.get(i).getRes();
            }
        }

        a = a * 2;
        d = d * 2;
        s = s * 2;

        Log.i("Valor", "A " + a);
        Log.i("Valor", "D " + d);
        Log.i("Valor", "S " + s);

    }

    public void btnPrevOnClick (View v){
        adicionarResposta();

        if (contador <= 0) {
            contador = 0;
        } else {
            contador--;
        }
        onRestart();
    }

    public void btnNextOnClick (View v){
        adicionarResposta();

        if (contador >= 20) {
            contador = 20;
        } else {
            contador++;
        }
        onRestart();
    }

    public void adicionarResposta(){
        perguntas.get(contador).setRes(getRes());
    }

    public int getRes(){
        rgRespostas = findViewById(R.id.rgRespostas);
        rb = findViewById(rgRespostas.getCheckedRadioButtonId());

        if (rb == rbResposta0){
            return 0;
        }else
        if (rb == rbResposta1){
            return 1;
        }else
        if (rb == rbResposta2){
            return 2;
        }else
        if (rb == rbResposta3){
            return 3;
        }

        return 4;
    }

    public boolean verificaRespondidas() {
        int respondidas = 0;


        for (int i = 0; i <= 20; i++) {
            if (perguntas.get(i).getRes() == 4) {

            } else if (perguntas.get(i).getRes() != 4) {
                respondidas++;
            }
        }

        if (respondidas == 21) {
            return true;
        } else {
            return false;
        }

    }

    public void print(){
        for(int i = 0; i <= 20; i++) {
            Log.i("Evento", "Resposta " + i + " " + perguntas.get(i).getRes());
        }
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        initRecyclerView();
        carregarPergunta();
    }
}
