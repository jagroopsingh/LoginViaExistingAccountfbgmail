package com.apps.gill.loginviaexistingaccount.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apps.gill.loginviaexistingaccount.R;
import com.apps.gill.loginviaexistingaccount.models.UserDetails;
import com.apps.gill.loginviaexistingaccount.utils.AlertBox;
import com.apps.gill.loginviaexistingaccount.utils.CommonData;
import com.facebook.Profile;

/**
 * Created by gill on 10-03-2016.
 */
public class RegFirstStage extends BaseActivity implements View.OnClickListener {
    String firstName, lastName, email, gender, picUrl;
    EditText etFirstName, etLastName, etEmail;
    Button btNext, btBack;
    Intent intent;

    public void init() {
        etFirstName = (EditText) findViewById(R.id.et_firstname);
        etLastName = (EditText) findViewById(R.id.et_lastname);
        etEmail = (EditText) findViewById(R.id.et_email);
        btNext = (Button) findViewById(R.id.bt_next);
        btBack = (Button) findViewById(R.id.bt_back);
    }

    public void setValue() {
        if (getIntent().getAction().equals("twitter")) {
//            firstName = CommonData.getUserDetails(RegFirstStage.this).firstName;
//            lastName = CommonData.getUserDetails(RegFirstStage.this).lastName;
//            email = CommonData.getUserDetails(RegFirstStage.this).email;
//            gender = CommonData.getUserDetails(RegFirstStage.this).gender;
//            picUrl = CommonData.getUserDetails(RegFirstStage.this).picUrl;
            picUrl = getIntent().getStringExtra("picUrl");
        } else if (getIntent().getAction().equals("linkedin")) {
            picUrl = getIntent().getStringExtra("picUrl");
            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
            email = getIntent().getStringExtra("email");
        } else if (getIntent().getAction().equals("gmail")) {
            email = getIntent().getStringExtra("Email");
            picUrl = getIntent().getStringExtra("picUrl");
        } else {
            picUrl = getIntent().getStringExtra("picUrl");
            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
            email = getIntent().getStringExtra("email");
            gender = getIntent().getStringExtra("gender");
        }
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etEmail.setText(email);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_firststage);
        if (getIntent().getAction().equals("signup")) {
            init();
        } else {
            init();
            setValue();
        }
        btNext.setOnClickListener(this);
        btBack.setOnClickListener(this);
    }

    private boolean onValidate() {
        boolean isValid = true;
        if (etFirstName.length() == 0) {
            AlertBox.alertDialogShow(RegFirstStage.this, "enter first name");
            firstName = etFirstName.getText().toString();
            return false;
        }
        if (etLastName.length() == 0) {
            AlertBox.alertDialogShow(RegFirstStage.this, "enter last name");
            lastName = etLastName.getText().toString();
            return false;
        }
        if (!(!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            AlertBox.alertDialogShow(RegFirstStage.this, "enter valid email");
            email = etEmail.getText().toString();
            return false;
        }
        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                if (onValidate()) {
                    intent = new Intent(RegFirstStage.this, RegSecondState.class);
                    intent.setAction("RegFirstStage");
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", email);
                    intent.putExtra("gender", gender);
                    intent.putExtra("picUrl", picUrl);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.bt_back:
                intent=new Intent(RegFirstStage.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}
