package louie.hanse.shareplate.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChatSocketTestController {

    @RequestMapping("/test")
    public String test() {
        return "socket-test";
    }
}
