package com.example.washer.client.washerclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.List;


public class MainActivity extends ActionBarActivity {
    TextView statusTv;
    ListView listView;
    Button addBtn, delBtn, addStackBtn, delStackBtn;
    ArrayAdapter adapter;
    DBContactHelper db;
    String select_input;
    String [] arr = new String[2];
    List<Contact> contactList;
    Intent intent;
    static String token;
    static String respond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        token = intent.getExtras().getString("token");
        statusTv = (TextView)findViewById(R.id.statusTv);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item);
        addBtn = (Button)findViewById(R.id.addBtn);
        delBtn = (Button)findViewById(R.id.delBtn);
        addStackBtn = (Button)findViewById(R.id.addStackBtn);
        delStackBtn = (Button)findViewById(R.id.delStackBtn);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view.findViewById(R.id.list_item);
                select_input = tv.getText().toString();
                arr = select_input.split(" | ");

                String url = "http://218.54.46.89:3000/washer/getstatus";
                HttpClient http = new DefaultHttpClient();
                try {

                    ArrayList<NameValuePair> nameValuePairs =
                            new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("token", token));
                    nameValuePairs.add(new BasicNameValuePair("washeridx", arr[1]));

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
                        if(obj.getString("data").equals("0")){
                            statusTv.setText("노는 중");
                        } else {
                            statusTv.setText("동작 중");
                        }
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });
        db = new DBContactHelper(this);
        contactList = db.getAllContacts();
        for(int i=0;i<db.getContactsCount();i++){
            adapter.add(contactList.get(i).getName() + " | " + contactList.get(i).getIdx());
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRadio();
            }
        });
        addStackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://218.54.46.89:3000/washer/reserve";
                HttpClient http = new DefaultHttpClient();
                try {

                    ArrayList<NameValuePair> nameValuePairs =
                            new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("token", token));
                    nameValuePairs.add(new BasicNameValuePair("washeridx", arr[1]));

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
                }catch(Exception e){e.printStackTrace();}
            }
        });
        delStackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://218.54.46.89:3000/washer/reservecancel";
                HttpClient http = new DefaultHttpClient();
                try {

                    ArrayList<NameValuePair> nameValuePairs =
                            new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("token", token));
                    nameValuePairs.add(new BasicNameValuePair("washeridx", arr[1]));

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
                }catch(Exception e){e.printStackTrace();}
            }
        });
    }
    private void DialogRadio(){
        final CharSequence[] PhoneModels = new String[db.getContactsCount()];
        for(int i=0;i<adapter.getCount();i++){
            PhoneModels[i] = adapter.getItem(i).toString();
        }
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("삭제할 세탁기를 선택하시오.");
        alt_bld.setSingleChoiceItems(PhoneModels, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Contact contact = new Contact();
                String[] input = PhoneModels[item].toString().split(" | ");
                contact.setName(input[0]);
                db.deleteContact(contact);
                adapter.remove(adapter.getItem(item));
                // dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 1 :
                if(resultCode == Activity.RESULT_OK){
                    int idx = Integer.parseInt(data.getExtras().getString("idx"));
                    String washerName = data.getExtras().getString("washerName");
                    Contact contact = new Contact(idx, washerName);
                    db.addContact(contact);
                    adapter.add(idx + " | " + washerName);
                }
                break;
        }
    }
}
