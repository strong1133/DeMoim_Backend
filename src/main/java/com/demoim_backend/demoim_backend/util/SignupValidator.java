package com.demoim_backend.demoim_backend.util;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class SignupValidator {

    //Username(Email)
    public static boolean usernameValid(String username) {
        return Pattern.matches("^([\\w\\.\\_\\-])*[a-zA-Z0-9]+([\\w\\.\\_\\-])*([a-zA-Z0-9])+([\\w\\.\\_\\-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,8}$", username);
    }

    //PW
    public static boolean pwValid(String pw) {
        return Pattern.matches("^[A-Za-z0-9]{4,20}$", pw);
    }


}
