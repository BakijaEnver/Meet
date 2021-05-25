package com.example.enver.meet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    Button login;
    EditText username, password;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (TextView) findViewById(R.id.register);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Username field can not be empty.", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Password field can not be empty.", Toast.LENGTH_SHORT).show();
                }
                else {
                    BackgroundTask backgroundTask = new Login.BackgroundTask();
                    backgroundTask.execute(username.getText().toString(), password.getText().toString());

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        String check_info_url;
        @Override
        protected void onPreExecute() {
            check_info_url = "http://139.59.164.205/login_user.php";
        }

        @Override
        protected void onPostExecute(String result) {
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

                String s = Character.toString(result.charAt(0));
                if(s.equals("W")){
                    SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                    preferences.edit().putString("userName", username.getText().toString()).apply();
                    preferences.edit().putString("userPass", password.getText().toString()).apply();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), MainNetwork_New.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    }, 1500);
                }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... arg) {
            String username, email, password;
            username = arg[0];
            password = arg[1];
            try {
                URL url = new URL(check_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"),8);
                String data_String = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data_String);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="", line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
