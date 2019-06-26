package softagi.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity
{
    EditText email_field,password_field;
    Button signin_btn;

    String email,password;

    FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        signin_btn = findViewById(R.id.signin_btn);

        auth = FirebaseAuth.getInstance();

        signin_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = email_field.getText().toString();
                password = password_field.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "password is too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(SigninActivity.this);
                progressDialog.setMessage("Please Wait Until Signing In ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                signUser(email,password);
            }
        });
    }

    private void signUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            if (task.getResult().getUser().isEmailVerified())
                            {
                                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                            } else
                                {
                                    Toast.makeText(getApplicationContext(), "please check your email and verify it", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                        } else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                    }
                });
    }

    public void register(View view)
    {
        onBackPressed();
    }
}
