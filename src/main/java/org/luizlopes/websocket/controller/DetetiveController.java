package org.luizlopes.websocket.controller;

import org.luizlopes.domain.*;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.domain.jogador.Posicao;
import org.luizlopes.domain.mapper.CartaMapper;
import org.luizlopes.service.CartaService;
import org.luizlopes.websocket.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.*;

import static org.luizlopes.websocket.controller.DetetiveConstants.Destination.*;
import static org.luizlopes.websocket.controller.DetetiveConstants.Resource.*;

@Controller
public class DetetiveController implements Observer {

    @Autowired
    private Jogo jogo;

    @Autowired
    private Jogadores jogadores;

    @Autowired
    private CartaService cartaService;

    @Autowired
    private SimpMessagingTemplate template;

    @PostConstruct
    public void init() {
        jogo.addObserver(this);
    }

    @MessageMapping(MESSAGE_BROKER)
    public void message(Message message, Principal principal) throws Exception {
        message.setSender(principal.getName());
        if (message.getRecipient().isEmpty()) {
            template.convertAndSend(CHAT_TOPIC, message);
        } else {
            if (message.senderNotIsRecipient()) {
                template.convertAndSendToUser(message.getRecipient(), CHAT_QUEUE, message);
            }
            template.convertAndSendToUser(message.getSender(), CHAT_QUEUE, message);
        }
    }

    @MessageMapping(COMMAND_BROKER)
    public void command(Principal principal, Command command) throws Exception {
        if (command != null) {
            if (command.getType() == CommandType.ESCOLHER_PERSONAGEM) {
                escolhePersonagem(principal, command);
            } else {
                Jogador jogador = jogadores.getByUsuario(principal.getName());
                jogador.receiveCommand(command);
                sendInfo(jogador);
            }
        }
    }

    private void escolhePersonagem(Principal principal, Command command) {
        Carta personagem = CartaMapper.parse(command);
        Posicao posicaoInicial = cartaService.getPosicaoInicial(personagem);
        jogadores.connectUser(principal.getName(), personagem, posicaoInicial, cartaService.anotacoesIniciais());
        Jogador jogador = jogadores.getByUsuario(principal.getName());
        sendCommand(jogador);
        sendInfo(jogador);
    }

    private void sendInfo(Jogador jogador) {
        if (jogador.sendInfo() != null) {
            Info info = jogador.sendInfo();
            if (info.getType() == InfoType.JOGADORES) {
                info.setBody(jogadores.jogadores());
            }
            sendGameInfo(info);
        }
    }

    private void sendCommand(Jogador jogador) {
        Command command = jogador.sendCommand();
        if (command != null)
            template.convertAndSendToUser(jogador.getUsuario(), COMMAND_QUEUE, command);
    }

    @MessageMapping(INFO_BROKER)
    public void info(Principal principal, Info infoRequest) {
        if (infoRequest.getType() == InfoType.PERSONAGENS_DISPONIVEIS) {
            List<Carta> personagensDisponiveis = cartaService.personagensDisponiveis(jogadores.personagensEscolhidos());
            Info infoResponse = new Info(personagensDisponiveis, infoRequest.getType());
            template.convertAndSendToUser(principal.getName(), INFO_QUEUE, infoResponse);
        }
    }

    private void sendGameInfo(Info info) {
        template.convertAndSend(INFO_TOPIC, info);
    }

    @Override
    public void update(Observable o, Object arg) {
        Jogador jogador = (Jogador) arg;
        sendCommand(jogador);
        sendInfo(jogador);
    }
}
