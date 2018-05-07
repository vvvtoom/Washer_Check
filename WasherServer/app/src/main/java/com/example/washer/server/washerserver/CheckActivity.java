package com.example.washer.server.washerserver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


public class CheckActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        File file = getFileStreamPath("token.txt");
        try{
            if( file.isFile() == false ) {
                String strBuf = ReadTextAssets("token.txt");
                WriteTextFile("token.txt", strBuf);
            }
        } catch (NullPointerException e){
            WriteTextFile("token.txt", "");
        }

        String str = ReadTextFile("token.txt");

        if(isNetworkStat(this) == false){
            Toast.makeText(this, "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_LONG).show();
            System.exit(0);
        }

        if(str.equals("")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
            Intent intent = new Intent(this, CheckActivity2.class);
            intent.putExtra("token", str);
            startActivity(intent);
            finish();
        }

    }
    public static boolean isNetworkStat( Context context ) {
        ConnectivityManager manager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        boolean blte_4g = false;
        if(lte_4g != null)
            blte_4g = lte_4g.isConnected();
        if( mobile != null ) {
            if (mobile.isConnected() || wifi.isConnected() || blte_4g)
                return true;
        } else {
            if ( wifi.isConnected() || blte_4g )
                return true;
        }
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("네트워크 오류");
        dlg.setMessage("네트워크 상태를 확인해 주십시요.");
        dlg.setIcon(R.drawable.ic_launcher);
        dlg.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        dlg.show();
        return false;
    }

    public String ReadTextAssets(String strFileName) {
        String text = null;
        try {
            InputStream is = getAssets().open(strFileName);
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