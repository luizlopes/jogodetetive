package org.luizlopes.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sorteador {

    private int jogadoresRestantes;
    private final List<Carta> cartas;

    public Sorteador(int quantidade, List<Carta> cartas) {
        this.jogadoresRestantes = quantidade;
        this.cartas = cartas;
    }

    public List<Carta> distribuir() {
        return distribuirCartas(cartas);
    }

    private List<Carta> distribuirCartas(List<Carta> cartas) {
        List<Carta> selecionados = new ArrayList<>();
        int quantidadeCartas = cartas.size() / jogadoresRestantes;
        for(int i = 1; i <= quantidadeCartas; i++) {
            Carta sorteio = sorteio(cartas);
            selecionados.add(sorteio);
            cartas.remove(sorteio);
        }
        jogadoresRestantes--;
        return selecionados;
    }

    private Carta sorteio(List<Carta> cartas) {
        Random random = new Random();
        int personagemIndex = random.nextInt(cartas.size());
        return cartas.get(personagemIndex);
    }

}
