package org.luizlopes.domain.jogador;

import org.luizlopes.domain.jogador.estados.JogadorState;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

public class FimDeJogo implements JogadorState {
    @Override
    public Command sendCommand() {
        return null;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        return null;
    }

    @Override
    public Info sendInfo() {
        return null;
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.FIM_DE_JOGO;
    }

    @Override
    public Contexto getContexto() {
        return null;
    }
}
