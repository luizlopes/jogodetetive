package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.Palpite;
import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.domain.mapper.PalpiteMapper;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.CommandType;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

import java.util.Map;

public class FazerPalpite implements JogadorState {

    private Contexto contexto;

    public FazerPalpite(Contexto contexto) {
        this.contexto = contexto;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(CommandType.FAZER_PALPITE);
        command.setOptions(contexto.getAtual().getPosicao());
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        Palpite palpite = PalpiteMapper.parse((Map) response.getResponse());
        palpite.setJogador(contexto.getAtual());
        contexto.setPalpite(palpite);
        return new EsperarExibirCarta(contexto);
    }

    @Override
    public Info sendInfo() {
        return new Info(contexto.getAtual(), InfoType.ULTIMO_JOGADOR);
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.FAZENDO_PALPITE;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
