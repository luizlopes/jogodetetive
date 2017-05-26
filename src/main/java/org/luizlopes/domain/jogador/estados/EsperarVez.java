package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

import static org.luizlopes.websocket.model.CommandType.ESPERAR_VEZ;

public class EsperarVez implements JogadorState {

    private Jogador jogador;

    public EsperarVez(Jogador jogador) {
        this.jogador = jogador;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(ESPERAR_VEZ);
        command.setOptions(jogador);
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        return this;
    }

    @Override
    public Info sendInfo() {
        return new Info(jogador, InfoType.ULTIMO_JOGADOR);
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.ESPERANDO;
    }

}
