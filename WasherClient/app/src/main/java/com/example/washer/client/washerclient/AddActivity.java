package com.example.washer.client.washerclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddActivity extends ActionBarActivity {
    EditText idxText, washerName;
    Button addBtn, cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        idxText = (EditText)findViewById(R.id.idxText);
        washerName = (EditText)findViewById(R.id.washerName);
        addBtn = (Button)findViewById(R.id.addBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("idx", idxText.getText().toString());
                resultIntent.putExtra("washerName", washerName.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });
    }
}
