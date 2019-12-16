package com.example.anamnese;

import java.util.ArrayList;
import java.util.List;

public class Pergunta {

    private String pergunta;
    private List<String> respostas = new ArrayList<>();
    private int tipoADS;

    public Pergunta(int tipoADS, String pergunta, String... respostas){
        this.tipoADS = tipoADS;
        this.pergunta = pergunta;
        this.respostas.add(respostas[0]);
        this.respostas.add(respostas[1]);
        this.respostas.add(respostas[2]);
        this.respostas.add(respostas[3]);
    }

    public int getTipoADS(){
        return this.tipoADS;
    }

    public String getPergunta(){
        return this.pergunta;
    }

    public List<String> getRespostas(){
        return this.respostas;
    }

}
