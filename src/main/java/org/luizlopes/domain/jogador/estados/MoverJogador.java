package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.PosicaoMapper;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.domain.jogador.Posicao;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

import static org.luizlopes.websocket.model.CommandType.MOVER_JOGADOR;

public class MoverJogador implements JogadorState {

    private Jogador jogador;
    private Integer casas;

    public MoverJogador(Jogador jogador, Integer casas) {
        this.jogador = jogador;
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
        jogador.setPosicao(posicao);
        if (posicao.isComodo()) {
            return new FazerPalpite(jogador);
        }
        return new EsperarVez(jogador);
    }

    @Override
    public Info sendInfo() {
        return null;
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.MOVENDO;
    }
}
