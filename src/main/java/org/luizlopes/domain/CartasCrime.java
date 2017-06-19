package org.luizlopes.domain;

import lombok.Getter;

@Getter
public class CartasCrime {

    private final Carta personagemCrime;
    private final Carta armaCrime;
    private final Carta localCrime;

    public CartasCrime(Carta personagemCrime, Carta armaCrime, Carta localCrime) {
        this.personagemCrime = personagemCrime;
        this.armaCrime = armaCrime;
        this.localCrime = localCrime;
    }

    public Boolean isAcusacaoCorreta(Palpite palpite) {
        return personagemCrime.equals(palpite.getSuspeito()) &&
                armaCrime.equals(palpite.getArma()) &&
                localCrime.equals(palpite.getLocal());
    }
}
