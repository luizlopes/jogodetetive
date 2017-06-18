package org.luizlopes.domain;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class Anotacao implements Serializable {

    private Carta carta;
    private Boolean selecionada;
    private Boolean minhaCarta;

    public Anotacao(Carta carta, boolean selecionada) {
        this.carta = carta;
        this.selecionada = selecionada;
        this.minhaCarta = false;
    }

    public Anotacao(Carta carta) {
        this(carta, false);
    }

}
