package org.luizlopes.domain;

import org.luizlopes.websocket.model.Command;

import java.util.Map;

public class PalpiteMapper {

    public static Palpite parse(Map map) {
        Map palpite = (Map) map.get("palpite");

        Map cartaSuspeitoMap = (Map) ((Map) palpite.get("suspeito")).get("carta");
        Carta suspeito = CartaMapper.parse(cartaSuspeitoMap);

        Map cartaArmaMap = (Map) ((Map) palpite.get("arma")).get("carta");
        Carta arma = CartaMapper.parse(cartaArmaMap);

        Map localArmaMap = (Map) ((Map) palpite.get("local")).get("carta");
        Carta local = CartaMapper.parse(localArmaMap);

        return new Palpite(suspeito, arma, local);

    }
}
