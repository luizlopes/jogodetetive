package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

public class EsperarVezSilencio implements JogadorState {

    private Jogador jogador;

    public EsperarVezSilencio(final Jogador jogador) {
        this.jogador = jogador;
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
        return null;
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
