package org.luizlopes.domain.jogador.estados;

import lombok.Getter;
import org.luizlopes.domain.Carta;
import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.domain.mapper.CartaMapper;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.CommandType;
import org.luizlopes.websocket.model.Info;

import java.util.Map;

@Getter
public class SolicitarExibirCarta implements JogadorState {
    private final Contexto contexto;

    public SolicitarExibirCarta(final Contexto contexto) {
        this.contexto = contexto;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(CommandType.EXIBIR_CARTA);
        command.setOptions(contexto.getPalpite());
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        Carta cartaASerExibida = CartaMapper.parse(((Map)((Map)response.getResponse()).get("carta")));
        contexto.exibirCarta(cartaASerExibida);
        return new EsperarVezSilencio(contexto.getAtual());
    }

    @Override
    public Info sendInfo() {
        // TODO Avisar outros jogadores quem está exibindo as cartas
        return null;
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.EXIBINDO_CARTA;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
