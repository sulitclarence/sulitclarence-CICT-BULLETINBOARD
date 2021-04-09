package com.group2.cicitbulletinboardtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginForm extends AppCompatActivity {
    EditText _txtUser, _txtPass;
    Button _btnLogin;
    Spinner _spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        _txtPass=(EditText)findViewById(R.id.txtPass);
        _txtUser=(EditText)findViewById(R.id.txtUser);
        _btnLogin=(Button)findViewById(R.id.btnLogin);
        _spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usertype, R.layout.support_simple_spinner_dropdown_item);
        _spinner.setAdapter(adapter);


        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = _spinner.getSelectedItem().toString();
                if(_txtUser.getText().toString().equals("Admin")&& _txtPass.getText().toString().equals("Admin")&& item.equals("Admin")){
                    Intent intent = new Intent(LoginForm.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginForm.this, "Welcome Admin", Toast.LENGTH_SHORT).show();

                }else if(_txtUser.getText().toString().equals("")&& _txtPass.getText().toString().equals("")&& item.equals("Student")){

                    Intent intent = new Intent(LoginForm.this, UsersView.class);
                    startActivity(intent);
                    Toast.makeText(LoginForm.this, "Welcome Student", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}