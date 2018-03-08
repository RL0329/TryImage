package com.example.reanmae.tryapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int INTENT_REQUEST_CODE = 100;

    public static final String URL = "http://10.16.33.98:3000";

    private Button mBtImageSelect;
    private Button mBtImageShow;
    private ProgressBar mProgressBar;
    private String mImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtImageSelect=findViewById(R.id.btnadd);
        mBtImageShow=findViewById(R.id.btnshow);

        mBtImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");

                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
            }
        });
        mBtImageShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mImageUrl));
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                try {

                    InputStream is = getContentResolver().openInputStream(data.getData());

                    uploadImage(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private void uploadImage(byte[] imageBytes) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserClient retrofitInterface = retrofit.create(UserClient.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Call<GitHubRepo> call = retrofitInterface.uploadImage(body);

        call.enqueue(new Callback<GitHubRepo>() {
            @Override
            public void onResponse(Call<GitHubRepo> call, retrofit2.Response<GitHubRepo> response) {



                if (response.isSuccessful()) {

                    GitHubRepo responseBody = response.body();
                    mBtImageShow.setVisibility(View.VISIBLE);
                    mImageUrl = URL + responseBody.getPath();
                    Snackbar.make(findViewById(R.id.content), responseBody.getMessage(),Snackbar.LENGTH_SHORT).show();

                } else {

                    ResponseBody errorBody = response.errorBody();

                    Gson gson = new Gson();

                    try {

                        GitHubRepo errorResponse = gson.fromJson(errorBody.string(), GitHubRepo.class);
                        Snackbar.make(findViewById(R.id.content), errorResponse.getMessage(),Snackbar.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GitHubRepo> call, Throwable t) {


                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
    }
//    private static final int MY_PERMISSIONS_REQUEST = 100;
//    private int PICK_IMAGE = 1;
//    public static final String URL = "http://10.16.33.98:3000";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
//        }
//
//        Button addImage = findViewById(R.id.addimage);
//        addImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(
//                        Intent.createChooser(intent, "Select Picture"),
//                        PICK_IMAGE);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri uri = data.getData();
//
//        }
//    }
//
//    public void onRequestPermissionResult(int requestcode, String permissions[], int[] grantResults) {
//        switch (requestcode) {
//            case MY_PERMISSIONS_REQUEST: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//
//                }
//            }
//            return;
//        }
//    }


}
