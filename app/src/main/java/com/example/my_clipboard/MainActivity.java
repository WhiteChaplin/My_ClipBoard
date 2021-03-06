package com.example.my_clipboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnAdd;
    Button btnLogOut;
    ListView user_ClipBoard_list;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase = null;
    DatabaseReference databaseReference = null;
    DatabaseReference myWorkInfoRef = null;
    String UID;
    final static int PERMISSION_CODE=1;
    private ArrayList<ClipBoard> clipBoard = new ArrayList<ClipBoard>();
    String[] permission_list = {
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnLogOut = (Button)findViewById(R.id.btn_logOut);
        user_ClipBoard_list = (ListView)findViewById(R.id.user_ClipBoard_list);
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        UID = firebaseUser.getUid();

        //?????? ?????? ??????
        checkPermission();


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        myWorkInfoRef = firebaseDatabase.getReference("users").child(UID.toString());
        myWorkInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                        //?????? ????????? ?????? ????????? ????????????
                        clipBoard.add(new ClipBoard(snapshot2.child("clipBoardName").getValue(String.class),snapshot2.child("clipBoard").getValue(String.class)));
                        ClipBoardListAdapter adapter = new ClipBoardListAdapter(getApplicationContext(),R.layout.clipboard_list_view,clipBoard);
                        user_ClipBoard_list.setAdapter(adapter);
                        user_ClipBoard_list.setClickable(true);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom dialogCustom = new DialogCustom(MainActivity.this);
                //DialogCustom?????? ?????? ???????????? ?????? ??????.MyDialogListener??? ?????? ??????
                dialogCustom.setDialogListener(new MyDialogListener() {
                    @Override
                    public void onPositiveClicked(String message) {
                        //onPostiveClicked??? ???????????? ?????? ??????????????? ???????????????
                        Intent intent = getIntent();
                        finish(); //?????? ???????????? ?????? ??????
                        overridePendingTransition(0, 0); //????????? ??????????????? ?????????
                        startActivity(intent); //?????? ???????????? ????????? ??????
                        overridePendingTransition(0, 0); //????????? ??????????????? ?????????
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                dialogCustom.callDialog();
            }
        });


        user_ClipBoard_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        user_ClipBoard_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

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
