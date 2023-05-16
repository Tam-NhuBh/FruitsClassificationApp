package hcmute.edu.vn.fruitsclassificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ImageView imgSignout, imgManage;
    TextView tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        setContentView(R.layout.layout_register);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.layout_listdishes);
//        setContentView(R.layout.activity_main);



        imgSignout = findViewById(R.id.imgLogout);
        imgManage = findViewById(R.id.imgManage);
        tvEmail = findViewById(R.id.tv_Email);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(!user.getEmail().contains("@admin.com"))
        {
            imgManage.setVisibility(View.GONE);
        }
        tvEmail.setText(user.getEmail());
        imgSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

    }
}