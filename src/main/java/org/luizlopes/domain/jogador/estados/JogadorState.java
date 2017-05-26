package org.luizlopes.domain.jogador.estados;

import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

public interface JogadorState {

    Command sendCommand();

    JogadorState receiveReponse(Command response);

    Info sendInfo();

    JogadorStatus status();
}
