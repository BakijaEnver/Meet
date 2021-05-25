package com.example.enver.meet;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    Button register;
    TextView login;
    EditText username,password,confirmpassword,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = (TextView) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        email = (EditText) findViewById(R.id.email);
        register = (Button) findViewById(R.id.register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Username field can not be empty.", Toast.LENGTH_SHORT).show();
                }
                else if(email.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Email field can not be empty.", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Password field can not be empty.", Toast.LENGTH_SHORT).show();
                }
                else if(confirmpassword.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Password field can not be empty.", Toast.LENGTH_SHORT).show();
                }
                else if(username.getText().toString().length() < 4 ){
                    Toast.makeText(Register.this, "Username must be longer than 3 characters.", Toast.LENGTH_SHORT).show();
                }
                else if(!isEmailValid(email.getText().toString())){
                    Toast.makeText(Register.this, "Email is not valid.", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().length() < 4 ){
                    Toast.makeText(Register.this, "Password must be longer than 3 characters.", Toast.LENGTH_SHORT).show();
                }
                else if(!password.getText().toString().equals(confirmpassword.getText().toString()) ){
                    Toast.makeText(Register.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else{
                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(username.getText().toString(),email.getText().toString(),password.getText().toString());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        String add_info_url;
        @Override
        protected void onPreExecute() {
            add_info_url = "http://139.59.164.205/insert_user.php";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... arg) {
            String username, email, password;
            username = arg[0];
            email = arg[1];
            password = arg[2];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"),8);
                String data_String = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
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

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

}
