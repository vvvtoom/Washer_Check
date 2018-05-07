package com.example.washer.server.washerserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;


public class CheckActivity2 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check2);
        Intent intent1 = getIntent();
        String token = intent1.getExtras().getString("token");
        File file = getFileStreamPath("idx.txt");
        if (file.isFile() == false) {
            String strBuf = ReadTextAssets("idx.txt");
            WriteTextFile("idx.txt", strBuf);
        }

        String str = ReadTextFile("idx.txt");

        if (str.equals("")) {
            Intent intent = new Intent(this, RegisterActivity2.class);
            intent.putExtra("token", token);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("idx", str);
            intent.putExtra("token", token);
            startActivity(intent);
            finish();
        }

    }

    public String ReadTextAssets(String strFileName) {
        String text = null;
        try {
            InputStream is = getApplicationContext().getAssets().open(strFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            text = new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return text;
    }

    public boolean WriteTextFile(String strFileName, String strBuf) {
        try {
            File file = getFileStreamPath(strFileName);
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(strBuf);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public String ReadTextFile(String strFileName) {
        String text = null;
        try {
            File file = getFileStreamPath(strFileName);
            FileInputStream fis = new FileInputStream(file);
            Reader in = new InputStreamReader(fis);
            int size = fis.available();
            char[] buffer = new char[size];
            in.read(buffer);
            in.close();

            text = new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return text;
    }
}
