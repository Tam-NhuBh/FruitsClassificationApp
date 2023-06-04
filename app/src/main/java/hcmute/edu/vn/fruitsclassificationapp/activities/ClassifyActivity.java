package hcmute.edu.vn.fruitsclassificationapp.activities;

import static android.content.ContentValues.TAG;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;

import android.util.Base64;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import hcmute.edu.vn.fruitsclassificationapp.Login;
import hcmute.edu.vn.fruitsclassificationapp.RecommendActivity;
import hcmute.edu.vn.fruitsclassificationapp.RecommendOnlyActivity;
import hcmute.edu.vn.fruitsclassificationapp.SystemStaticVariables;
import hcmute.edu.vn.fruitsclassificationapp.ml.Model;
import hcmute.edu.vn.fruitsclassificationapp.R;


public class ClassifyActivity extends AppCompatActivity {
    ImageView camera, choose, imgSignout, imgManage, recommend,predictBtn;
    ImageView imageView;
    TextView result;
    TextView tvEmail;
    Bitmap bitmap;
    private static final String MODEL_PATH = "model.lite";
    protected Interpreter tflite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permission
        getPermission();

        String[] labels = new String[1001];
        int cnt = 0;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("labels.txt")));
            String line = bufferedReader.readLine();
            while (line!=null)
            {
                labels[cnt]=line;
                cnt++;
                line = bufferedReader.readLine();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        camera = findViewById(R.id.takePicture);
        choose = findViewById(R.id.choosePicture);
        imgSignout = findViewById(R.id.imgLogout);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        recommend = findViewById(R.id.recommend);
        imgManage = findViewById(R.id.imgManage);
        predictBtn = findViewById(R.id.imgPredict);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent =  new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);

            }
        });
        predictBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://10.0.2.2:8000/images";
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("stringImage", base64Image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest stringRequest = new JsonObjectRequest (Request.Method.POST, url, jsonParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String resultString = null;
                                try {
                                    resultString = response.getString("result");
                                    if(resultString.equals("Unknown") == true){
                                        result.setText("Take again!");
                                    }
                                    else{
                                        int resultInt = Integer.parseInt(resultString);
                                        result.setText(labels[resultInt] + " ");
                                        SystemStaticVariables.DetectedFruitName = labels[resultInt];                                    }
                                } catch (JSONException e) {
                                    result.setText("Not Get");
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println((error.toString()));
                                result.setText( "Disconnect!");
                            }
                        });
                queue.add(stringRequest);
            }
        });

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
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cam) {
                startActivity(new Intent(getApplicationContext(), RecommendOnlyActivity.class));

                Log.d(TAG, SystemStaticVariables.DetectedFruitName );
            }
        });
        imgManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecommendActivity.class));
            }
        });
    }

    void getPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ClassifyActivity.this, new String[]{Manifest.permission.CAMERA},11);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NotNull int[] grantResults)
    {
        if(requestCode==11)
        {
            if(grantResults.length>0)
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    this.getPermission();
                }
            }

        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int reusltCode, @Nullable Intent data)
    {
        if (requestCode==10)
        {
            if(data!=null)
            {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode==12)
        {
            bitmap=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, reusltCode, data);

    }
}

