package client;

import client.MailClient;

public class MailSenderFactory {

    public static MailClient get() {
        return new SpringMailClient();
    }

}
