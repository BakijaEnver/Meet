package com.example.enver.meet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Chat extends AppCompatActivity {


    TextView name, email;
    Button button2;
    SharedPreferences preferences;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("View Profile");

        b = getIntent().getExtras();
        String username = b.getString("UserName");

        name = (TextView) findViewById(R.id.name);
        button2 = (Button) findViewById(R.id.button2);

        name.setText(username);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


    }
}
