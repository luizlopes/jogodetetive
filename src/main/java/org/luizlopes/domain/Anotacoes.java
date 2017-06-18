package org.luizlopes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Anotacoes implements Serializable {

    private List<Anotacao> suspeitos;
    private List<Anotacao> armas;
    private List<Anotacao> locais;
    @JsonIgnore private List<Carta> cartasSuspeitos;
    @JsonIgnore private List<Carta> cartasArmas;
    @JsonIgnore private List<Carta> cartasLocais;

    public Anotacoes(List<Anotacao> suspeitos, List<Anotacao> armas, List<Anotacao> locais) {
        this.suspeitos = suspeitos;
        this.armas = armas;
        this.locais = locais;
    }

    public void cartasSuspeitos(List<Carta> cartasSuspeitos) {
        this.cartasSuspeitos = cartasSuspeitos;
        for (Carta cartaSuspeito : cartasSuspeitos) {
            for (Anotacao anotacao : suspeitos) {
                if (anotacao.getCarta().equals(cartaSuspeito)) {
                    anotacao.setMinhaCarta(true);
                }
            }
        }
    }

    public void cartasArmas(List<Carta> cartasArmas) {
        this.cartasArmas = cartasArmas;
        for (Carta cartaArma : cartasArmas) {
            for (Anotacao anotacao : armas) {
                if (anotacao.getCarta().equals(cartaArma)) {
                    anotacao.setMinhaCarta(true);
                }
            }
        }
    }

    public void cartasLocais(List<Carta> cartasLocais) {
        this.cartasLocais = cartasLocais;
        for (Carta cartaLocal : cartasLocais) {
            for (Anotacao anotacao : locais) {
                if (anotacao.getCarta().equals(cartaLocal)) {
                    anotacao.setMinhaCarta(true);
                }
            }
        }
    }

    public int possuiCartas(Palpite palpite) {
        int quantidade = 0;
        if (getCartasSuspeitos().contains(palpite.getSuspeito())) {
            quantidade++;
        }
        if (getCartasArmas().contains(palpite.getArma())) {
            quantidade++;
        }
        if (getCartasLocais().contains(palpite.getLocal())) {
            quantidade++;
        }
        return quantidade;
    }
}
