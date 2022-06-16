package com.example.withwheel;

import android.app.ProgressDialog;
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

    private static final String TAG_JSON = "persons";
    private static final String TAG_ID = "userid";
    private static final String TAG_PASS = "password";
    private static final String TAG_NICKNAME = "nickname";

    private TextView mTextViewResult;
    private Button button_change;
    private EditText editNickname, editPass;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mListViewList;
    public String mJsonString;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*SharedPreferences preference = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String user = preference.getString("id", "");*/

        setContentView(R.layout.profile_change);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        editNickname = (EditText) findViewById(R.id.editText_main_nickname_change);
        editPass = (EditText) findViewById(R.id.editText_main_Password_change);

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        editor = preferences.edit();
        userid = preferences.getString("id", "");

        button_change = findViewById(R.id.button_main_insert_change);
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), userid + "  " + editNickname.getText().toString()
                        + "  " + editPass.getText().toString(), Toast.LENGTH_SHORT).show();
                profile_change.GetData task = new profile_change.GetData();
                task.execute("http://10.0.2.2/change1.php", userid, editNickname.getText().toString(),
                        editPass.getText().toString());
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
                    "Please Wait", "잠시만 기다려주세요.", true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(progressDialog != null){
                progressDialog.dismiss();
            }

            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;

                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

                String userid= (String) params[1];
                String nickname = (String) params[2];
                String password = (String) params[3];

                String serverURL = (String) params[0];//"http://10.0.2.2/login.php";
                String postParameters = "userid=" + userid + "&nickname =" + nickname + "&password =" + password;

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
        Toast.makeText(getApplicationContext(), mJsonString, Toast.LENGTH_SHORT).show();

        /*
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                AlertDialog.Builder builder = new AlertDialog.Builder(change.this);
                dialog = builder.setMessage(userid + "님 정보가 수정되었습니다.")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), mypage.class);
                                intent.putExtra("loginID", editNickname.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        }).create();
                dialog.show();

                return;
         */
    }
}