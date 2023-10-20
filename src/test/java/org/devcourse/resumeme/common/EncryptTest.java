package org.devcourse.resumeme.common;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class EncryptTest {

    @Test
    @Disabled
    void 프로퍼티_파일_값을_암호화_하는데_사용한다() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        encryptor.setPassword("");
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setStringOutputType("base64");

        String url = "";
        String username = "";
        String password = "";
        String urlResult = encryptor.encrypt(url);
        String usernameResult = encryptor.encrypt(username);
        String passwordReesult = encryptor.encrypt(password);

        System.out.println("url plain : " + encryptor.decrypt(urlResult));
        System.out.println("url encoding : " + urlResult);

        System.out.println("username plain : " + encryptor.decrypt(usernameResult));
        System.out.println("username encoding : " + usernameResult);

        System.out.println("password plain : " + encryptor.decrypt(passwordReesult));
        System.out.println("password encoding : " + passwordReesult);
    }

}
