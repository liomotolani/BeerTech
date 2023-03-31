package com.beerkhaton.mealtrackerapi.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendNewUserEmail(String email, String username, String password, String code)
            throws MessagingException, MessagingException, UnsupportedEncodingException;
}
