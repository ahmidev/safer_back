package com.projet3.safertogether.services;

public interface EmailService {



    void sendResetPasswordEmail(String email, String resetLink);
}
