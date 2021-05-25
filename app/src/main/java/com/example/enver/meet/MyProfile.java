package com.example.enver.meet;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class MyProfile extends AppCompatActivity {

    Button button2;
    EditText editText2, editText3, editText4;
    TextView name, email;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("My Profile");
        preferences = getSharedPreferences("user", MODE_PRIVATE);

        button2 = (Button) findViewById(R.id.button2);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

        name.setText(preferences.getString("userName", ""));
        email.setText(name.getText().toString() +  "@gmail.com");

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText2.getText().toString().isEmpty() || editText3.getText().toString().isEmpty() || editText4.getText().toString().isEmpty()){
                    Toast.makeText(MyProfile.this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
                }
                else if(editText2.getText().toString().length()<4 || editText3.getText().toString().length()<4 || editText4.getText().toString().length()<4){
                    Toast.makeText(MyProfile.this, "Fields must be longer than 3 characters.", Toast.LENGTH_SHORT).show();
                }
                else if(!editText4.getText().toString().equals(editText3.getText().toString())){
                    Toast.makeText(MyProfile.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else{

                    BackgroundTask backgroundTask = new MyProfile.BackgroundTask();
                    backgroundTask.execute(editText2.getText().toString(), preferences.getString("userName", ""), editText4.getText().toString());

                }
            }
        });


    }



    class BackgroundTask extends AsyncTask<String, Void, String> {

        String add_info_url;
        @Override
        protected void onPreExecute() {
            add_info_url = "http://139.59.164.205/change_password.php";
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            editText2.setText("");
            editText3.setText("");
            editText4.setText("");

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
                String data_String = URLEncoder.encode("Old","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("New","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
