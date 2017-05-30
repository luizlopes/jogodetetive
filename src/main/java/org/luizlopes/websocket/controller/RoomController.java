package org.luizlopes.websocket.controller;

import org.luizlopes.domain.Jogadores;
import org.luizlopes.domain.Jogo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RoomController {

    @Autowired
    private Jogo jogo;

    @Autowired
    private Jogadores jogadores;

    @RequestMapping(value = "/rooms", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("jogoStatus", jogo.getStatus());
        model.addAttribute("jogadores", jogadores.jogadores().size());
        return "rooms";
    }

    @RequestMapping(value="/reset", method=RequestMethod.POST)
    public String reiniciarPartida() {
        jogo.reiniciarPartida();
        return "redirect:/rooms";
    }

}
