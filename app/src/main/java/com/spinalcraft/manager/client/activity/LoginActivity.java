package com.spinalcraft.manager.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
public class LoginActivity extends Activity {

    private String cachedUsername, cachedPassword;
    EditText usernameEdit, passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Messenger.shouldShowDebug = true;

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

        cachedLogin();
    }

    private void cachedLogin(){
        cachedUsername = FileIO.read(".username", this);
        cachedPassword = FileIO.read(".password", this);

        if(cachedUsername != null) {
            usernameEdit.setText(cachedUsername);
            passwordEdit.requestFocus();
            if (cachedPassword != null) {
                passwordEdit.setText(cachedPassword);
                attemptLogin(cachedUsername, cachedPassword);
            }
        }
    }

    private void attemptLogin(String username, String password){
        new LoginTask(username, password, this, new Command() {
            @Override
            public void execute(Object... data) {
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        passwordEdit.setText("");
        passwordEdit.requestFocus();
    }
}
