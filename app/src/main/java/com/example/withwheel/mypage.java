package com.example.withwheel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
import java.util.concurrent.atomic.DoubleAccumulator;

public class mypage extends AppCompatActivity {

    private static String TAG = "mypage";
    String place_address;
    private AlertDialog dialog;
    private TextView mTextViewResult;
    ArrayList<locationData> mArrayList;

    public String mJsonString;

    TextView textID;
    Button startLogin, startRegister, btnlogout, btnDelete, btnchange;
    SharedPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preference = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String user = preference.getString("id", "");

        if (user != "") {
            setContentView(R.layout.activity_mypage);
            textID = (TextView) findViewById(R.id.textID);
            btnlogout = (Button) findViewById(R.id.btnLogout);
            btnDelete = (Button) findViewById(R.id.btnMembershipWithdrawal);
            btnchange = (Button) findViewById(R.id.btnChange);

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(mypage.this);

                    builder.setTitle("진짜 탈퇴할 거면 비번 입력 ㄱ");
                    builder.setView(editText);
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String password;
                            password = editText.getText().toString();
                            mypage.GetData task = new mypage.GetData();
                            task.execute("http://10.0.2.2/Delete.php", name, password);
                        }
                    });
                    builder.show();
                }
            });

            btnchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editText = new EditText(mypage.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mypage.this);

                    builder.setTitle("진짜 수정할 거면 비번 입력 ㄱ");
                    builder.setView(editText);
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String password;

                            password = editText.getText().toString();
                            mypage.GetData task = new mypage.GetData();
                            task.execute("http://10.0.2.2/change.php", name, password);

                        }
                    });
                    builder.show();
                }

            });

        }
        else{
            setContentView(R.layout.activity_mypage_logout_ver);

            startLogin = (Button) findViewById(R.id.startLogin);
            startRegister = (Button) findViewById(R.id.startRegister);

            Toast.makeText(mypage.this, "로그인이 필요합니다", Toast.LENGTH_SHORT);

            startLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mypage.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            startRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mypage.this, RegisterActivity.class);
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
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result) ;

            if (result == null){
                mTextViewResult.setText(errorString);
            }

            else {
                //mTextViewResult.setText(result);
                Toast.makeText(mypage.this, result, Toast.LENGTH_SHORT).show();

                if(result.equals("회원탈퇴에 성공했습니다.")){
                    SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.remove("id");
                    editor.commit();

                    Intent i = new Intent(mypage.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                if(result.equals("비번 확인^^")){
                    Intent i = new Intent(mypage.this, change.class);
                    startActivity(i);
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
