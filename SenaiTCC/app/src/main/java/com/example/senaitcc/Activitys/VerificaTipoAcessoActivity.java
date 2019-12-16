package com.example.senaitcc.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.senaitcc.Pessoa;
import com.example.senaitcc.R;
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

public class VerificaTipoAcessoActivity extends AppCompatActivity {

    private Pessoa logado;
    private String parametros;
    private String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifica_tipo_usuario);

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

                    URL url = new URL("http://192.168.100.4:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");

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

                    verificarPrimeiroAcesso();

                }catch(Exception e){
                    Log.i("teste", e.toString());
                }
            }

        }).start();
    }

    private void verificarPrimeiroAcesso() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String acao = "verificarPrimeiroAcesso";
                    String cod_pessoa = String.valueOf(logado.getFbcod_pessoa());

                    parametros = "acao="+acao+"&codPessoa="+cod_pessoa;

                    URL url = new URL("http://192.168.100.4:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(parametros);

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String apnd = "", linha = "";

                    while ((linha = br.readLine()) != null)
                        apnd += linha;

                    JSONObject obj2 = new JSONObject();
                    obj2.put("primeiroAcesso", apnd);

                    if(obj2.get("primeiroAcesso").equals("true")){
                        if(tipoUsuario.equals("1")){

                        }
                        if(tipoUsuario.equals("2")){
                            Intent intent = new Intent(VerificaTipoAcessoActivity.this, DASS21Activity.class);
                            intent.putExtra("codPessoa", cod_pessoa);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }else{
                        if(tipoUsuario.equals("1")){

                        }
                        if(tipoUsuario.equals("2")){
                            Intent intent = new Intent(VerificaTipoAcessoActivity.this, PesquisarEstagiarioActivity.class);
                            intent.putExtra("codPessoa", cod_pessoa);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }catch(Exception e){
                    Log.i("teste", e.toString());
                }
            }
        }).start();
    }

}
