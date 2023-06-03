package hcmute.edu.vn.fruitsclassificationapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hcmute.edu.vn.fruitsclassificationapp.activities.ClassifyActivity;

public class MainActivity extends AppCompatActivity {
    ImageView camera, choose, recommend, imgManage;
    ImageView imageView;
    TextView result;
    ImageView imgSignout, imgManage;
    TextView tvEmail;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, SystemStaticVariables.DetectedFruitName );
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        camera = findViewById(R.id.takePicture);
        choose = findViewById(R.id.choosePicture);
        recommend = findViewById(R.id.recommend);
      
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cam) {
                Intent intent = new Intent(getApplicationContext(), ClassifyActivity.class);
                startActivity(intent);
                finish();
            }

        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View select) {
                Intent intent = new Intent(getApplicationContext(), ClassifyActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "Clicked button choose picture" );
            }

        });
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cam) {

                startActivity(new Intent(getApplicationContext(), RecommendOnlyActivity.class));
                Log.d(TAG, SystemStaticVariables.DetectedFruitName );
            }
        });

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

        imgManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecommendActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}