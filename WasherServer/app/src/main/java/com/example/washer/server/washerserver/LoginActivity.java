package com.example.washer.server.washerserver;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


public class LoginActivity extends ActionBarActivity {
    Button loginBtn, registerBtn;
    EditText idText, pwdText;
    String respond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

                loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        idText = (EditText)findViewById(R.id.idText);
        pwdText = (EditText)findViewById(R.id.pwdText);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idText.getText().toString() == "" || pwdText.getText().toString() == ""){
                    Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 비었습니다.", Toast.LENGTH_LONG).show();
                } else {
                    String url = "http://218.54.46.89:3000/user/login";
                    HttpClient http = new DefaultHttpClient();
                    try {

                        ArrayList<NameValuePair> nameValuePairs =
                                new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("id", idText.getText().toString()));
                        nameValuePairs.add(new BasicNameValuePair("pw", pwdText.getText().toString()));

                        HttpParams params = http.getParams();
                        HttpConnectionParams.setConnectionTimeout(params, 5000);
                        HttpConnectionParams.setSoTimeout(params, 5000);

                        HttpPost httpPost = new HttpPost(url);
                        UrlEncodedFormEntity entityRequest =
                                new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

                        httpPost.setEntity(entityRequest);

                        HttpResponse responsePost = http.execute(httpPost);
                        HttpEntity resEntity = responsePost.getEntity();
                        respond = EntityUtils.toString(resEntity);
                        JSONObject obj = new JSONObject(respond);
                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_LONG).show();
                        if(obj.getString("return").equals("200")){
                            JSONArray arr = obj.getJSONArray("data");
                            JSONObject secObj = arr.getJSONObject(0);
                            String token = secObj.getString("token");
                            WriteTextFile("token.txt", token);
                            Intent intent = new Intent(getApplicationContext(), CheckActivity2.class);
                            intent.putExtra("token", token);
                            startActivity(intent);
                            finish();
                        }
                    }catch(Exception e){e.printStackTrace();}
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
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
}
