package com.lfhardware.shared;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    public String hi(){
        return "Hi";
    }
}
