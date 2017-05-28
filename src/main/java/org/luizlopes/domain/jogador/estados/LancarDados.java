package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

import static org.luizlopes.websocket.model.CommandType.LANCAR_DADOS;

public class LancarDados implements JogadorState {

    private Jogador jogador;

    public LancarDados(Jogador jogador) {
        this.jogador = jogador;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(LANCAR_DADOS);
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        if (response != null) {
            return new MoverJogador(jogador, (Integer) response.getResponse());
        }
        return this;
    }

    @Override
    public Info sendInfo() {
        return new Info(jogador, InfoType.JOGADOR_ATUAL);
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.LANCANDO_DADOS;
    }
}
