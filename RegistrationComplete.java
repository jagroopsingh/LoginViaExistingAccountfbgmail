package com.apps.gill.loginviaexistingaccount.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apps.gill.loginviaexistingaccount.R;
import com.apps.gill.loginviaexistingaccount.utils.CommonData;

/**
 * Created by gill on 13-03-2016.
 */
public class RegistrationComplete extends AppCompatActivity {
    TextView tvName, tvEmail;
    Button btLogOut,btBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration_complete);
        tvName=(TextView) findViewById(R.id.tv_name);
        tvEmail=(TextView) findViewById(R.id.tv_email);
        btLogOut=(Button) findViewById(R.id.bt_logout);
        btBack=(Button) findViewById(R.id.bt_back);
        btBack.setVisibility(View.GONE);
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLogOutAlert();
            }
        });
    }
    private void displayLogOutAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistrationComplete.this);
        alert.setTitle("Alert!!");
        alert.setMessage("Sure to Log Out");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonData.clearAllAppData(RegistrationComplete.this);
                Intent intent = new Intent(RegistrationComplete.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}

