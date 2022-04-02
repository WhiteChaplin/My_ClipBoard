package com.example.my_clipboard;

import android.Manifest;
import android.content.DialogInterface;
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
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class SignUpActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText EditEmail,EditTextPass,EditTextName,EditTextPhone;
    Button btnCreate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        EditEmail = (EditText)findViewById(R.id.EditEmail);
        EditTextPass = (EditText)findViewById(R.id.EditTextPass);
        EditTextName = (EditText)findViewById(R.id.EditTextName);
        EditTextPhone = (EditText)findViewById(R.id.EditTextPhone);
        btnCreate = (Button)findViewById(R.id.btn_create);
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final String Email = EditEmail.getText().toString();
        final String Name = EditTextName.getText().toString();
        final String Phone = EditTextPhone.getText().toString();
        //final String NicName = EditTextNicName.getText().toString();
        final String Password = EditTextPass.getText().toString();


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EditEmail.getText().toString().equals("") && !EditTextPass.getText().toString().equals("") && !EditTextName.getText().toString().equals("") &&
                        !EditTextPhone.getText().toString().equals("")){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(EditEmail.getText().toString(), EditTextPass.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                    builder.setTitle("회원가입 완료!");
                                    builder.setMessage("회원가입이 완료 되었습니다! 확인 버튼을 누르면 자동으로 로그인이 됩니다.");
                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            UserInfo userInfo = new UserInfo();
                                            userInfo.userEmail = Email;
                                            userInfo.userName = Name;
                                            //userInfo.userNicName = NicName;
                                            userInfo.userPhone = Phone;

                                            userInfo.setUserName(EditTextName.getText().toString());
                                            userInfo.setUserEmail(EditEmail.getText().toString());
                                            userInfo.setUserPhone(EditTextPhone.getText().toString());
                                            //userInfo.setUserNicName(EditTextNicName.getText().toString());

                                            String uid = task.getResult().getUser().getUid();
                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userInfo);

                                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }
                            });
                }
                else{
                    Toast.makeText(getApplicationContext(),"모든 부분을 다 채워주세요!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
