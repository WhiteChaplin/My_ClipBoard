package com.example.my_clipboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class LoginActivity extends AppCompatActivity {
    EditText idInput,passwordInput;
    Button btn_create,btn_sign;
    FirebaseRemoteConfig firebaseRemoteConfig;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    final static int PERMISSION_CODE=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btn_create = (Button) findViewById(R.id.btn_login);
        btn_sign = (Button)findViewById(R.id.btn_sign);
        idInput = (EditText)findViewById(R.id.EditEmail);
        passwordInput = (EditText)findViewById(R.id.EditTextPass);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        final Intent intent = new Intent(this,MainActivity.class);

        checkPermission();

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEvent();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //???????????? ????????? ???????????? ???????????? ??????
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    //?????????
                    startActivity(intent);
                    finish();
                }else
                {
                    //????????????
                }
            }
        };
    }

    void loginEvent()
    {
        if(!idInput.getText().toString().equals("") && !passwordInput.getText().toString().equals("")) {
            firebaseAuth.signInWithEmailAndPassword(idInput.getText().toString(), passwordInput.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //????????? ????????? ????????? ?????????
                            if (!task.isSuccessful()) {
                                //????????? ????????? ??????
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(),"???????????? ??????????????? ??????????????????",Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPermission(){
        /*
        for (String permission : permission_list){
            //?????? ?????? ??????
            int chk = checkCallingOrSelfPermission(permission);
            if(chk == PackageManager.PERMISSION_DENIED){
                //?????? ?????? ????????? ???????????? ?????? ??????
                requestPermissions(permission_list,0);
            }
        }
         */
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},PERMISSION_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
            for(int i=0; i<grantResults.length;i++){
                //?????????
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                }
                else{
                    //????????????
                    Toast.makeText(getApplicationContext(),"??? ????????? ??????????????????",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
