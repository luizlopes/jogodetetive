package org.luizlopes.domain;

import lombok.Getter;
import lombok.Setter;
import org.luizlopes.domain.jogador.Jogador;

@Getter
public class Palpite {

    @Setter
    private Jogador jogador;
    private final Carta suspeito;
    private final Carta arma;
    private final Carta local;

    public Palpite(Carta suspeito, Carta arma, Carta local) {
        this.suspeito = suspeito;
        this.arma = arma;
        this.local = local;
    }
}
