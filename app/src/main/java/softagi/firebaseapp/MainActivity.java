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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    EditText email,password,confirmpassword,username;
    Button register_btn;
    TextView signin_txt;

    String email_txt,password_txt,confirmpassword_txt,username_txt;

    FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_field);
        password = findViewById(R.id.password_field);
        confirmpassword = findViewById(R.id.confirmpassword_field);
        username = findViewById(R.id.username_field);
        register_btn = findViewById(R.id.register_btn);
        signin_txt = findViewById(R.id.already_txt);

        register_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email_txt = email.getText().toString();
                password_txt = password.getText().toString();
                confirmpassword_txt = confirmpassword.getText().toString();
                username_txt = username.getText().toString();

                if (TextUtils.isEmpty(email_txt))
                {
                    Toast.makeText(MainActivity.this, "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password_txt.length() < 6)
                {
                    Toast.makeText(MainActivity.this, "password is too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!confirmpassword_txt.equals(password_txt))
                {
                    Toast.makeText(MainActivity.this, "password isn't matching", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username_txt))
                {
                    Toast.makeText(MainActivity.this, "please enter your username", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please Wait Until Creating Account ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                createUser(email_txt,password_txt);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
        {
            if (user.isEmailVerified())
            {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
            } else
                {
                    Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(intent);
                }
        }
    }

    private void createUser(String email_txt, String password_txt)
    {
        auth.createUserWithEmailAndPassword(email_txt,password_txt)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            task.getResult().getUser().sendEmailVerification();

                            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        } else
                            {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                    }
                });
    }

    @Override
    public void onBackPressed()
    {
        finishAffinity();
    }

    public void signIn(View view)
    {
        Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(intent);
    }
}
