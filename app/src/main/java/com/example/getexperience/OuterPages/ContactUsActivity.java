package com.example.getexperience.OuterPages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;

import com.example.getexperience.databinding.ActivityContactUsBinding;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ContactUsActivity extends AppCompatActivity {

    private ActivityContactUsBinding binding;
    //private DatabaseReference mData;
    private String messageToSend, emailToSend;
    SweetAlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading = new SweetAlertDialog(ContactUsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitle("Sending Email...");
        //loading.setContentText("Please wait...");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        binding.contactSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageToSend = binding.contactMessageData.getText().toString();
                emailToSend = binding.contactEmailData.getText().toString();
                if (emailToSend.isEmpty()) {
                    binding.contactEmailData.setError("Please enter your email");
                    binding.contactEmailData.setFocusable(true);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailToSend).matches()) {
                    binding.contactEmailData.setError("Invalid email, try again");
                    binding.contactEmailData.setFocusable(true);
                } else if (messageToSend.isEmpty()) {
                    binding.contactMessageData.setError("Please enter your message");
                    binding.contactMessageData.setFocusable(true);
                } else

                {
                    loading.show();
                    final String username = "getexperiencee@gmail.com";
                    final String password = "1234554321Mm";
                    Properties properties = new Properties();
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.starttls.enable", "true");
                    properties.put("mail.smtp.host", "smtp.gmail.com");
                    properties.put("mail.smtp.port", "587");
                    Session session = Session.getDefaultInstance(properties, new Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password); }});
                    try {
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(emailToSend));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(username));
                        message.setSubject("Get Experience App");
                        message.setText("This email: " + emailToSend + "\n \nSend this message from your app:\n \n" + messageToSend);
                        Transport.send(message);
                        loading.dismiss();
                        SweetAlertDialog dialog = new SweetAlertDialog(ContactUsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitle("Email has been send successfully");
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setConfirmClickListener(sDialog -> {
                            dialog.dismissWithAnimation();
                        });
                        dialog.show();
                    } catch (MessagingException e) {
                        System.out.println(e);
                        throw new RuntimeException(e);
                    } } }});
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
}

