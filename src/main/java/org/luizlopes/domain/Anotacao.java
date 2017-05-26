package org.luizlopes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
public class Anotacao implements Serializable {

    private Carta carta;
    private Boolean selecionada;

    public Anotacao(Carta carta, boolean selecionada) {
        this.carta = carta;
        this.selecionada = selecionada;
    }

    public Anotacao(Carta carta) {
        this(carta, false);
    }

}
