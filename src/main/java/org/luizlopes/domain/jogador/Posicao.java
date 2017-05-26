package org.luizlopes.domain.jogador;

import lombok.Getter;

@Getter
public class Posicao {

    private int[] posicao;
    private boolean ehComodo;

    public Posicao(int x, int y, boolean ehComodo) {
        posicao = new int[]{x, y};
        this.ehComodo = ehComodo;
    }

    public Posicao(int x, int y) {
        this(x, y, false);
    }

}
