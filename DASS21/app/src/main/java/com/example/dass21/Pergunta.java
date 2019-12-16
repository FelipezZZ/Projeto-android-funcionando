package com.example.dass21;

import java.util.ArrayList;
import java.util.List;

public class Pergunta {

    private int tipoADS;
    private String pergunta;
    private int res;


    public Pergunta(int tipoADS, String pergunta, int res){
        this.tipoADS = tipoADS;
        this.pergunta = pergunta;
        this.res = res;
    }

    public int getTipoADS(){
        return this.tipoADS;
    }

    public String getPergunta(){
        return this.pergunta;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
