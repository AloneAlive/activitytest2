package com.example.ts.activitytest2;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class e_LoginActivity extends e_BaseActivity {

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e__login);

        accountEdit = findViewById(R.id.e_login_account);
        passwordEdit = findViewById(R.id.e_login_password);
        login = findViewById(R.id.e_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                //admin 123
                if (account.equals("admin") && password.equals("123")) {
                    Toast.makeText(e_LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(e_LoginActivity.this, e_MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(e_LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

