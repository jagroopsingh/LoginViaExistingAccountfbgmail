package com.apps.gill.loginviaexistingaccount.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.apps.gill.loginviaexistingaccount.R;
import com.apps.gill.loginviaexistingaccount.models.UserDetails;
import com.apps.gill.loginviaexistingaccount.utils.AlertBox;
import com.apps.gill.loginviaexistingaccount.utils.CommonData;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

/**
 * Created by gill on 11-03-2016.
 */
public class RegSecondState extends BaseActivity implements View.OnClickListener {
    private static final int SELECT_FILE = 1;
    String gender, imageUrl, phoneNumber, locality, city, state, pincode,firstName,lastName,email;
    EditText etGender, etPhoneNumber, etLocality, etCity, etState, etPincode;
    ImageView ivProfilePic;
    Button btFinish, btBack,btUpload;
    Intent intent;

    public void init() {
        etCity = (EditText) findViewById(R.id.et_city);
        etState = (EditText) findViewById(R.id.et_state);
        etGender = (EditText) findViewById(R.id.et_gender);
        etPincode = (EditText) findViewById(R.id.et_pincode);
        etLocality = (EditText) findViewById(R.id.et_locality);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        ivProfilePic = (ImageView) findViewById(R.id.iv_profilepic);
        btBack = (Button) findViewById(R.id.bt_back);
        btFinish = (Button) findViewById(R.id.bt_finish);
        btUpload = (Button) findViewById(R.id.bt_upload);
    }

    public void getValue() {
            gender=getIntent().getStringExtra("gender");
            imageUrl=getIntent().getStringExtra("picUrl");
            firstName=getIntent().getStringExtra("firstName");
            lastName=getIntent().getStringExtra("lastName");
            email=getIntent().getStringExtra("email");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_secondstage);
        init();
        getValue();
        etGender.setText(gender);
        Picasso.with(this)
                .load(imageUrl)
                .into(ivProfilePic);
        btFinish.setOnClickListener(this);
        btBack.setOnClickListener(this);
        btUpload.setOnClickListener(this);
    }

    private boolean onValidate() {
        boolean isValid = true;
        if (ivProfilePic == null) {
            AlertBox.alertDialogShow(RegSecondState.this, "upload image");
            return false;
        }
        if (etGender.length() == 0) {
            AlertBox.alertDialogShow(RegSecondState.this, "enter gender");
            gender = etGender.getText().toString();
            return false;
        }
        if (etPhoneNumber.length() == 0) {
            AlertBox.alertDialogShow(RegSecondState.this, "enter phone number");
            phoneNumber = etPhoneNumber.getText().toString();
            return false;
        }
        if (etLocality.length() == 0) {
            AlertBox.alertDialogShow(RegSecondState.this, "enter locality");
            locality = etLocality.getText().toString();
            return false;
        }
        if (etCity.length() == 0) {
            AlertBox.alertDialogShow(RegSecondState.this, "enter city");
            city = etCity.getText().toString();
            return false;
        }
        if (etState.length() == 0) {
            AlertBox.alertDialogShow(RegSecondState.this, "enter state");
            state = etState.getText().toString();
            return false;
        }
        if (etPincode.length() == 0) {
            AlertBox.alertDialogShow(RegSecondState.this, "enter pincode");
            pincode = etPincode.getText().toString();
            return false;
        }
        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_finish:
                if (onValidate()) {
                    saveData();
                    intent = new Intent(RegSecondState.this, RegistrationComplete.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.bt_back:
                intent = new Intent(RegSecondState.this, RegFirstStage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_upload:
                selectImage();
                break;
        }
    }

    private void selectImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_FILE);
    }

    private void saveData() {
        UserDetails userDetails = new UserDetails(firstName,lastName,phoneNumber,email,locality, city, state, gender, pincode,imageUrl);
        CommonData.setUserDetails(userDetails, RegSecondState.this);
        ProjectSQLiteDatabase projectSQLiteDatabase=new ProjectSQLiteDatabase(RegSecondState.this);
        projectSQLiteDatabase.addContact(userDetails);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE) {
            Uri selectedImageUri = data.getData();
            Picasso.with(this)
                    .load(selectedImageUri)
                    .into(ivProfilePic);
        }
    }
}
