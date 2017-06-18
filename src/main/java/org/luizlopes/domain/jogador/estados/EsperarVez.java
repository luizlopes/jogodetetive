package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

import static org.luizlopes.websocket.model.CommandType.ESPERAR_VEZ;

public class EsperarVez implements JogadorState {

    private Jogador jogador;

    public EsperarVez(final Jogador jogador) {
        this.jogador = jogador;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(ESPERAR_VEZ);
        Object[] array = {jogador, jogador.getAnotacoes()};
        command.setOptions(array);
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

    @Override
    public Contexto getContexto() {
        return null;
    }
}
