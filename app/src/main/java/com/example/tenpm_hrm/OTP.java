package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.SecureRandom;
import java.util.Properties;

public class OTP extends AppCompatActivity {

    private Button btnConfirm;
    private EditText etOTP;
    private DatabaseHandler databaseHandler; // Initialize the database handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp);

        btnConfirm = findViewById(R.id.btnConfirm);
        etOTP = findViewById(R.id.etOTP);

        databaseHandler = new DatabaseHandler(this); // Initialize database handler

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String email = getIntent().getStringExtra("email");
        String correctOTP = getIntent().getStringExtra("otp");

        btnConfirm.setOnClickListener(v -> {
            String enteredOTP = etOTP.getText().toString().trim();
            if (enteredOTP.equals(correctOTP)) {
                resetPassword(email);
                Intent intent = new Intent(OTP.this, Login.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(OTP.this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword(String email) {
        String newPassword = generateRandomPassword();
        boolean isUpdated = databaseHandler.updatePassword(email, newPassword);

        if (isUpdated) {
            sendEmail(email, newPassword);
            Toast.makeText(this, "Đã gửi mật khẩu mới qua Email của bạn", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không thể cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateRandomPassword() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    private void sendEmail(String email, String newPassword) {
        new Thread(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication("storetenpm@gmail.com", "aqyu dylx cyyb vudh"); // Replace with actual credentials
                    }
                });

                javax.mail.Message message = new javax.mail.internet.MimeMessage(session);
                message.setFrom(new javax.mail.internet.InternetAddress("storetenpm@gmail.com"));
                message.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(email));
                message.setSubject("New Password");
                message.setText("Your new password is: " + newPassword);

                javax.mail.Transport.send(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
