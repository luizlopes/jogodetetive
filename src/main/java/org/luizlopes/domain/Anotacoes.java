package org.luizlopes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Anotacoes implements Serializable {

    private List<Anotacao> suspeitos;
    private List<Anotacao> armas;
    private List<Anotacao> locais;

    public void cartasSuspeitos(List<Carta> cartasSuspeitos) {
        for (Carta cartaSuspeito : cartasSuspeitos) {
            for (Anotacao anotacao : suspeitos) {
                if (anotacao.getCarta().equals(cartaSuspeito)) {
                    anotacao.setMinhaCarta(true);
                }
            }
        }
    }

    public void cartasArmas(List<Carta> cartasArmas) {
        for (Carta cartaArma : cartasArmas) {
            for (Anotacao anotacao : armas) {
                if (anotacao.getCarta().equals(cartaArma)) {
                    anotacao.setMinhaCarta(true);
                }
            }
        }
    }

    public void cartasLocais(List<Carta> cartasLocais) {
        for (Carta cartaLocal : cartasLocais) {
            for (Anotacao anotacao : locais) {
                if (anotacao.getCarta().equals(cartaLocal)) {
                    anotacao.setMinhaCarta(true);
                }
            }
        }
    }

}
