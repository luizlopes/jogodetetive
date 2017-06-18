package org.luizlopes.domain.jogador.estados;

import lombok.Getter;
import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

@Getter
public class EsperarExibirCarta implements JogadorState {

    private Contexto contexto;

    public EsperarExibirCarta(Contexto contexto) {
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
        return JogadorStatus.ESPERANDO_EXIBIR_CARTA;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
