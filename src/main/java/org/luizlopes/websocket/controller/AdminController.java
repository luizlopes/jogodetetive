package org.luizlopes.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.luizlopes.domain.Jogadores;
import org.luizlopes.domain.Jogo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class AdminController {

    @Autowired
    private Jogo jogo;

    @Autowired
    private Jogadores jogadores;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        model.addAttribute("jogoStatus", jogo.getStatus());
        model.addAttribute("jogadores", jogadores.jogadores());
        return "admin";
    }

    @RequestMapping(value="/edit", method=RequestMethod.POST, params="action=cancelar")
    public String cancelarPartida() {
        System.out.println("cancelar partida");
        jogo.cancelarPartida();
        return "admin";
    }

    @RequestMapping(value="/edit", method=RequestMethod.POST, params="action=reiniciar")
    public String reiniciarPartida() {
        System.out.println("reiniciar partida");
        jogo.reiniciarPartida();
        return "admin";
    }
}
