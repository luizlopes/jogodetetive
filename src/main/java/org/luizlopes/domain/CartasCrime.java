package org.luizlopes.domain;

import java.util.List;
import java.util.Random;

public class CartasCrime {

    private final List<Carta> personagens;
    private final List<Carta> armas;
    private final List<Carta> locais;
    private final Carta personagemCrime;
    private final Carta armaCrime;
    private final Carta localCrime;

    public CartasCrime(List<Carta> personagens, List<Carta> armas, List<Carta> locais) {
        this.personagens = personagens;
        this.armas = armas;
        this.locais = locais;

        personagemCrime = sorteiaCartas(personagens);
        armaCrime = sorteiaCartas(armas);
        localCrime = sorteiaCartas(locais);

        personagens.remove(personagemCrime);
        armas.remove(armaCrime);
        locais.remove(localCrime);
    }

    private Carta sorteiaCartas(List<Carta> cartas) {
        Random random = new Random();
        int personagemIndex = random.nextInt(cartas.size());
        return cartas.get(personagemIndex);
    }

    public Sorteador createSorteadorSuspeitos(int quantidade) {
        return new Sorteador(quantidade, personagens);
    }

    public Sorteador createSorteadorArmas(int quantidade) {
        return new Sorteador(quantidade, armas);
    }

    public Sorteador createSorteadorLocais(int quantidade) {
        return new Sorteador(quantidade, locais);
    }

    public Boolean isAcusacaoCorreta(Palpite palpite) {
        return personagemCrime.equals(palpite.getSuspeito()) &&
                armaCrime.equals(palpite.getArma()) &&
                localCrime.equals(palpite.getLocal());
    }
}
