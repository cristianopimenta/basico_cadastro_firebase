package br.com.otimizesistemas.cadastrobasico;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private EditText mEdtId, mEdtNome, mEdtTelefone;
    private Button bSalvar, bPesquisar, bAtualizar, bExcluir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtId = findViewById(R.id.edtId);
        mEdtNome = findViewById(R.id.edtNome);
        mEdtTelefone = findViewById(R.id.edtTelefone);
        bSalvar = findViewById(R.id.btSalvar);
        bPesquisar = findViewById(R.id.btPesquisar);
        bAtualizar = findViewById(R.id.btAtualizar);
        bExcluir = findViewById(R.id.btExcluir);

        bSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarContato();
            }
        });

        bPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesquisarDados();
            }
        });

        bAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    AtualizarContato();
            }
        });

        bExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //      ExcluirContato();
            }
        });

    }

    public void SalvarContato() {
            String idStr = mEdtId.getText().toString();
            String nome = mEdtNome.getText().toString();
            String telefone = mEdtTelefone.getText().toString();

            if (idStr.isEmpty() || nome.isEmpty() || telefone.isEmpty()) {
                // Se algum campo estiver vazio, não salve e informe o usuário.
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int id = Integer.parseInt(idStr);

                Agenda agenda = new Agenda(id, nome, telefone);

                FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
                DatabaseReference reference = databaseReference.getReference("agenda");

                // Usando push() para gerar uma chave única.
                reference.push().setValue(agenda);

                // Limpar os campos após salvar.
                mEdtId.setText("");
                mEdtNome.setText("");
                mEdtTelefone.setText("");

                Toast.makeText(this, "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                // Tratar exceção se o ID não for um número válido.
                Toast.makeText(this, "O ID deve ser um número válido", Toast.LENGTH_SHORT).show();
            }
        }


        public void pesquisarDados(){
        mEdtNome.setText("");
        mEdtTelefone.setText("");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        int id = Integer.parseInt(mEdtId.getText().toString());
        databaseReference.child("agenda").orderByChild("id").equalTo(id).addChildEventListener(childEventListener);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Agenda agenda = snapshot.getValue(Agenda.class);
            Log.i("FIREBASE_CONSULTA", snapshot.getValue().toString());
            mEdtNome.setText(agenda.getNome());
            mEdtTelefone.setText(agenda.getTelefone());
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            // Implemente este método se você quiser lidar com a alteração de dados
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            // Implemente este método se você quiser lidar com a remoção de dados
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            // Implemente este método se você quiser lidar com a movimentação de dados
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Implemente este método se ocorrer um erro na leitura dos dados
        }
    };


}