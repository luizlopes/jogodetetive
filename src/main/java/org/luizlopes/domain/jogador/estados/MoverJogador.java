package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.domain.jogador.Posicao;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> map = (LinkedHashMap) response.getResponse();
        List<Integer> posicao = (List<Integer>) map.get("posicao");
        Boolean ehComodo = (Boolean) map.get("ehComodo");
        jogador.setPosicao(new Posicao(posicao.get(0), posicao.get(1), ehComodo));
        if (ehComodo) {
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
