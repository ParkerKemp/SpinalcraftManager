package com.spinalcraft.manager.client.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spinalcraft.berberos.common.BerberosError.ErrorCode;
import com.spinalcraft.berberos.common.BerberosError;
import com.spinalcraft.easycrypt.messenger.Messenger;
import com.spinalcraft.manager.client.R;
import com.spinalcraft.manager.client.task.LoginTask;
import com.spinalcraft.manager.client.util.Command;
import com.spinalcraft.manager.client.util.FileIO;

/**
 * Created by Parker on 10/12/2015.
 */
public class LoginActivity extends BaseActivity {

    private ProgressBar spinner;
    private LinearLayout progressLayout;
    private String cachedUsername, cachedPassword;
    EditText usernameEdit, passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressLayout = (LinearLayout)findViewById(R.id.progressLayout);
        progressLayout.setVisibility(View.GONE);
//
//        spinner = (ProgressBar)findViewById(R.id.progressBar);
//        spinner.setVisibility(View.GONE);
        usernameEdit = (EditText)findViewById(R.id.username);
        passwordEdit = (EditText)findViewById(R.id.password);
        Button loginButton = (Button)findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(usernameEdit.getText().toString(), passwordEdit.getText().toString());
            }
        });

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin(usernameEdit.getText().toString(), passwordEdit.getText().toString());
                }
                return false;
            }
        });

        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");

        populateFields(username, password);
    }

    @Override
    public void onBackPressed(){
    }

    private void populateFields(String username, String password){
        if(username != null) {
            usernameEdit.setText(username);
            passwordEdit.requestFocus();
            if (password != null) {
                passwordEdit.setText(password);
            }
        }
    }

//    private void cachedLogin(){
//        cachedUsername = FileIO.read(".username", getApplication());
//        cachedPassword = FileIO.read(".password", getApplication());
//
//        if(cachedUsername != null) {
//            usernameEdit.setText(cachedUsername);
//            passwordEdit.requestFocus();
//            if (cachedPassword != null) {
//                passwordEdit.setText(cachedPassword);
//                attemptLogin(cachedUsername, cachedPassword);
//            }
//        }
//    }

    private void attemptLogin(String username, String password){
        progressLayout.setVisibility(View.VISIBLE);
        new LoginTask(username, password, this, new Command() {
            @Override
            public void execute(Object... data) {
                progressLayout.setVisibility(View.GONE);
                Activity activity = (Activity)data[0];
                BerberosError.ErrorCode code = (ErrorCode)data[1];
                if(code == ErrorCode.NONE)
                    grantAccess();
                else if(code == ErrorCode.AUTHENTICATION)
                    passwordEdit.setText("");
            }
        }).execute();
    }

    private void grantAccess(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivityForResult(intent, 0);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        passwordEdit.setText("");
        passwordEdit.requestFocus();
    }
}
