package com.example.xddd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class GitHubActivity extends AppCompatActivity {


    EditText inputEmail;
    Button btnLogin;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_git_hub);

        inputEmail=findViewById(R.id.inputEmail);
        btnLogin=findViewById(R.id.btnLogin);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                if (!email.matches(emailPattern)) {
                    Toast.makeText(GitHubActivity.this, "Ingresa un email apropiado", Toast.LENGTH_SHORT).show();
                } else
                {
                  OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                    provider.addCustomParameter("login", email);

                    List<String> scopes =
                            new ArrayList<String>() {
                                {
                                    add("user:email");
                                }
                            };
                    provider.setScopes(scopes);


                    Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
                    if (pendingResultTask != null) {
                        // There's something already here! Finish the sign-in for your user.
                        pendingResultTask
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                // User is signed in.
                                                // IdP data available in
                                                // authResult.getAdditionalUserInfo().getProfile().
                                                // The OAuth access token can also be retrieved:
                                                // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                                                // The OAuth secret can be retrieved by calling:
                                                // ((OAuthCredential)authResult.getCredential()).getSecret().
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(GitHubActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                    } else {
                        mAuth
                                .startActivityForSignInWithProvider(GitHubActivity.this, provider.build())
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                               openNextActivity();
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(GitHubActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                    }
                }


            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openNextActivity() {
        Intent intent=new Intent(GitHubActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}