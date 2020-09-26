package com.sourcey.materiallogindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity {
    @BindView(R.id.input_forgot_email)
    EditText edtForgotEmail;

    @BindView(R.id.btn_reset)
    Button btnResetPassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String mResetEmail = edtForgotEmail.getText().toString();
        if (!validate(mResetEmail)) {
            return;
        }

        auth.sendPasswordResetEmail(mResetEmail).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                onResetSuccessful();
            } else {
                onResetFail();
            }
        })
        .addOnFailureListener(this, e -> {
            onResetFail();
        });
    }

    private void onResetFail() {
        Toast.makeText(this, "Unable to send reset email", Toast.LENGTH_LONG).show();
    }

    private void onResetSuccessful() {
        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
    }

    private boolean validate(String mResetEmail) {
        boolean valid = true;

        if (mResetEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mResetEmail).matches()) {
            edtForgotEmail.setError("enter a valid email address");
            valid = false;
        } else {
            edtForgotEmail.setError(null);
        }
        return valid;
    }
}
