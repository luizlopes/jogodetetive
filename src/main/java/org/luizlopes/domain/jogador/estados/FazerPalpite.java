package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.Palpite;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.domain.mapper.PalpiteMapper;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.CommandType;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

import java.util.Map;

public class FazerPalpite implements JogadorState {

    private Jogador jogador;

    public FazerPalpite(Jogador jogador) {
        this.jogador = jogador;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(CommandType.FAZER_PALPITE);
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        Palpite palpite = PalpiteMapper.parse((Map) response.getResponse());
        palpite.setJogador(jogador);
        return new VerCartas(jogador, palpite);
    }

    @Override
    public Info sendInfo() {
        return new Info(jogador, InfoType.ULTIMO_JOGADOR);
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.FAZENDO_PALPITE;
    }
}
