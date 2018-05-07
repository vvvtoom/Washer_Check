package com.example.washer.client.washerclient;

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
import org.json.JSONObject;

import java.util.ArrayList;


public class RegisterActivity extends ActionBarActivity {
    Button submitBtn, cancelBtn;
    EditText idText, pwdText, nameText, phoneText;
    static String respond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        submitBtn = (Button)findViewById(R.id.submitBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        idText = (EditText)findViewById(R.id.idText);
        pwdText = (EditText)findViewById(R.id.pwdText);
        nameText = (EditText)findViewById(R.id.nameText);
        phoneText = (EditText)findViewById(R.id.phoneText);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idText.getText().toString() == "" || pwdText.getText().toString() == "" ||
                        phoneText.getText().toString() == "" || nameText.getText().toString() == ""){
                    Toast.makeText(getApplicationContext(), "모든 칸을 채워주세요.", Toast.LENGTH_LONG).show();
                } else {
                    String url = "http://218.54.46.89:3000/user/register";
                    HttpClient http = new DefaultHttpClient();
                    try {

                        ArrayList<NameValuePair> nameValuePairs =
                                new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("id", idText.getText().toString()));
                        nameValuePairs.add(new BasicNameValuePair("pw", pwdText.getText().toString()));
                        nameValuePairs.add(new BasicNameValuePair("name", nameText.getText().toString()));
                        nameValuePairs.add(new BasicNameValuePair("phonenumber", phoneText.getText().toString()));

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
                            finish();
                        }
                    }catch(Exception e){e.printStackTrace();}
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
