package org.foxminded.rymarovych.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String index() {
        LOGGER.debug("/ GET HTTP request received");

        return "index";
    }

    @GetMapping("/menu")
    public String menu() {
        LOGGER.debug("/menu GET HTTP request received");

        return "menu";
    }
}
