package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

public class JogadorGanhouPartida implements JogadorState {
    private Contexto contexto;

    public JogadorGanhouPartida(Contexto contexto) {
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
        return new Info(contexto.getPalpite(), InfoType.GANHOU_PARTIDA);
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.GANHOU_PARTIDA;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
