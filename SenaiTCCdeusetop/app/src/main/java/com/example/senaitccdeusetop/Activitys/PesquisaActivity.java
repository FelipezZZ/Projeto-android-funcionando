package com.example.senaitccdeusetop.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.senaitccdeusetop.Chat.ContactsActivity;
import com.example.senaitccdeusetop.R;
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

public class PesquisaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnPesquisar;
    Spinner spnDia, spnInicio, spnFim;
    String Dia, inicio, fim, diaFormatado;

    private Pessoa logado;
    private String parametros;
    private String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        btnPesquisar = findViewById(R.id.btnPesquisar);
        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pesquisar(diaFormatado, inicio, fim);
            }
        });


        spnDia = findViewById(R.id.spnDia);
        spnDia.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adpDias = ArrayAdapter.createFromResource(this,
                R.array.dias, android.R.layout.simple_spinner_item);
        adpDias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDia.setAdapter(adpDias);


        ArrayAdapter<CharSequence> adpHorario = ArrayAdapter.createFromResource(this,
                R.array.Horario, android.R.layout.simple_spinner_item);

        spnInicio = findViewById(R.id.spnInicio);
        spnInicio.setOnItemSelectedListener(this);
        adpHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnInicio.setAdapter(adpHorario);


        spnFim = findViewById(R.id.spnFim);
        spnFim.setOnItemSelectedListener(this);
        adpHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFim.setAdapter(adpHorario);

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
                Intent intent = new Intent(PesquisaActivity.this, PesquisaActivity.class);
                startActivity(intent);
                return true;
            case R.id.item1:
                intent = new Intent(PesquisaActivity.this, AnamnesesActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                intent = new Intent(PesquisaActivity.this, ContactsActivity.class);
                startActivity(intent);
                return true;
            case R.id.item3:
                intent = new Intent(PesquisaActivity.this, EditarPerfilActivity.class);
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
            Intent intent = new Intent(PesquisaActivity.this, LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

        switch (parent.getId()) {

            case R.id.spnDia:
                Dia = parent.getItemAtPosition(i).toString();
                Toast.makeText(this, "spinner dia" + Dia, Toast.LENGTH_SHORT).show();
                switch (Dia) {
                    case "Domingo":
                        diaFormatado = "dom";
                        break;

                    case "Segunda":
                        diaFormatado = "seg";
                        break;

                    case "Terça":
                        diaFormatado = "ter";
                        break;

                    case "Quarta":
                        diaFormatado = "qua";
                        break;

                    case "Quinta":
                        diaFormatado = "qui";
                        break;

                    case "Sexta":
                        diaFormatado = "sex";
                        break;

                    case "Sábado":
                        diaFormatado = "sab";
                        break;

                }


                break;

            case R.id.spnInicio:
                inicio = parent.getItemAtPosition(i).toString();
                Toast.makeText(this, "spinner Inicio", Toast.LENGTH_SHORT).show();

                break;

            case R.id.spnFim:
                fim = parent.getItemAtPosition(i).toString();
                Toast.makeText(this, "spinner Fim", Toast.LENGTH_SHORT).show();

                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    public void Pesquisar(final String dia, String inicio, String fim) {
        /*
        string currentString = "Fruit: they taste good";
        String[] separated = currentString.split(":");
        separated[0]; // this will contain "Fruit"
        separated[1]; // this will contain " they taste good"
        */
        String[] inicioFormatado = inicio.split(":");
        String[] fimFormatado = fim.split(":");


        if (Integer.parseInt(inicioFormatado[0]) > Integer.parseInt(fimFormatado[0])) {

            Toast.makeText(this, "O horário de inicio precisa ser maior que o de fim", Toast.LENGTH_SHORT).show();
        } else{

            final String horario = inicio + "~~" + fim;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String acao = "pesquisaEstagiario";

                        String parametros = "acao=" + acao + "&dia=" + dia + "&horario=" + horario;

                        URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                        //URL url = new URL("http://10.87.202.138:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                        // URL url = new URL ("http://10.87.202.168:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");

                        Log.i("batata","chegou na url");
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
                        obj.put("cod_pessoa", apnd);
                        Log.i("batata","conseguiu colocar no json");
                        Toast.makeText(PesquisaActivity.this, "F", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                    }

                }


            }).start();
            

        }
    }
}




