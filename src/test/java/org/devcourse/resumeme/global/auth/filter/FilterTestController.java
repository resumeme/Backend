package org.devcourse.resumeme.global.auth.filter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterTestController {

    @GetMapping("/filter-test")
    public String test() {
        return "success";
    }

}
