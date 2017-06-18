package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

public class EsperarApurarAcusacao implements JogadorState {

    private Contexto contexto;

    public EsperarApurarAcusacao(Contexto contexto) {
        this.contexto = contexto;
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
        return JogadorStatus.ESPERANDO_APURAR_ACUSACAO;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
