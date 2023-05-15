package hcmute.edu.vn.fruitsclassificationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.fruitsclassificationapp.activities.ClassifyActivity;

public class MainActivity extends AppCompatActivity {
    ImageView camera, choose, recommend;
    ImageView imageView;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            }

        });
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cam) {
                Intent intent = new Intent(getApplicationContext(), ClassifyActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

}