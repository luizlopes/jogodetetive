package org.luizlopes.domain.jogador;

import lombok.Getter;

@Getter
public class Posicao {

    private int[] posicao;
    private String comodo;

    public Posicao(int x, int y, String comodo) {
        posicao = new int[]{x, y};
        this.comodo = comodo;
    }

    public Posicao(int x, int y) {
        this(x, y, null);
    }

    public boolean isComodo() {
        return comodo != null && !comodo.isEmpty();
    }
}
