package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gamerleague.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtNome, edtSenha;
    Button btnEntrar, btnAbrirCadastro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAbrirCadastro = findViewById(R.id.btnAbrirCadastro);
        btnEntrar = findViewById(R.id.btnEntrar);

        edtNome = findViewById(R.id.edtNome);
        edtSenha = findViewById(R.id.edtSenha);

        btnAbrirCadastro.setOnClickListener(this);
        btnEntrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAbrirCadastro:
                Intent intent = new Intent(this, CadastroActivity.class);
                startActivity(intent);
                break;
            case R.id.btnEntrar:
                logarPessoa();
                break;

                default:
                    Log.i("errolog","erro");
                    break;

        }
    }

    private void logarPessoa() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    final String strLogin = edtNome.getText().toString();
                    final String strSenha = edtSenha.getText().toString();

                    String acao = "logarPessoa";

                    String strlogin;
                    String parametros;
                    parametros = "acao="+acao+"&login="+strLogin+"&senha="+strSenha;


                    URL url = new URL("http://10.87.202.138:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.connect();
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());

                    wr.writeBytes(parametros);

                    Log.i("info1","TO AQUI");
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String apnd = "", linha = "";

                    while ((linha = br.readLine()) != null) apnd += linha;

                    JSONObject obj = new JSONObject(apnd);

                    if (obj.get("status").equals("sucesso")){
                        Log.i("status","funfou cacete");
                                Intent inte = new Intent(MainActivity.this.getApplicationContext(), Anamnese.class);
                                startActivity(inte);


                    }

                } catch (ProtocolException e) {
                    e.printStackTrace();
                    Log.i("info1",e.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.i("info1",e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("info1",e.toString());
                }catch(Exception e){
                    Log.i("info1",e.toString());
                }



            }

            }).start();


    }
}
