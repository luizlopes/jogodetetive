package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.Palpite;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

public class VerCartas implements JogadorState {

    private final Jogador jogador;
    private final Palpite palpite;

    public VerCartas(Jogador jogador, Palpite palpite) {
        this.jogador = jogador;
        this.palpite = palpite;
    }

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
        return null;
    }
}
