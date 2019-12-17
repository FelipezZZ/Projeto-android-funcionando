package com.example.senaitccdeusetop.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

public class DefineHorarioActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spnDia,spnInicio,spnFim;
    Button btnSalvar;
    String dia,inicio,fim,diaFormatado;
    private Pessoa logado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_horario);

        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            salvarHorario(dia,inicio,fim,String.valueOf(logado.getFbcod_pessoa()));
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

                    }
                });







    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.spnDia:
                dia = parent.getItemAtPosition(position).toString();

                switch (dia) {
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
                inicio = parent.getItemAtPosition(position).toString();


                break;

            case R.id.spnFim:
                fim = parent.getItemAtPosition(position).toString();


                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void salvarHorario(String diaFormatado, String inicio, String fim, final String codPessoa){
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
                        String acao = "salvaHorario";

                        String parametros = "acao=" + acao + "&dia=" + dia + "&horario=" + horario+"&codPessoa"+codPessoa;

                       // URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                        URL url = new URL("http://10.87.202.138:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
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

                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                    }

                }


            }).start();


        }
    }



}
