package com.example.senaitccdeusetop.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senaitccdeusetop.Vo.Pessoa;
import com.example.senaitccdeusetop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditEmail;
    private EditText mEditPassword;
    private Button mBtnEnter;
    private TextView mTxtAccount;

    private String parametros;

    private String email;
    private String password;
    private String nome;

    private int cod_pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("AUXILIUM");

        mEditEmail = findViewById(R.id.edit_email);
        mEditPassword = findViewById(R.id.edit_password);
        mBtnEnter = findViewById(R.id.btn_enter);
        mTxtAccount = findViewById(R.id.txt_account);

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = mEditEmail.getText().toString();
                password = mEditPassword.getText().toString();

                if(email == null || email.isEmpty() || password == null || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email e senha devem ser preenchidos", Toast.LENGTH_SHORT).show();
                    return;
                }

                verificaCadastroSQL();

            }
        });

        mTxtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TypeUserActivity.class);
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                verifyAuthentication();
            }
        }).start();
    }

    private void verificaCadastroSQL() {

        Log.i("teste", "ovo verificar");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String acao = "verificaCadastroSQL";

                    parametros = "acao="+acao+"&email="+email+"&senha="+password;


//                    URL url = new URL("http://192.168.56.1:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                    URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                  //  URL url = new URL("http://192.168.56.1:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
//                    URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(parametros);

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String apnd = "", linha = "";

                    while ((linha = br.readLine()) != null)
                        apnd += linha;

                    JSONObject obj = new JSONObject(apnd);

                    String Scod_pessoa = String.valueOf(obj.get("cod_pessoa"));
                    nome = String.valueOf(obj.get("nome"));
                    cod_pessoa = Integer.valueOf(Scod_pessoa);

                    Log.i("teste", "cod_pessoa " + cod_pessoa);

                    if(cod_pessoa != 0 ){
                        createUserFireBase();
                    }else{
                        logar();
                    }

                }catch (Exception e){
                    Log.i("teste", "kkk " + e.toString());
                }
            }
        }).start();
    }

    private void createUserFireBase() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("Teste", task.getResult().getUser().getUid());

                            cadastrarFireBase();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste", e.getMessage());
                    }
                });
    }

    private void cadastrarFireBase() {

        Log.i("teste", "Ovo cadastra no fogo");

        String uid = FirebaseAuth.getInstance().getUid();
        String fbnome = nome;
        String profileUrl = "https://cdn1.iconfinder.com/data/icons/business-users/512/circle-512.png";
        int fbcod_pessoa = cod_pessoa;
        String sfbcod_pessoa = String.valueOf(cod_pessoa);

        Pessoa p = new Pessoa(uid, fbnome, profileUrl, fbcod_pessoa);

        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(p)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        codFBinSql();

//                        Intent intent = new Intent(LoginActivity.this, VerificaTipoAcessoActivity.class);
//
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste", e.getMessage());
                    }
                });
    }

    private void codFBinSql() {

        Log.i("teste", "codFB LOGIN");

        final int Pcod_pessoa = cod_pessoa;
        final String cod_firebase = FirebaseAuth.getInstance().getUid();

        new Thread(new Runnable() {
            public void run() {
                try {

                    String acao = "codFBinSql";

                    parametros = "acao="+acao+"&cod_pessoa="+Pcod_pessoa+"&cod_firebase="+cod_firebase;

//                    URL url = new URL("http://192.168.56.1:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                    URL url = new URL("http://192.168.100.78:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
//                    URL url = new URL("http://10.87.202.138:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
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
                    obj.put("result", apnd);

                    if(String.valueOf(obj.get("result")).equals("ok")){
                        Log.i("teste", "adicionou o codfb");

                        Intent intent = new Intent(LoginActivity.this, VerificaTipoAcessoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Log.i("teste", "não adicionou o codfb");
                    }
                }catch(Exception e){
                    Log.i("teste", e.toString());
                }
            }

        }).start();
    }

    private void logar(){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(LoginActivity.this, VerificaTipoAcessoActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste", e.getMessage());
                    }
                });
    }

    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() != null){
            Intent intent = new Intent(LoginActivity.this, VerificaTipoAcessoActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

}
