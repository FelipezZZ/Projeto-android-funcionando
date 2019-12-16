package com.example.anamnese;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements RecyclerViewAdapter.OnNoteListenner {

    //LISTAS
    //LISTA DE PERGUNTAS
    List<Pergunta> perguntas = new ArrayList<Pergunta>() {
        {
            add(new Pergunta(3, "1-Tive dificuldade de me acalmar", "0", "1", "2", "3"));
            add(new Pergunta(1, "2-Minha boca ficou seca", "0", "1", "2", "3"));
            add(new Pergunta(2, "3-Nao tive nenhum sentimento positivo", "0", "1", "2", "3"));
            add(new Pergunta(1, "4-Em alguns momentos tive dificuldade de respirar (chiado e falta de ar sem esforço físico)", "0", "1", "2", "3"));
            add(new Pergunta(2, "5-Não consegui ter iniciativa para fazer as coisas", "0", "1", "2", "3"));
            add(new Pergunta(3, "6-Exagerei intencionalmente ao reagir a situações", "0", "1", "2", "3"));
            add(new Pergunta(1, "7-Tive tremedeira (por exemplo, nas mãos)", "0", "1", "2", "3"));
            add(new Pergunta(3, "8-Senti que estava sempre nervoso(a)", "0", "1", "2", "3"));
            add(new Pergunta(1, "9-Me preocupei com situações em que poderia entrar em pânico e parecer ridículo(a)", "0", "1", "2", "3"));
            add(new Pergunta(2, "10-Senti que não tinha vontade de nada", "0", "1", "2", "3"));
            add(new Pergunta(3, "11-Me senti inquieto(a)", "0", "1", "2", "3"));
            add(new Pergunta(3, "12-Tive dificuldade de relaxar", "0", "1", "2", "3"));
            add(new Pergunta(2, "13-Me senti deprimido e sem motivação", "0", "1", "2", "3"));
            add(new Pergunta(3, "14-Eu não conseguia tolerar as coisas que me impediam de continuar a fazer o que estava realizando", "0", "1", "2", "3"));
            add(new Pergunta(1, "15-Eu senti que ia entrar em pânico", "0", "1", "2", "3"));
            add(new Pergunta(2, "16-Nada me deixou entusiasmado", "0", "1", "2", "3"));
            add(new Pergunta(2, "17-Eu senti que era uma pessoa sem valor", "0", "1", "2", "3"));
            add(new Pergunta(3, "18-Eu senti que estava sendo muito sensível/emotivo", "0", "1", "2", "3"));
            add(new Pergunta(1, "19-Eu percebi uma mudança nos meus batimentos cardíacos embora não estivesse praticando exercício rigoroso (ex. batimento cardíaco acelerado ou irregular)", "0", "1", "2", "3"));
            add(new Pergunta(1, "20-Eu senti medo sem motivo", "0", "1", "2", "3"));
            add(new Pergunta(2, "21-Senti que a vida não tinha sentido", "0", "1", "2", "3"));
        }
    };

    //LISTA DE RESPOSTAS ANSIEDADE
    ArrayList<Integer> ansiedade = new ArrayList<Integer>(){
        {
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
        }
    };

    //LISTA DE RESPOSTAS DEPRESSAO
    ArrayList<Integer> depressao = new ArrayList<Integer>(){
        {
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
        }
    };

    //LISTA DE RESPOSTAS STRESS
    ArrayList<Integer> stress = new ArrayList<Integer>(){
        {
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
            add(4);
        }
    };

    TextView pergunta;
    RadioButton rbResposta0, rbResposta1, rbResposta2, rbResposta3, rb;
    RadioGroup rgRespostas;
    int tipoADS, contador = 0;
    Resposta a, d, s;
    private ArrayList<Integer> mNumeros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pergunta = (TextView) findViewById(R.id.pergunta);
        rbResposta0 = (RadioButton) findViewById(R.id.rbResposta0);
        rbResposta1 = (RadioButton) findViewById(R.id.rbResposta1);
        rbResposta2 = (RadioButton) findViewById(R.id.rbResposta2);
        rbResposta3 = (RadioButton) findViewById(R.id.rbResposta3);

        getNumeros();
        carregarPergunta();
    }

    //COLOCA OS NUMEROS NA LISTA PRA SEREM SETADOS NOS "BOTÕES"
    private void getNumeros() {

        mNumeros.add(1);
        mNumeros.add(2);
        mNumeros.add(3);
        mNumeros.add(4);
        mNumeros.add(5);
        mNumeros.add(6);
        mNumeros.add(7);
        mNumeros.add(8);
        mNumeros.add(9);
        mNumeros.add(10);
        mNumeros.add(11);
        mNumeros.add(12);
        mNumeros.add(13);
        mNumeros.add(14);
        mNumeros.add(15);
        mNumeros.add(16);
        mNumeros.add(17);
        mNumeros.add(18);
        mNumeros.add(19);
        mNumeros.add(20);
        mNumeros.add(21);

        initRecyclerView();
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNumeros, this, ansiedade, depressao, stress);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(int position) {
        Log.i("OnNoteClick", "clicado chapa");
        if (mNumeros.get(position) == 0) {
            contador = mNumeros.get(position+1);
        } else if(mNumeros.get(position)>= 1){
            contador = mNumeros.get(position);
            contador--;
        }
        Log.i("position",String.valueOf(mNumeros.get(position)));
        onRestart();
    }

    private void carregarPergunta(){

        RadioGroup rgRespostas = (RadioGroup) findViewById(R.id.rgRespostas);

        if (perguntas.size() > 0) {
            Pergunta p = perguntas.get(contador);
            tipoADS = p.getTipoADS();
            pergunta.setText(p.getPergunta());
            List<String> respostas = p.getRespostas();
            rbResposta0.setText(respostas.get(0));
            rbResposta1.setText(respostas.get(1));
            rbResposta2.setText(respostas.get(2));
            rbResposta3.setText(respostas.get(3));
            rgRespostas.clearCheck();
        } else { //acabaram as questões
            //mandar para outra tela
        }


        //MARCA O RADIO BUTTON DE ACORDO COM OQ ESTA NA LISTA DE RESPOSTAS
        if(contador == 0){
            if(stress.get(0) == 0){
                rbResposta0.setChecked(true);
            }
            if(stress.get(0) == 1){
                rbResposta1.setChecked(true);
            }
            if(stress.get(0) == 2){
                rbResposta2.setChecked(true);
            }
            if(stress.get(0) == 3){
                rbResposta3.setChecked(true);
            }
        }
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
            if (contador == 0) {
                adicionarStress();
            }
            if (contador == 1) {
                adicionarAnsiedade();
            }
            if (contador == 2) {
                adicionarDepressao();
            }
            if (contador == 3) {
                adicionarAnsiedade();
            }
            if (contador == 4) {
                adicionarDepressao();
            }
            if (contador == 5) {
                adicionarStress();
            }
            if (contador == 6) {
                adicionarAnsiedade();
            }
            if (contador == 7) {
                adicionarStress();
            }
            if (contador == 8) {
                adicionarAnsiedade();
            }
            if (contador == 9) {
                adicionarDepressao();
            }
            if (contador == 10) {
                adicionarStress();
            }
            if (contador == 11) {
                adicionarStress();
            }
            if (contador == 12) {
                adicionarDepressao();
            }
            if (contador == 13) {
                adicionarStress();
            }
            if (contador == 14) {
                adicionarAnsiedade();
            }
            if (contador == 15) {
                adicionarDepressao();
            }
            if (contador == 16) {
                adicionarDepressao();
            }
            if (contador == 17) {
                adicionarStress();
            }
            if (contador == 18) {
                adicionarAnsiedade();
            }
            if (contador == 19) {
                adicionarAnsiedade();
            }
            if (contador == 20) {
                adicionarStress();
            }
            if (contador == 21) {
                adicionarDepressao();
            }
        }

        public void adicionarAnsiedade(){
            rgRespostas = (RadioGroup) findViewById(R.id.rgRespostas);
            rb = (RadioButton) findViewById(rgRespostas.getCheckedRadioButtonId());

            if(contador == 1){
                if (rb == rbResposta0){
                    ansiedade.set(0, 0);
                }
                if (rb == rbResposta1){
                    ansiedade.set(0, 1);
                }
                if (rb == rbResposta2){
                    ansiedade.set(0, 2);
                }
                if (rb == rbResposta3){
                    ansiedade.set(0, 3);
                }
                Log.i("ANSIEDADE 0", String.valueOf(ansiedade.get(0)));
            }

            if(contador == 3){
                if (rb == rbResposta0){
                    ansiedade.set(1, 0);
                }
                if (rb == rbResposta1){
                    ansiedade.set(1, 1);
                }
                if (rb == rbResposta2){
                    ansiedade.set(1, 2);
                }
                if (rb == rbResposta3){
                    ansiedade.set(1, 3);
                }
                Log.i("ANSIEDADE 1", String.valueOf(ansiedade.get(1)));
            }

            if(contador == 6){
                if (rb == rbResposta0){
                    ansiedade.set(2, 0);
                }
                if (rb == rbResposta1){
                    ansiedade.set(2, 1);
                }
                if (rb == rbResposta2){
                    ansiedade.set(2, 2);
                }
                if (rb == rbResposta3){
                    ansiedade.set(2, 3);
                }
                Log.i("ANSIEDADE 2", String.valueOf(ansiedade.get(2)));
            }

            if(contador == 8){
                if (rb == rbResposta0){
                    ansiedade.set(3, 0);
                }
                if (rb == rbResposta1){
                    ansiedade.set(3, 1);
                }
                if (rb == rbResposta2){
                    ansiedade.set(3, 2);
                }
                if (rb == rbResposta3){
                    ansiedade.set(3, 3);
                }
                Log.i("ANSIEDADE 3", String.valueOf(ansiedade.get(3)));
            }

            if(contador == 14){
                if (rb == rbResposta0){
                    ansiedade.set(4, 0);
                }
                if (rb == rbResposta1){
                    ansiedade.set(4, 1);
                }
                if (rb == rbResposta2){
                    ansiedade.set(4, 2);
                }
                if (rb == rbResposta3){
                    ansiedade.set(4, 3);
                }
                Log.i("ANSIEDADE 4", String.valueOf(ansiedade.get(4)));
            }

            if(contador == 18){
                if (rb == rbResposta0){
                    ansiedade.set(5, 0);
                }
                if (rb == rbResposta1){
                    ansiedade.set(5, 1);
                }
                if (rb == rbResposta2){
                    ansiedade.set(5, 2);
                }
                if (rb == rbResposta3){
                    ansiedade.set(5, 3);
                }
                Log.i("ANSIEDADE 5", String.valueOf(ansiedade.get(5)));
            }

            if(contador == 19){
                if (rb == rbResposta0){
                    ansiedade.set(6, 0);
                }
                if (rb == rbResposta1){
                    ansiedade.set(6, 1);
                }
                if (rb == rbResposta2){
                    ansiedade.set(6, 2);
                }
                if (rb == rbResposta3){
                    ansiedade.set(6, 3);
                }
                Log.i("ANSIEDADE 6", String.valueOf(ansiedade.get(6)));
            }
        }

        public void adicionarDepressao(){
            rgRespostas = (RadioGroup) findViewById(R.id.rgRespostas);
            rb = (RadioButton) findViewById(rgRespostas.getCheckedRadioButtonId());

            if(contador == 2){
                if (rb == rbResposta0){
                    depressao.set(0, 0);
                }
                if (rb == rbResposta1){
                    depressao.set(0, 1);
                }
                if (rb == rbResposta2){
                    depressao.set(0, 2);
                }
                if (rb == rbResposta3){
                    depressao.set(0, 3);
                }
                Log.i("DEPRESSAO 0", String.valueOf(ansiedade.get(0)));
            }

            if(contador == 4){
                if (rb == rbResposta0){
                    depressao.set(1, 0);
                }
                if (rb == rbResposta1){
                    depressao.set(1, 1);
                }
                if (rb == rbResposta2){
                    depressao.set(1, 2);
                }
                if (rb == rbResposta3){
                    depressao.set(1, 3);
                }
                Log.i("DEPRESSAO 1", String.valueOf(ansiedade.get(1)));
            }

            if(contador == 9){
                if (rb == rbResposta0){
                    depressao.set(2, 0);
                }
                if (rb == rbResposta1){
                    depressao.set(2, 1);
                }
                if (rb == rbResposta2){
                    depressao.set(2, 2);
                }
                if (rb == rbResposta3){
                    depressao.set(2, 3);
                }
                Log.i("DEPRESSAO 2", String.valueOf(ansiedade.get(2)));
            }

            if(contador == 12){
                if (rb == rbResposta0){
                    depressao.set(3, 0);
                }
                if (rb == rbResposta1){
                    depressao.set(3, 1);
                }
                if (rb == rbResposta2){
                    depressao.set(3, 2);
                }
                if (rb == rbResposta3){
                    depressao.set(3, 3);
                }
                Log.i("DEPRESSAO 3", String.valueOf(ansiedade.get(3)));
            }

            if(contador == 15){
                if (rb == rbResposta0){
                    depressao.set(4, 0);
                }
                if (rb == rbResposta1){
                    depressao.set(4, 1);
                }
                if (rb == rbResposta2){
                    depressao.set(4, 2);
                }
                if (rb == rbResposta3){
                    depressao.set(4, 3);
                }
                Log.i("DEPRESSAO 4", String.valueOf(ansiedade.get(4)));
            }

            if(contador == 16){
                if (rb == rbResposta0){
                    depressao.set(5, 0);
                }
                if (rb == rbResposta1){
                    depressao.set(5, 1);
                }
                if (rb == rbResposta2){
                    depressao.set(5, 2);
                }
                if (rb == rbResposta3){
                    depressao.set(5, 3);
                }
                Log.i("DEPRESSAO 5", String.valueOf(ansiedade.get(5)));
            }

            if(contador == 20){
                if (rb == rbResposta0){
                    depressao.set(6, 0);
                }
                if (rb == rbResposta1){
                    depressao.set(6, 1);
                }
                if (rb == rbResposta2){
                    depressao.set(6, 2);
                }
                if (rb == rbResposta3){
                    depressao.set(6, 3);
                }
                Log.i("DEPRESSAO 6", String.valueOf(ansiedade.get(6)));
            }
        }

        public void adicionarStress(){
            rgRespostas = (RadioGroup) findViewById(R.id.rgRespostas);
            rb = (RadioButton) findViewById(rgRespostas.getCheckedRadioButtonId());

            if(contador == 0){
                if (rb == rbResposta0){
                    stress.set(0, 0);
                }
                if (rb == rbResposta1){
                    stress.set(0, 1);
                }
                if (rb == rbResposta2){
                    stress.set(0, 2);
                }
                if (rb == rbResposta3){
                    stress.set(0, 3);
                }
                Log.i("STRESS 0", String.valueOf(ansiedade.get(0)));
            }

            if(contador == 5){
                if (rb == rbResposta0){
                    stress.set(1, 0);
                }
                if (rb == rbResposta1){
                    stress.set(1, 1);
                }
                if (rb == rbResposta2){
                    stress.set(1, 2);
                }
                if (rb == rbResposta3){
                    stress.set(1, 3);
                }
                Log.i("STRESS 1", String.valueOf(ansiedade.get(1)));
            }

            if(contador == 7){
                if (rb == rbResposta0){
                    stress.set(2, 0);
                }
                if (rb == rbResposta1){
                    stress.set(2, 1);
                }
                if (rb == rbResposta2){
                    stress.set(2, 2);
                }
                if (rb == rbResposta3){
                    stress.set(2, 3);
                }
                Log.i("STRESS 2", String.valueOf(ansiedade.get(2)));
            }

            if(contador == 10){
                if (rb == rbResposta0){
                    stress.set(3, 0);
                }
                if (rb == rbResposta1){
                    stress.set(3, 1);
                }
                if (rb == rbResposta2){
                    stress.set(3, 2);
                }
                if (rb == rbResposta3){
                    stress.set(3, 3);
                }
                Log.i("STRESS 3", String.valueOf(ansiedade.get(3)));
            }

            if(contador == 11){
                if (rb == rbResposta0){
                    stress.set(4, 0);
                }
                if (rb == rbResposta1){
                    stress.set(4, 1);
                }
                if (rb == rbResposta2){
                    stress.set(4, 2);
                }
                if (rb == rbResposta3){
                    stress.set(4, 3);
                }
                Log.i("STRESS 4", String.valueOf(ansiedade.get(4)));
            }

            if(contador == 13){
                if (rb == rbResposta0){
                    stress.set(5, 0);
                }
                if (rb == rbResposta1){
                    stress.set(5, 1);
                }
                if (rb == rbResposta2){
                    stress.set(5, 2);
                }
                if (rb == rbResposta3){
                    stress.set(5, 3);
                }
                Log.i("STRESS 5", String.valueOf(ansiedade.get(5)));
            }

            if(contador == 17){
                if (rb == rbResposta0){
                    stress.set(6, 0);
                }
                if (rb == rbResposta1){
                    stress.set(6, 1);
                }
                if (rb == rbResposta2){
                    stress.set(6, 2);
                }
                if (rb == rbResposta3){
                    stress.set(6, 3);
                }
                Log.i("STRESS 6", String.valueOf(ansiedade.get(6)));
            }
        }

        @Override
        protected void onRestart () {
            super.onRestart();
            //if(contador <= 0){
            //   contador = 1;
            // }else {
            initRecyclerView();
            carregarPergunta();
        }
    }


