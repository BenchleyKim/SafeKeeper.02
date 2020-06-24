package com.example.safekeeper;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
private DatabaseReference database;
    private EditText et_id, et_pass, et_name, et_age;
    private Button btn_register2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//액티비티 실행시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_age= findViewById(R.id.et_age);
        //회원가입 버튼 클릭 시 수행
        btn_register2 = findViewById(R.id.btn_register2);
        btn_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Edit Text에 입력되어 있는 값을 get한다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                String userProfile = "사용자";
                int userAge = Integer.parseInt(et_age.getText().toString());

                database = FirebaseDatabase.getInstance().getReference();
                UserMarker userMarker = new UserMarker(userProfile,userID,userName, String.valueOf(userAge));
                database.child("Userdata").push().setValue(userMarker);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//성공한 경우
                                Toast.makeText(getApplicationContext(),"등록에 성공했습니다",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }else{//실패한 경우
                                Toast.makeText(getApplicationContext(),"등록에 실패했습니다",Toast.LENGTH_SHORT).show();
                                return;


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                //서버로 Volley를 이용해서 요청을 합니다.

                RegisterRequest registerRequest = new RegisterRequest(userID,userPass,userName,userAge, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });


    }
}
