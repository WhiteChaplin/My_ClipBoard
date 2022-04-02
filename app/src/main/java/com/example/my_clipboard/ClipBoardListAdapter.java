package com.example.my_clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class ClipBoardListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<ClipBoard> arrayList;
    private int layout;
    private Context context;

    public ClipBoardListAdapter(Context context, int layout, ArrayList<ClipBoard> arrayList)
    {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
        this.layout = layout;
        this.context = context;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = inflater.inflate(layout,parent,false);
        }
        ClipBoard clipBoard = arrayList.get(position);

        convertView.setClickable(false);

        TextView textClipTitle = (TextView)convertView.findViewById(R.id.text_list_clip_name);
        textClipTitle.setText(clipBoard.getClipBoardName());

        EditText editClipBoard = (EditText)convertView.findViewById(R.id.edit_list_clip);
        editClipBoard.setText(clipBoard.getClipBoard());

        Button btnCopy = (Button)convertView.findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("CODE",editClipBoard.getText().toString()); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(context,"클립보드 복사 OK",Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
