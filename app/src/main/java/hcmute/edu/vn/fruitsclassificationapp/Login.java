package hcmute.edu.vn.fruitsclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hcmute.edu.vn.fruitsclassificationapp.model.User;
import hcmute.edu.vn.fruitsclassificationapp.until.StringUntil;

public class Login extends AppCompatActivity {
    EditText edtEmailLogin, edtPassLogin;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView signUpAcc;
    RadioButton rdbAdmin, rdbUser;
    TextView forgotPass;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPassLogin = findViewById(R.id.edtPassLogin);
        progressBar = findViewById(R.id.processBarLogin);
        rdbAdmin = findViewById(R.id.rdb_admin);
        rdbUser = findViewById(R.id.rdb_user);
        signUpAcc = findViewById(R.id.signUpAcc);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = String.valueOf(edtEmailLogin.getText());
                pass = String.valueOf(edtPassLogin.getText());
                if (StringUntil.isEmpty(email)) {
                    Toast.makeText(Login.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
                } else if (StringUntil.isEmpty(pass)) {
                    Toast.makeText(Login.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
                } else if (!StringUntil.isValidEmail(email)) {
                    Toast.makeText(Login.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
                } else {
                    if (rdbAdmin.isChecked()) {
                        if (!email.contains("@admin.com")) {
                            Toast.makeText(Login.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
                        } else {
                            signInUser(email, pass);
                        }
                        return;
                    }

                    if (email.contains("@admin.com")) {
                        Toast.makeText(Login.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
                    } else {
                        signInUser(email, pass);
                    }
                }

            }
        });
        signUpAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPass = findViewById(R.id.forgotPass);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(Login.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Login.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(Login.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }
    private void signInUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
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
                            Toast.makeText(Login.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(Login.this, getString(R.string.msg_sign_in_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}