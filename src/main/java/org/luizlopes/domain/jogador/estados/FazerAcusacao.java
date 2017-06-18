package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.Palpite;
import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.domain.mapper.PalpiteMapper;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.CommandType;
import org.luizlopes.websocket.model.Info;

import java.util.Map;

public class FazerAcusacao implements JogadorState {

    private Contexto contexto;

    public FazerAcusacao(Contexto contexto) {
        this.contexto = contexto;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(CommandType.FAZER_ACUSACAO);
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        Palpite palpite = PalpiteMapper.parse((Map) response.getResponse(), "acusacao");
        palpite.setJogador(contexto.getAtual());
        contexto.setPalpite(palpite);
        return new EsperarApurarAcusacao(contexto);
    }

    @Override
    public Info sendInfo() {
        // TODO Enviar info sobre jogador que esta fazendo a acusacao
        // TODO Enviar info sobre a acusacao
        return null;
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.FAZENDO_ACUSACAO;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
