package com.example.xddd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView createnewAccount;

    EditText inputEmail,inputPassword;
    Button btnSesion;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView imgGithub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createnewAccount = findViewById(R.id.createNewAccount);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        btnSesion=findViewById(R.id.btnSesion);
        progressDialog=new ProgressDialog(this);
        imgGithub=findViewById(R.id.imgGithub);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        createnewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));

                    }
                });

                btnSesion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        perforLogin();



            }
        });
        imgGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,GitHubActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void perforLogin() {
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();

        if (!email.matches(emailPattern));
        {
            inputEmail.setError("Ingrese el correo electrónico");
        }if (password.isEmpty() || password.length() < 6)

        {
            inputPassword.setError("Ingrese la contraseña adecuada");
        }else
        {
            progressDialog.setMessage("Espere el Inicio de sesion...");
            progressDialog.setTitle("Iniciado!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this, "Inicio Completo", Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}

    private void sendUserToNextActivity() {
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}