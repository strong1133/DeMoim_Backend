package com.demoim_backend.demoim_backend.util;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class SignupValidator {

    //Username(Email)
    public static boolean usernameValid(String username) {
        return Pattern.matches("^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", username);
    }

    //PW
    public static boolean pwValid(String pw) {
        return Pattern.matches("^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{4,20}$", pw);
    }


}
