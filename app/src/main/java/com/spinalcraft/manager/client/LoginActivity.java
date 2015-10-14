package com.spinalcraft.manager.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Parker on 10/12/2015.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText password = (EditText)findViewById(R.id.password);
        Button loginButton = (Button)findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                }
                return false;
            }
        });
    }

    private void attemptLogin(){
        EditText usernameEdit = (EditText)findViewById(R.id.username);
        EditText passwordEdit = (EditText)findViewById(R.id.password);

        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        System.out.println("Attempting login.");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        
        new LoginTask().execute(username, password);
    }
}
