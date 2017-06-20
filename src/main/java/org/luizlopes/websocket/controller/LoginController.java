package org.luizlopes.websocket.controller;

import org.luizlopes.domain.UserRequest;
import org.luizlopes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
    @RequestMapping(value = "/createAccount", method = RequestMethod.GET)
    public String newAccount(Model model) {
        UserRequest newUser = new UserRequest();
        model.addAttribute("newUser", newUser);
        return "create_account";
    }

    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public String saveAccount(@ModelAttribute(value = "newUser") UserRequest newUser) {
    	userService.create(newUser);
        return "login";
    }
}
