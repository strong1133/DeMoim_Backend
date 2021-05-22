package com.demoim_backend.demoim_backend.util.coolsms;



import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CoolSmsUtil {

    public void sendCertNumberSms(String phoneNum,String certNumber){

        Message coolsms = new Message(CoolsmsProperties.api_key, CoolsmsProperties.api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to",phoneNum );
        params.put("from",CoolsmsProperties.fromNumber );
        params.put("type", "SMS");
        params.put("text", "[DeMoim]인증번호는"+certNumber+"입니다.");
        params.put("app_version", "test app 1.2");

        try {

            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());

            //에러가 발생하면
            if(obj.containsKey("error_list")){
                throw new IllegalArgumentException("문자메세지가 발송되지못했습니다.");
            }

        } catch (
                CoolsmsException e) {
            throw new IllegalArgumentException("문자메세지가 발송되지못했습니다.");

        }
    }
}
