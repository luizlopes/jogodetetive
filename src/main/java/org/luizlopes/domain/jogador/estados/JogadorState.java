package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

public interface JogadorState {

    Command sendCommand();

    JogadorState receiveReponse(Command response);

    Info sendInfo();

    JogadorStatus status();

    Contexto getContexto();
}
