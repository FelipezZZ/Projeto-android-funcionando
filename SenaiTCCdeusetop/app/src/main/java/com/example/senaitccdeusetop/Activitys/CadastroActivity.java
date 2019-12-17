package com.example.senaitccdeusetop.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btn_selected_photo;
    private CircleImageView img_photo;
    private Spinner spinner;
    private EditText mEditRa;

    private EditText mEditUsername;
    private EditText mEditEmail;
    private EditText mEditPassword;
    private EditText mEditCPassword;
    private Button mBtnEnter;

    private Uri mSelectedUri;

    private String parametros;

    private String universidade;
    private int tipoperfil;

    private String RA;
    private String nome;
    private String email;
    private String senha;
    private String csenha;

    private int cod_pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btn_selected_photo = findViewById(R.id.btn_selected_photo);
        img_photo = findViewById(R.id.img_photo);
        spinner = findViewById(R.id.spinner);
        mEditRa = findViewById(R.id.edit_ra);


        mEditUsername = findViewById(R.id.edit_username);
        mEditEmail = findViewById(R.id.edit_email);
        mEditPassword = findViewById(R.id.edit_password);
        mEditCPassword = findViewById(R.id.edit_Cpassword);
        mBtnEnter = findViewById(R.id.btn_enter);

        Intent intent = getIntent();
        String tipoUsuario = intent.getStringExtra("tipoUsuario");

        if(tipoUsuario.equals("paciente")){
            tipoperfil = 2;
        }

        if(tipoUsuario.equals("estagiario")){
            tipoperfil = 1;
            btn_selected_photo.setVisibility(View.VISIBLE);
            img_photo.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            mEditRa.setVisibility(View.VISIBLE);
        }

        btn_selected_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.universidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void cadastrar() {
        RA = mEditRa.getText().toString();
        nome = mEditUsername.getText().toString();
        email = mEditEmail.getText().toString();
        senha = mEditPassword.getText().toString();
        csenha = mEditCPassword.getText().toString();

        if(tipoperfil == 1){
            if(RA == null || RA.isEmpty() || nome == null || nome.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty() || csenha == null || csenha.isEmpty()){
                Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                return;
            }
        }else if(tipoperfil == 2){
            if(nome == null || nome.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty() || csenha == null || csenha.isEmpty()){
                Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                return;
            }
            universidade = null;
            RA = null;
        }

        if(!validateEmailFormat(email)){
            Toast.makeText(this, "Insira um email válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!senha.equals(csenha)){
            Toast.makeText(this, "As senhas devem ser iguais", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                try {

                    String acao = "cadastrarPessoa";

                    parametros = "acao="+acao+"&universidade="+universidade+"&RA="+RA+"&nome="+nome+"&email="+email+
                            "&senha="+senha+"&tipoPerf="+tipoperfil+"&cadastroFB="+"true";

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

                    JSONObject obj = new JSONObject();
                    obj.put("cod_pessoa", apnd);

                    String Scod_pessoa = String.valueOf(obj.get("cod_pessoa"));
                    cod_pessoa = Integer.valueOf(Scod_pessoa);

                    createUserFireBase();
                }catch(Exception e){
                    Log.i("teste", e.toString());
                }
            }

        }).start();
    }

    public void createUserFireBase(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
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
        if(tipoperfil == 1){
            String filename = UUID.randomUUID().toString();
            final StorageReference ref = FirebaseStorage.getInstance().getReference("/images" + filename);
            ref.putFile(mSelectedUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.i("Teste", uri.toString());

                                   String uid = FirebaseAuth.getInstance().getUid();
                                    String fbnome = nome;
                                    String profileUrl = uri.toString();
                                    int fbcod_pessoa = cod_pessoa;

                                    Pessoa p = new Pessoa(uid, fbnome, profileUrl, fbcod_pessoa);

                                    FirebaseFirestore.getInstance().collection("users")
                                            .document(uid)
                                            .set(p)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                        codFBinSql();

//                                                    Intent intent = new Intent(CadastroActivity.this, VerificaTipoAcessoActivity.class);
//
//                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                                                    startActivity(intent);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.i("Teste", e.getMessage());
                                                }
                                            });

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste", e.getMessage(), e);
                        }
                    });



        }else if(tipoperfil == 2){
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

//                            Intent intent = new Intent(CadastroActivity.this, VerificaTipoAcessoActivity.class);
//
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Teste", e.getMessage());
                        }
                    });
        }

    }

    private void codFBinSql() {

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

                            Intent intent = new Intent(CadastroActivity.this, VerificaTipoAcessoActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            mSelectedUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
                img_photo.setImageDrawable(new BitmapDrawable(bitmap));
                btn_selected_photo.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        universidade = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean validateEmailFormat(final String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

}
