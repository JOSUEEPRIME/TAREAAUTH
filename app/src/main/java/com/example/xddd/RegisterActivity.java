package com.example.xddd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
    TextView alreadyHaveaccount;
    EditText inputEmail,inputPassword, inputConfirmPassword;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        alreadyHaveaccount=findViewById(R.id.alreadyHaveaccount);

        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        inputConfirmPassword=findViewById(R.id.inputConfirmPassword);
        btnRegister=findViewById(R.id.btnRegister);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        alreadyHaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforAuth();
            }
        });
    }

    private void PerforAuth() {
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        String confirmPassword=inputConfirmPassword.getText().toString();

        if (!email.matches(emailPattern));
        {
            inputEmail.setError("Ingrese el correo electrónico");
        }if (password.isEmpty() || password.length() < 6)

        {
            inputPassword.setError("Ingrese la contraseña adecuada");
        } else if (!password.equals(confirmPassword))
        {
         inputConfirmPassword.setError("la contraseña no coincide con ambos campos");
        }else
        {
            progressDialog.setMessage("Espere el Registro...");
            progressDialog.setTitle("Registrado!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                     progressDialog.dismiss();
                     sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registro Completo", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}