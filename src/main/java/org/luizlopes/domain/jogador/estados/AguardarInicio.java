package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

import static org.luizlopes.websocket.model.CommandType.AGUARDAR_INICIO_JOGO;

public class AguardarInicio implements JogadorState {

    private Jogador jogador;

    public AguardarInicio(Jogador jogador) {
        this.jogador = jogador;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(AGUARDAR_INICIO_JOGO);
        command.setOptions(jogador);
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        return new Pronto();
    }

    @Override
    public Info sendInfo() {
        return new Info(null, InfoType.JOGADORES);
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.INICIANDO;
    }

    @Override
    public Contexto getContexto() {
        return null;
    }
}
