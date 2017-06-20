package org.luizlopes.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/createAccount", method = RequestMethod.GET)
    public String newAccount() {
        return "create_account";
    }

    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public String saveAccount() {
        // TODO implementar criacao de conta
        return "login";
    }
}
