package hcmute.edu.vn.fruitsclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hcmute.edu.vn.fruitsclassificationapp.model.User;
import hcmute.edu.vn.fruitsclassificationapp.until.StringUtil;

public class Register extends AppCompatActivity {

    EditText edtName, edtEmail, edtPass;
    Button btnRegister;
    FirebaseAuth mAuth;
    //FirebaseUser mUser;
    ProgressBar progressBar;
    RadioButton rdbAdmin, rdbUser;
    TextView loginAcc;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        //mUser = mAuth.getCurrentUser();

        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        progressBar = findViewById(R.id.processBar);
        rdbAdmin = findViewById(R.id.rdb_admin);
        rdbUser = findViewById(R.id.rdb_user);
        loginAcc = findViewById(R.id.loginAcc);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = String.valueOf(edtEmail.getText());
                pass = String.valueOf(edtPass.getText());
//                if(TextUtils.isEmpty(email)){
//                    Toast.makeText(Register.this,"Enter Email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(TextUtils.isEmpty(pass)){
//                    Toast.makeText(Register.this,"Enter Password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                mAuth.createUserWithEmailAndPassword(email, pass)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressBar.setVisibility(View.GONE);
//                                if (task.isSuccessful()) {
//                                    Intent intent = new Intent(Register.this,Login.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    Toast.makeText(Register.this, "Register Successfully.",
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Toast.makeText(Register.this, "Register Failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                if (StringUtil.isEmpty(email)) {
                    Toast.makeText(Register.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
                } else if (StringUtil.isEmpty(pass)) {
                    Toast.makeText(Register.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
                } else if (!StringUtil.isValidEmail(email)) {
                    Toast.makeText(Register.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
                } else {
                    if (rdbAdmin.isChecked()) {
                        if (!email.contains("@admin.com")) {
                            Toast.makeText(Register.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
                            } else {
                            signUpUser(email, pass);
                        }
                        return;
                    }

                    if (email.contains("@admin.com")) {
                        Toast.makeText(Register.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
                        } else {
                        signUpUser(email, pass);
                    }
                }
            }
        });




        loginAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signUpUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            if (user.getEmail() != null && user.getEmail().contains("@admin.com")) {
                                userObject.setAdmin(true);
                            }
//                            DataStoreManager.setUser(userObject);
//                            GlobalFunction.gotoMainActivity(this);
                            Intent intent = new Intent(Register.this,Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(Register.this, "Register Successfully.", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(Register.this, getString(R.string.msg_sign_up_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}