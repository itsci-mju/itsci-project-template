package org.itsci.controller;

import org.apache.log4j.Logger;
import org.itsci.config.WebConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logging = Logger.getLogger(HomeController.class);

    @GetMapping("/")
    public String home() {
        return "home";
    }

}
