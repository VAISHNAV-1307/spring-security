package com.example.SpringSecurityEx7.controllers;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    //@PostAuthorize

    @GetMapping("/demo")
    @PreAuthorize("hasAuthority('read')")
    public String demo() {
        return "Demo";
    }

    @GetMapping("/demo3/{something}")
    @PreAuthorize("""
            (#something == authentication.name) or
            hasAnyAuthority('write', 'read')
            """)
    // this is the authentication object directly from the security context which has been get stored at the time of filter level authentication
    public String demo3(@PathVariable("something") String something) {
        var a = SecurityContextHolder.getContext().getAuthentication();
        return "Demo3";
    }

    @GetMapping("/demo4/{something}")
    @PreAuthorize("@demo4ConditionEvaluator.condition()")
    public String demo4() {
        return "Demo4";
    }

    // @PostAuthorize

    @GetMapping("/demo5")
    @PostAuthorize("returnObject != 'Demo5'")
    public String demo5() {
        return "Demo5";
    }

    // @PreFilter -> Works with either array or collection

    @GetMapping("/demo6")
    @PreFilter("filterObject.contains('a')")
    // It will allow only the string has a, it will filter out the strings which contains a
    public String demo6(@RequestBody List<String> values) {
        System.out.println("Values : " + values);
        return "Demo6";
    }

    // @PostFilter -> works only when return type is array or collection

    @GetMapping("/demo7")
    @PostFilter("filterObject.contains('a')") // It will allow only the output string containing a
    public List<String> demo7() {
        return List.of("abc", "def");
    }


}
