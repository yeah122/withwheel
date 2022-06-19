package com.example.withwheel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class profile_insert extends AppCompatActivity
{
    private static String IP_ADDRESS = "192.168.219.102";//다음 줄에 있는 IP 주소를 아파치 웹서버가 설치된  컴퓨터의 IP
    private static String TAG = "withwheel";//

    private EditText mEditTextID;
    private EditText mEditTextPassword;
    private EditText mEditTextPassword2;

    String result_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_register);

        mEditTextID = (EditText)findViewById(R.id.editText_main_name);
        mEditTextPassword = (EditText)findViewById(R.id.editText_main_Password);
        mEditTextPassword2 = (EditText) findViewById(R.id.editText_main_Password2);

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userid = mEditTextID.getText().toString();
                String password = mEditTextPassword.getText().toString();
                String password2 = mEditTextPassword2.getText().toString();

                if(userid.equals("") || userid == null){ //아이디 입력 안 됐으면
                    Toast.makeText(profile_insert.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {//아이디 입력 됐으면
                    //비밀번호가 빈 칸이 아니면
                    if(!password.equals("") && !password2.equals("")){
                        //비밀번호가 6자 이상이면
                        if(password.length() >= 6){
                            // 비밀번호가 같으면
                            if(password.equals(password2)){
                                InsertData task = new InsertData();
                                task.execute("http://" + IP_ADDRESS + "/insert.php", userid, password);
                            }

                            else {//비밀번호가 다르면
                                Toast.makeText(profile_insert.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(profile_insert.this, "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(profile_insert.this, "비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(profile_insert.this,
                    "잠시만 기다려주세요.", null, true, false);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "POST response  - " + result);

            if (result == null){
                System.out.println(result);
            }
            else {
                result_message = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String userid = (String)params[1];
            String password = (String)params[2];

            String serverURL = (String)params[0];
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
                String line = null;

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

        if (result_message.equals("회원가입에 성공하였습니다.")){

            mEditTextID.setText("");
            mEditTextPassword.setText("");
            mEditTextPassword2.setText("");

            AlertDialog.Builder builder = new AlertDialog.Builder(profile_insert.this);
            builder.setTitle("회원가입에 성공하였습니다.").setMessage("환영합니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("startTab", 4);
                    startActivity(intent);
                    finish();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            Toast.makeText(profile_insert.this, result_message, Toast.LENGTH_SHORT).show();
        }


        return;
    }
}