package com.example.senaitccdeusetop.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.senaitccdeusetop.Chat.ContactsActivity;
import com.example.senaitccdeusetop.R;
import com.google.firebase.auth.FirebaseAuth;

public class PesquisarEstagiarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_estagiario);

        Log.i("teste", "to no pesquisaEstagiario");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.contacts:
                Intent intent = new Intent(PesquisarEstagiarioActivity.this, ContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.logut:
                FirebaseAuth.getInstance().signOut();
                verifyAuthentication();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(PesquisarEstagiarioActivity.this, LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

}
