package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

public class JogadorForaDaPartida implements JogadorState {
    private Contexto contexto;

    public JogadorForaDaPartida(Contexto contexto) {
        this.contexto = contexto;
    }

    @Override
    public Command sendCommand() {
        return null;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        return this;
    }

    @Override
    public Info sendInfo() {
        // Avisar todos que jogador está fora
        return null;
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.FORA_DA_PARTIDA;
    }

    @Override
    public Contexto getContexto() {
        return null;
    }
}
