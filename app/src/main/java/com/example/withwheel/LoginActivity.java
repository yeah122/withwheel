package com.example.withwheel;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "login";

    private static final String TAG_JSON = "person";
    private static final String TAG_ID = "userid";
    private static final String TAG_PASS = "password";

    ArrayList<HashMap<String, String>> mArrayList;
    private EditText mEditTextID, mEditTextPass;
    Button btn_login, btn_register;
    public String mJsonString;
    private AlertDialog dialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mEditTextID = (EditText) findViewById(R.id.et_id);
        mEditTextPass = (EditText) findViewById(R.id.et_pass);

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = preferences.edit();

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mArrayList.clear();
                GetData task = new GetData();
                task.execute("http://10.0.2.2/login.php", mEditTextID.getText().toString(), mEditTextPass.getText().toString());
            }
        });
        mArrayList = new ArrayList<>();
    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
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

            String userid = (String)params[1];
            String password = (String)params[2];

            String serverURL = (String)params[0];//"http://10.0.2.2/login.php";
            String postParameters = "userid=" + userid + "&password=" + password ;


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

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            //JSONArray jsonArray = new JSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String userid = item.getString(TAG_ID);
                String password = item.getString(TAG_PASS);

                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_ID, userid);
                hashMap.put(TAG_PASS, password);

                mArrayList.add(hashMap);

                editor.putString("id", userid);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                dialog = builder.setMessage(userid + "님 로그인 되었습니다.")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })

                        .create();
                dialog.show();


                return;
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }




}