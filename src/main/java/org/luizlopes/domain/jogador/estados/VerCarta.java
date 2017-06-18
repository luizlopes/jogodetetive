package org.luizlopes.domain.jogador.estados;

import lombok.Getter;
import org.luizlopes.domain.Carta;
import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.CommandType;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

@Getter
public class VerCarta implements JogadorState {

    private final Carta cartaASerExibida;
    private final Contexto contexto;

    public VerCarta(Contexto contexto, Carta cartaASerExibida) {
        this.contexto = contexto;
        this.cartaASerExibida = cartaASerExibida;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(CommandType.VER_CARTA);
        command.setOptions(cartaASerExibida);
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        return new EsperarVez(contexto.getAtual());
    }

    @Override
    public Info sendInfo() {
        return new Info(contexto.getPalpite(), InfoType.PALPITE_FEITO);
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.VENDO_CARTA;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
