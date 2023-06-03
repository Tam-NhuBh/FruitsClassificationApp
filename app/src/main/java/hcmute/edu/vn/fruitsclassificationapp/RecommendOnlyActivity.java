package hcmute.edu.vn.fruitsclassificationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import hcmute.edu.vn.fruitsclassificationapp.model.MainModel;

public class RecommendOnlyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecommendOnlyAdapter mainAdapter;
    //variable below used for add operation
    FloatingActionButton floatingActionButton;
    FloatingActionButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_only);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //fetch data from firebase
        txtSearch(SystemStaticVariables.DetectedFruitName);
        //back to home page
        btnBack = (FloatingActionButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void txtSearch(String str){
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                        .child("recommendfoods")
                                        .orderByChild("fruit_name")
                                        .startAt(str)
                                        .endAt(str + "~")
                                , MainModel.class)
                        .build();
        mainAdapter = new RecommendOnlyAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
}