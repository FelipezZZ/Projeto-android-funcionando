package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gamerleague.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener{

    RadioGroup rbButtons;
    RadioButton rbFuncionario, rbPaciente;
    EditText edtNome,edtLogin, edtNick, edtIdentidade, edtSenha, edtCSenha;
    Button btnCadastrar;
    String parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        rbButtons = findViewById(R.id.rbButtons);
        rbFuncionario = findViewById(R.id.rbFuncionario);
        rbPaciente = findViewById(R.id.rbPaciente);

        edtLogin = findViewById(R.id.edtLogin);
        edtNick = findViewById(R.id.edtNick);
        edtIdentidade = findViewById(R.id.edtIdentidade);
        edtSenha = findViewById(R.id.edtSenha);
        edtCSenha = findViewById(R.id.edtCSenha);

        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnCadastrar:
                cadastrar();
        }
    }

     public void cadastrar(){
        final String strLogin = edtLogin.getText().toString();
        final String strNick = edtNick.getText().toString();
        final String strIdentidade = edtIdentidade.getText().toString();
        final String strSenha = edtSenha.getText().toString();
        final String strCSenha = edtCSenha.getText().toString();

        int tipoPerf = 0;

        if(rbFuncionario.isChecked()){
            tipoPerf = 1;
        }else if(rbPaciente.isChecked()){
            tipoPerf = 2;
        }

        final String stipoPerf = String.valueOf(tipoPerf);

        new Thread(new Runnable() {
            public void run() {
                try {

                    String acao = "cadastrarPessoa";

                    String strlogin;
                    parametros = "acao="+acao+"&login="+strLogin+"&nickname="+strNick+"&senha="+strSenha+"&csenha="+strCSenha+"&identidade="+strIdentidade+"&tipoPerf="+stipoPerf;

                    URL url = new URL("http://10.87.202.138:8080/ProjetoPsicologoBackEnd/ProcessaPessoa");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);//QUEBRANDO CODIGO
                    con.connect();

                   DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                   wr.writeBytes(parametros);

                   BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                   String apnd = "", linha = "";

                   while ((linha = br.readLine()) != null) apnd += linha;

                   JSONObject obj = new JSONObject(apnd);

                }catch(Exception e){

                    Log.i("teste", e.toString());

                }

            }

        }).start();
        Toast.makeText(this, parametros, Toast.LENGTH_SHORT).show();
    }

}
