package ro.pub.cs.systems.eim.lab03.phonedialer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.systems.eim.lab03.phonedialer.util.Constants;

public class PhoneDialerActivity extends AppCompatActivity {
    // Views
    private EditText mEditTextPhoneNumber;
    private ImageButton mImageButtonBackspace;
    private ImageButton mImageButtonCall;
    private ImageButton mImageButtonHangup;
    private Button mButtonStar;
    private Button mButtonHash;
    private List<Button> mButtonsNumbers;

    // Listeners
    private final ButtonNumberListener mButtonNumberListener = new ButtonNumberListener();
    private final ButtonBackspaceListener mButtonBackspaceListener = new ButtonBackspaceListener();
    private final ButtonCallListener mButtonCallListener = new ButtonCallListener();
    private final ButtonHangupListener mButtonHangupListener = new ButtonHangupListener();

    private class ButtonNumberListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String phoneNumberOld = mEditTextPhoneNumber.getText().toString();
            if (phoneNumberOld.length() < Constants.PHONE_NUMBER_MAX_LENGTH) {
                String digit = ((Button) view).getText().toString();
                String phoneNumberNew = phoneNumberOld + digit;
                mEditTextPhoneNumber.setText(phoneNumberNew);
            }
        }
    }

    private class ButtonBackspaceListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String phoneNumberOld = mEditTextPhoneNumber.getText().toString();
            if (phoneNumberOld.length() > 0) {
                String phoneNumberNew = phoneNumberOld.substring(0, phoneNumberOld.length() - 1);
                mEditTextPhoneNumber.setText(phoneNumberNew);
            }
        }
    }

    private class ButtonCallListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        PhoneDialerActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        1);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + mEditTextPhoneNumber.getText().toString()));
                startActivity(intent);
            }
        }
    }

    private class ButtonHangupListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);

        init();
    }

    private void init() {
        mEditTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        mImageButtonBackspace = findViewById(R.id.image_button_backspace);
        mImageButtonCall = findViewById(R.id.image_button_call);
        mImageButtonHangup = findViewById(R.id.image_button_hangup);
        mButtonStar = findViewById(R.id.button_star);
        mButtonHash = findViewById(R.id.button_hash);
        mButtonsNumbers = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            Button buttonNr = findViewById(Constants.BUTTONS_NUMBERS[i]);
            mButtonsNumbers.add(buttonNr);
        }

        setListeners();
    }

    private void setListeners() {
        mImageButtonBackspace.setOnClickListener(mButtonBackspaceListener);
        mImageButtonCall.setOnClickListener(mButtonCallListener);
        mImageButtonHangup.setOnClickListener(mButtonHangupListener);
        mButtonStar.setOnClickListener(mButtonNumberListener);
        mButtonHash.setOnClickListener(mButtonNumberListener);
        for (int i = 0; i <= 9; i++) {
            mButtonsNumbers.get(i).setOnClickListener(mButtonNumberListener);
        }
    }
}