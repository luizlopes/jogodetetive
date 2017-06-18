package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.domain.jogador.Posicao;
import org.luizlopes.domain.mapper.PosicaoMapper;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

import static org.luizlopes.websocket.model.CommandType.MOVER_JOGADOR;

public class MoverJogador implements JogadorState {

    private Contexto contexto;
    private Integer casas;

    public MoverJogador(Contexto contexto, Integer casas) {
        this.contexto = contexto;
        this.casas = casas;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(MOVER_JOGADOR);
        command.setOptions(casas);
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        Posicao posicao = PosicaoMapper.parse(response);
        contexto.getAtual().setPosicao(posicao);
        if (posicao.isComodo()) {
            return new FazerPalpite(contexto);
        }
        return new EsperarVez(contexto.getAtual());
    }

    @Override
    public Info sendInfo() {
        return null;
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.MOVENDO;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
