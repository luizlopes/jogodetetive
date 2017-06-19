package org.luizlopes.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SorteadorDeCartas {

    private final List<Carta> personagens;
    private final List<Carta> armas;
    private final List<Carta> locais;
    private int quantidade;
    private CartasCrime cartasCrime;
    private List<List<Carta>> montesSupeitos = new ArrayList<>();
    private List<List<Carta>> montesArmas = new ArrayList<>();;
    private List<List<Carta>> montesLocais = new ArrayList<>();;

    public SorteadorDeCartas(final List<Carta> personagens, final List<Carta> armas, final List<Carta> locais, int quantidadeJogadores) {
        this.personagens = personagens;
        this.armas = armas;
        this.locais = locais;
        this.quantidade = quantidadeJogadores;
        sorteiaCartasCrime();
        sorteiaCartasSuspeitos();
        sorteiaCartasArmas();
        sorteiaCartasLocais();
    }

    public CartasCrime getCartasCrime() {
        return cartasCrime;
    }

    public List<Carta> getMonteSuspeitos(int index) {
        return montesSupeitos.get(index);
    }

    public List<Carta> getMonteArmas(int index) {
        return montesArmas.get(index);
    }

    public List<Carta> getMonteLocais(int index) {
        return montesLocais.get(index);
    }

    private void sorteiaCartasSuspeitos() {
        for (int i = quantidade; i > 0; i--) {
            montesSupeitos.add(distribuirCartas(personagens, i));
        }
    }

    private void sorteiaCartasArmas() {
        for (int i = quantidade; i > 0; i--) {
            montesArmas.add(distribuirCartas(armas, i));
        }
    }

    private void sorteiaCartasLocais() {
        for (int i = quantidade; i > 0; i--) {
            montesLocais.add(distribuirCartas(locais, i));
        }
    }

    private List<Carta> distribuirCartas(List<Carta> cartas, int jogadoresRestantes) {
        List<Carta> selecionados = new ArrayList<>();
        int quantidadeCartas = cartas.size() / jogadoresRestantes;
        for(int i = 1; i <= quantidadeCartas; i++) {
            Carta sorteio = sorteiaCartas(cartas);
            selecionados.add(sorteio);
            cartas.remove(sorteio);
        }
        return selecionados;
    }

    private void sorteiaCartasCrime() {
        Carta personagemCrime = sorteiaCartas(personagens);
        Carta armaCrime = sorteiaCartas(armas);
        Carta localCrime = sorteiaCartas(locais);
        personagens.remove(personagemCrime);
        armas.remove(armaCrime);
        locais.remove(localCrime);
        this.cartasCrime = new CartasCrime(personagemCrime, armaCrime, localCrime);
    }

    private Carta sorteiaCartas(List<Carta> cartas) {
        Random random = new Random();
        int personagemIndex = random.nextInt(cartas.size());
        return cartas.get(personagemIndex);
    }
}
