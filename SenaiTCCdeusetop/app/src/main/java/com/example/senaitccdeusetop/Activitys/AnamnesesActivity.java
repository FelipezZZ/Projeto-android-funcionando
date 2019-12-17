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

import com.example.senaitccdeusetop.AnamnesesAdapter;
import com.example.senaitccdeusetop.Chat.ContactsActivity;
import com.example.senaitccdeusetop.R;
import com.example.senaitccdeusetop.RecyclerViewAdapter;
import com.example.senaitccdeusetop.Vo.Anamnese;
import com.example.senaitccdeusetop.Vo.Pessoa;
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

public class AnamnesesActivity extends AppCompatActivity implements RecyclerViewAdapter.OnNoteListenner {

    private Pessoa logado;
    private String parametros;
    private String tipoUsuario;

    List<Anamnese> listaanamneses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamneses);

        FirebaseFirestore.getInstance().collection("/users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        logado = documentSnapshot.toObject(Pessoa.class);
                        Log.i("teste", "cod " + logado.getFbcod_pessoa());
                        verificarTipoUsuario();
                    }
                });
    }

    private void pegarAnamneses() {

        Log.i("teste", "pegarAnam");

        new Thread(new Runnable() {
            public void run() {
                try {
                    parametros = "cod_pessoa="+ logado.getFbcod_pessoa() +
                            "&acao="+"listarAnamneses";

                    URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
//                    URL url = new URL("http://192.168.56.1:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(parametros);

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String linha = "";
                    JSONObject obj;
                    while ((linha = br.readLine()) != null) {
                        obj = new JSONObject(linha);
                        Log.i("teste", "obj " + obj);
                        Anamnese a = new Anamnese();
                        a.setCod_pessoa(obj.getInt("cod_pessoa"));
                        a.setDataAnamneses(obj.getString("dataAnamnese"));
                        a.setAnsiedade(obj.getInt("a"));
                        a.setDepressao(obj.getInt("d"));
                        a.setEstresse(obj.getInt("s"));
                        Log.i("teste", "a " + a.getDepressao());
                        listaanamneses.add(a);
                        initRecyclerView();
                    }
                }catch(Exception e) {
                    Log.i("teste", "erro " + e.toString());
                }
            }
        }).start();
    }

    private void initRecyclerView() {
        Log.i("teste", "recycler");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAnamnese);
        recyclerView.setLayoutManager(layoutManager);
        AnamnesesAdapter adapter = new AnamnesesAdapter(this, listaanamneses, (AnamnesesAdapter.OnNoteListenner) this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(int position) {
        Log.i("teste", "OnNoteClick");
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
                    pegarAnamneses();

                }catch(Exception e){
                    Log.i("teste", e.toString());
                }
            }

        }).start();
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
                Intent intent = new Intent(AnamnesesActivity.this, PesquisaActivity.class);
                startActivity(intent);
                return true;
            case R.id.item1:
                intent = new Intent(AnamnesesActivity.this, AnamnesesActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                intent = new Intent(AnamnesesActivity.this, ContactsActivity.class);
                startActivity(intent);
                return true;
            case R.id.item3:
                intent = new Intent(AnamnesesActivity.this, EditarPerfilActivity.class);
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

    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(AnamnesesActivity.this, LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }
}
