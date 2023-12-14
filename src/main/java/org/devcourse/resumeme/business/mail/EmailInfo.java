package org.devcourse.resumeme.business.mail;

import java.util.Map;

public record EmailInfo(String[] to, EmailType type, Map<String, Object> attributes) {

}
