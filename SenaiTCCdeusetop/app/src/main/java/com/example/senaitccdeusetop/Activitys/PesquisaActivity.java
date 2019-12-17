package com.example.senaitccdeusetop.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senaitccdeusetop.Chat.ChatActivity;
import com.example.senaitccdeusetop.Chat.ContactsActivity;
import com.example.senaitccdeusetop.R;
import com.example.senaitccdeusetop.Vo.Pessoa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PesquisaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnPesquisar;
    Spinner spnDia, spnInicio, spnFim;
    String Dia, inicio, fim, diaFormatado;

    private GroupAdapter adapter;

    private List<Pessoa> codPessoas = new ArrayList<>();
    private Pessoa logado;
    private String parametros;
    private String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

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

        final String uidLogado = FirebaseAuth.getInstance().getUid();

        RecyclerView rv = findViewById(R.id.recycler_contact);

        adapter = new GroupAdapter<>();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Log.i("teste", "cliko");

                UserItem userItem = (UserItem) item;
                String codigoEstagiario = userItem.user.getUuid();
                String nomeEstagiario = userItem.user.getFbnome();
                Log.i("teste", "cod do maniaco " + codigoEstagiario);
                Log.i("teste", "nome do maniaco " + nomeEstagiario);

                FirebaseFirestore.getInstance().collection("contatos" + uidLogado)
                        .document(codigoEstagiario)
                        .set(userItem.user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("teste", "adicionou");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                FirebaseFirestore.getInstance().collection("contatos" + codigoEstagiario)
                        .document(uidLogado)
                        .set(logado)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("teste", "adicionou");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });

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

        fetchUsers();
    }

    private void  fetchUsers(){

        List<String> codFb = new ArrayList<>();
        codFb.add("15Wj548WSBRzKRRzojdnV9qzCgf1");
        codFb.add("2R4m49qfqxenrG3Bb40XXehH1Lz1");
        codFb.add("6lURbhdNfhgQ6gla6Fvghwdsk8L2");

        for (String s : codFb){
            FirebaseFirestore.getInstance().collection("/users").document(s).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Pessoa user = documentSnapshot.toObject(Pessoa.class);
                            adapter.add(new UserItem(user));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

//        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/users").document("");
//        List<DocumentSnapshot> future = (List<DocumentSnapshot>) docRef.get();
//        DocumentSnapshot document = future.get(0);
//        if (document.exists()) {
//            Pessoa user = document.toObject(Pessoa.class);
//            adapter.add(new UserItem(user));
//        } else {
//            System.out.println("No such document!");
//        }



//        FirebaseFirestore.getInstance().collection("/users")
//                .document("kk")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if(e != null){
//                            Log.e("Teste", e.getMessage(), e);
//                            return;
//                        }
//
//                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
//                        for(DocumentSnapshot doc : docs){
//                            Pessoa user = doc.toObject(Pessoa.class);
//
//                            adapter.add(new UserItem(user));
//                        }
//                    }
//                });
    }

    private class UserItem extends Item<ViewHolder> {

        private final Pessoa user;

        private UserItem(Pessoa user){
            this.user = user;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView txtUsername = viewHolder.itemView.findViewById(R.id.textView);
            ImageView imgPhoto = viewHolder.itemView.findViewById(R.id.imageView);

            txtUsername.setText(user.getFbnome());

            Picasso.get()
                    .load(user.getProfileUrl())
                    .into(imgPhoto);
        }

        @Override
        public int getLayout() {
            return R.layout.item_user;
        }
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
            case R.id.item5:
                intent = new Intent(PesquisaActivity.this,DefineHorarioActivity.class);
                startActivity(intent);
                return true;

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
                        Log.i("teste", "oto peganu estagiaro");

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

                        String linha = "";
                        JSONObject obj;
                        while ((linha = br.readLine()) != null){
                            obj = new JSONObject(linha);
                            Log.i("teste", "obj " + obj);
                            Pessoa p = new Pessoa();
                            p.setFbcod_pessoa(obj.getInt("cod_pessoa"));
                            Log.i("teste", "FBcod " + p.getFbcod_pessoa());
                            codPessoas.add(p);
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                    }

                }


            }).start();
            

        }
    }
}




