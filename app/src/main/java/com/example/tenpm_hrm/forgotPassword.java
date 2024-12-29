package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class forgotPassword extends AppCompatActivity {

    private Button btnConfirm;
    private EditText etEmail;
    private DatabaseHandler databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        btnConfirm = findViewById(R.id.btnConfirm);
        etEmail = findViewById(R.id.etEmail);
        databaseHelper = new DatabaseHandler(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                if (!email.isEmpty()) {
                    if (databaseHelper.isEmailExists(email)) { // Ensure email exists in the database
                        String otp = generateOTP();
                        sendEmail(email, otp);
                        Toast.makeText(forgotPassword.this, "Đã gửi OTP qua Email của bạn", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(forgotPassword.this, OTP.class);
                        intent.putExtra("email", email);
                        intent.putExtra("otp", otp);
                        startActivity(intent);
                    } else {
                        Toast.makeText(forgotPassword.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(forgotPassword.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

    private void sendEmail(String email, String OTP) {
        new Thread(() -> {
            try {
                // Set up mail server properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // Authenticate with email server
                javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication("storetenpm@gmail.com", "aqyu dylx cyyb vudh"); // Replace with actual email and password
                    }
                });

                // Create email message
                javax.mail.Message message = new javax.mail.internet.MimeMessage(session);
                message.setFrom(new javax.mail.internet.InternetAddress("storetenpm@gmail.com"));
                message.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(email));
                message.setSubject("Your OTP");
                message.setText("Your new OTP is: " + OTP);

                // Send email
                javax.mail.Transport.send(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
