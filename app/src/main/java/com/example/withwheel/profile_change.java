package com.example.withwheel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class profile_change extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static String TAG = "change";

    private Button button_change;
    private EditText editPass1, editPass2;
    public String mJsonString;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_change);

        editPass1 = (EditText) findViewById(R.id.Password_change1);
        editPass2 = (EditText) findViewById(R.id.Password_change2);

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = preferences.edit();
        userid = preferences.getString("id", "");

        button_change = findViewById(R.id.button_main_insert_change);
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 비밀번호가 모두 입력이 되었다면
                if(!editPass1.getText().toString().equals("") && !editPass2.getText().toString().equals("")){
                    // 비밀번호가 같으면
                    if(editPass1.getText().toString().equals(editPass2.getText().toString())){
                        profile_change.GetData task = new profile_change.GetData();
                        task.execute("http://192.168.219.102/profile_change.php", userid, editPass1.getText().toString());
                    }
                    else {//비밀번호가 다르면
                        Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(profile_change.this,
                    "잠시만 기다려주세요.", null, true, false);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(progressDialog != null){
                progressDialog.dismiss();
            }
            Log.d(TAG, "response - " + result);

            if (result == null){
                System.out.println(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

                String userid= (String) params[1];
                String password = (String) params[2];

                String serverURL = (String) params[0];
                String postParameters = "userid=" + userid + "&password=" + password;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    private void showResult(){
        if(mJsonString.equals("확인")){
            Toast.makeText(getApplicationContext(), "비밀번호가 수정되었습니다.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(profile_change.this, MainActivity.class);
            intent.putExtra("startTab", 4);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), mJsonString, Toast.LENGTH_SHORT).show();
        }

    }

}