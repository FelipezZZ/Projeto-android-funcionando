package com.example.senaitccdeusetop.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.senaitccdeusetop.Chat.ContactsActivity;
import com.example.senaitccdeusetop.Vo.Pergunta;
import com.example.senaitccdeusetop.Vo.Pessoa;
import com.example.senaitccdeusetop.R;
import com.example.senaitccdeusetop.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    int a = 0 , d = 0, s = 0;
    int Pa = 0 , Pd = 0, Ps = 0;

    String parametros;
    Pessoa logado;
    private String tipoUsuario;

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

        FirebaseFirestore.getInstance().collection("/users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        logado = documentSnapshot.toObject(Pessoa.class);
                        verificarTipoUsuario();
                    }
                });

    }

    private void verificarTipoUsuario() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String acao = "verificarTipoUsuario";
                    String cod_pessoa = String.valueOf(logado.getFbcod_pessoa());

                    parametros = "acao="+acao+"&codPessoa="+cod_pessoa;

//                    URL url = new URL("http://192.168.56.1:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                    URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                    //URL url = new URL("http://10.87.202.177:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
//                    URL url = new URL("http://10.87.202.168:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");


                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(parametros);

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String apnd = "", linha = "";

                    while ((linha = br.readLine()) != null)
                        apnd += linha;

                    JSONObject obj = new JSONObject();
                    obj.put("tipoUsuario", apnd);
                    tipoUsuario = obj.getString("tipoUsuario");
                    Log.i("teste", "Tipo Usuario " + tipoUsuario);

                }catch(Exception e){
                    Log.i("teste", e.toString());
                }
            }

        }).start();
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
        Log.i("teste", "OnNoteClick");
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
    }

    public void btnConcluirOnClick(View v){

        adicionarResposta();

        for(int i =0; i <= 20 ; i++){
            if(perguntas.get(i).getTipoADS() == 1){
                Pa += perguntas.get(i).getRes();
            }else
            if(perguntas.get(i).getTipoADS() == 2){
                Pd += perguntas.get(i).getRes();
            }else
            if(perguntas.get(i).getTipoADS() == 3){
                Ps += perguntas.get(i).getRes();
            }
        }

        a = Pa * 2;
        d = Pd * 2;
        s = Ps * 2;

        salvarAnamnese();
    }

    private void salvarAnamnese() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String acao = "salvarAnamnese";
                    String Sa = String.valueOf(a);
                    String Sd = String.valueOf(d);
                    String Ss = String.valueOf(s);
                    Log.i("Valor", "A " + Sa);
                    Log.i("Valor", "D " + Sd);
                    Log.i("Valor", "S " + Ss);
                    String cod_pessoa = String.valueOf(logado.getFbcod_pessoa());

                    parametros = "acao="+acao+"&codPessoa="+cod_pessoa+"&a="+Sa+"&d="+Sd+"&s="+Ss;

                    URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
//                    URL url = new URL("http://10.87.202.177:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
//                    URL url = new URL("http://10.87.202.168:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(parametros);

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String apnd = "", linha = "";

                    while ((linha = br.readLine()) != null)
                        apnd += linha;

                    JSONObject obj = new JSONObject();
                    obj.put("status", apnd);

                    if(obj.getString("status").equals("success")){
                        Log.i("teste", "Foi anaminelson");
                        mudarAcesso();
                    }else{
                        Log.i("teste", "Nem funfo");
                    }

                }catch(Exception e){
                    Log.i("teste", e.toString());
                }
            }
        }).start();
    }

    private void mudarAcesso() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String acao = "mudarAcesso";
                    String cod_pessoa = String.valueOf(logado.getFbcod_pessoa());

                    parametros = "acao="+acao+"&codPessoa="+cod_pessoa;

                    URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                    //URL url = new URL("http://10.87.202.177:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
//                    URL url = new URL("http://10.87.202.168:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(parametros);

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String apnd = "", linha = "";

                    while ((linha = br.readLine()) != null)
                        apnd += linha;

                    JSONObject obj = new JSONObject();
                    obj.put("status", apnd);

                    if(obj.getString("status").equals("success")){
                        Log.i("teste", "mudou status");
                    }else{
                        Log.i("teste", "Nem funfo");
                    }

                    Intent intent = new Intent(DASS21Activity.this, VerificaTipoAcessoActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);

                }catch(Exception e){
                    Log.i("teste", e.toString());
                }
            }
        }).start();
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

    @Override
    protected void onRestart () {
        super.onRestart();
        initRecyclerView();
        carregarPergunta();
    }



    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(DASS21Activity.this, LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_paciente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item0:
                Intent intent = new Intent(DASS21Activity.this, PesquisaActivity.class);
                startActivity(intent);
                return true;
            case R.id.item1:
                intent = new Intent(DASS21Activity.this, AnamnesesActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                intent = new Intent(DASS21Activity.this, ContactsActivity.class);
                startActivity(intent);
                return true;
            case R.id.item3:
                intent = new Intent(DASS21Activity.this, EditarPerfilActivity.class);
                startActivity(intent);
                return true;
            case R.id.item4:
                FirebaseAuth.getInstance().signOut();
                verifyAuthentication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
