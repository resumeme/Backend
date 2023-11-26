package org.devcourse.resumeme.business.resume.domain;

import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.Arrays;
import java.util.List;

import static java.lang.Double.parseDouble;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ComponentUtils {

    public static List<String> toList(Component component) {
        String[] contents = (component == null ? "" : component.getContent()).split(",");

        return Arrays.asList(contents);
    }

    public static double toDouble(Component component) {
        String content = component.getContent();
        if (content == null) {
            return 0;
        }

        return parseDouble(content);
    }

}
