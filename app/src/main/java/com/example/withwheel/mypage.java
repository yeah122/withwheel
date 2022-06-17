package com.example.withwheel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class mypage extends AppCompatActivity {

    private static String TAG = "mypage";

    public String mJsonString;

    TextView textID;
    Button startLogin, startRegister, btnlogout, btnDelete, btnchange, btnBookmark;

    String URL;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preference = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String user = preference.getString("id", "");

        if (user != "") { // 로그인 되어 있으면
            setContentView(R.layout.mypage);
            textID = (TextView) findViewById(R.id.textID);
            btnlogout = (Button) findViewById(R.id.btnLogout);
            btnDelete = (Button) findViewById(R.id.btnMembershipWithdrawal);
            btnchange = (Button) findViewById(R.id.btnChange);
            btnBookmark= (Button) findViewById(R.id.btnBookmark);

            String name = user;

            textID.setText(name);

            btnlogout.setOnClickListener(new View.OnClickListener() {
                SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                @Override
                public void onClick(View view) {

                    editor.remove("id");
                    editor.commit();

                    Toast.makeText(mypage.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mypage.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editText = new EditText(mypage.this);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mypage.this);

                    builder.setTitle("정말 탈퇴하실 건가요?").setMessage("탈퇴를 원하시면 비밀번호를 입력해주세요.");
                    builder.setView(editText);
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String password;
                            password = editText.getText().toString();
                            URL = "Delete";
                            mypage.GetData task = new mypage.GetData();
                            task.execute("http://10.0.2.2/profile_delete.php", name, password);
                        }
                    });
                    builder.show();
                }
            });

            btnchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editText = new EditText(mypage.this);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mypage.this);

                    builder.setTitle("비밀번호 수정").setMessage("현재 비밀번호를 입력해주세요.");
                    builder.setView(editText);
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String password;
                            URL = "change";
                            password = editText.getText().toString();

                            if(!password.equals("")){
                                mypage.GetData task = new mypage.GetData();
                                task.execute("http://10.0.2.2/profile_change_check.php", name, password);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }

            });

            btnBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mypage.this, bookmark.class);
                    startActivity(i);
                }

            });

        }
        else{ // 로그인 되어있지 않으면
            setContentView(R.layout.mypage_logout_ver);

            startLogin = (Button) findViewById(R.id.startLogin);
            startRegister = (Button) findViewById(R.id.startRegister);

            Toast.makeText(mypage.this, "로그인이 필요합니다", Toast.LENGTH_SHORT);

            startLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mypage.this, profile_login.class);
                    startActivity(intent);
                }
            });

            startRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mypage.this, profile_insert.class);
                    startActivity(intent);
                }
            });
        }
    }

    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(mypage.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result) ;

            if (result == null){
                System.out.println(errorString);
            }

            else {
                if(URL.equals("Delete")){ // 회원탈퇴
                    if(result.equals("회원탈퇴에 성공했습니다.")){
                        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.remove("id");
                        editor.commit();

                        i = new Intent(mypage.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{ // 회원탈퇴 실패
                        Toast.makeText(mypage.this, result, Toast.LENGTH_SHORT).show();
                    }

                }
                else if(URL.equals("change")){ //비밀번호 수정
                    if(result.equals("확인")){//비밀번호 수정 완료됐을 때
                        Toast.makeText(mypage.this, "비밀번호가 확인되었습니다.", Toast.LENGTH_SHORT).show();
                        i = new Intent(mypage.this, profile_change.class);
                        startActivity(i);
                    }
                    else{//비번 다를 시
                        Toast.makeText(mypage.this, result, Toast.LENGTH_SHORT).show();
                    }

                }
                //탈퇴도 수정도 아닐 때
                else {
                    Toast.makeText(mypage.this, "오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String userid = (String)params[1];
            String password = (String)params[2];
            String serverURL = (String)params[0];//"http://10.0.2.2/charger.php";
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
}
