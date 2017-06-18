package org.luizlopes.domain.mapper;

import org.luizlopes.domain.Carta;
import org.luizlopes.domain.Palpite;
import org.luizlopes.domain.mapper.CartaMapper;

import java.util.Map;

public class PalpiteMapper {

    public static Palpite parse(Map map) {
        return PalpiteMapper.parse(map, "palpite");
    }

    public static Palpite parse(Map map, String root) {
        Map palpite = (Map) map.get(root);

        Map cartaSuspeitoMap = (Map) ((Map) palpite.get("suspeito")).get("carta");
        Carta suspeito = CartaMapper.parse(cartaSuspeitoMap);

        Map cartaArmaMap = (Map) ((Map) palpite.get("arma")).get("carta");
        Carta arma = CartaMapper.parse(cartaArmaMap);

        Map localArmaMap = (Map) ((Map) palpite.get("local")).get("carta");
        Carta local = CartaMapper.parse(localArmaMap);

        return new Palpite(suspeito, arma, local);
    }
}
