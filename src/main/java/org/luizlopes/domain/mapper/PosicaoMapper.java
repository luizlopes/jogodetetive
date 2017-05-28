package org.luizlopes.domain.mapper;

import org.luizlopes.domain.jogador.Posicao;
import org.luizlopes.websocket.model.Command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PosicaoMapper {

    public static Posicao parse(Command response) {
        Map<String, Object> map = (LinkedHashMap) response.getResponse();
        List<Integer> posicao = (List<Integer>) map.get("posicao");
        String comodo = (String) map.get("comodo");
        return new Posicao(posicao.get(0), posicao.get(1), comodo);
    }

}
