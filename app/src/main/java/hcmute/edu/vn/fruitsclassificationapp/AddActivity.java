package hcmute.edu.vn.fruitsclassificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    //1. Declare variables on view
    EditText fruitName, enFoodName, vnFoodName, linkImage;
    Button btnAdd, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //2. Access view add_activity.xml
        fruitName = (EditText) findViewById(R.id.txtFruitName);
        enFoodName = (EditText) findViewById(R.id.txtEnFoodName);
        vnFoodName = (EditText) findViewById(R.id.txtVnFoodName);
        linkImage = (EditText) findViewById(R.id.txtLinkImage);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);

        //3. Add button listener
        //3.1. button Add
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //5. Call method insert on btnAddListener
//                for(int i = 1; i < 10; i++){
//                    enFoodName.setText(enFoodName.getText() + Integer.toString(i));
//                    vnFoodName.setText(vnFoodName.getText() + Integer.toString(i));
//
//                }
                insertData();
                //6.Call method clearAll on btnAddListener
                clearAll();
                startActivity(new Intent(getApplicationContext(), RecommendActivity.class));
                finish();
            }
        });
        //3.2. button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecommendActivity.class));
                finish();
            }
        });
    }
    //4. Create method insert data to firebase
    private void insertData(){
        Map<String, Object> map = new HashMap<>();
        //4.1. put value from variable to map
        map.put("en_food_name", enFoodName.getText().toString());
        map.put("fruit_name", fruitName.getText().toString());
        map.put("link_image", linkImage.getText().toString());
        map.put("vn_food_name", vnFoodName.getText().toString());
        //4.2. add map to firebase database and success listener
        FirebaseDatabase.getInstance().getReference().child("recommendfoods").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddActivity.this, "Error while Insertion", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    //6. Create method clear all values on edit text after inserting successfully
    private void clearAll(){
        fruitName.setText("");
        enFoodName.setText("");
        vnFoodName.setText("");
        linkImage.setText("");
    }
}