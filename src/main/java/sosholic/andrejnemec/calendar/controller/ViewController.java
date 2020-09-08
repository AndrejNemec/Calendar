package sosholic.andrejnemec.calendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping({"/", "/login", "/register"})
    public String index() {
        return "forward:/index.html";
    }

}
