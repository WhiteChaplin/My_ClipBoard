package com.example.my_clipboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.core.content.SharedPreferencesCompat;

public class DialogCustom extends Dialog{
    private Context context;
    private MyDialogListener myDialogListener;
    String ClipBoardName;
    String ClipBoard;
    String dialogMessage = "OK";

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase = null;
    DatabaseReference myRef = null;
    String PhoneNum,setDate="";
    private String UID=null;
    SharedPreferences sharedPreferences;
    SharedPreferences sf;
    SharedPreferences.Editor editor;

    public DialogCustom(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void setDialogListener(MyDialogListener myDialogListener){
        this.myDialogListener = myDialogListener;
    }


    public void callDialog()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.show();

        final EditText EditClipBoardName = (EditText) dialog.findViewById(R.id.EditClipBoardName);
        final EditText EditClipBoard = (EditText) dialog.findViewById(R.id.EditClipBoard);
        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        final Button btnAddClipBoard = (Button) dialog.findViewById(R.id.btnAddClipBoard);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UID = firebaseUser.getUid().toString();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("users").child(UID.toString());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAddClipBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EditClipBoard.getText().toString().equals("") && !EditClipBoardName.getText().toString().equals(""))
                {
                    ClipBoard clipBoard = new ClipBoard();
                    clipBoard.clipBoardName = EditClipBoardName.getText().toString();
                    clipBoard.clipBoard = EditClipBoard.getText().toString();

                    FirebaseDatabase.getInstance().getReference().child("users").child(UID).push().child(EditClipBoardName.getText().toString()).setValue(clipBoard)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful())
                                    {
                                        Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(context,"추가 완료",Toast.LENGTH_SHORT).show();
                                        //메인으로 리스너를 보냄
                                        myDialogListener.onPositiveClicked("OK");
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(context,"누락된 공간이 있습니다",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}




