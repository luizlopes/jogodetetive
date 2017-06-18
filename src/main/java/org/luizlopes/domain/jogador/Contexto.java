package org.luizlopes.domain.jogador;

import lombok.Getter;
import lombok.Setter;
import org.luizlopes.domain.Carta;
import org.luizlopes.domain.Jogadores;
import org.luizlopes.domain.Palpite;

import static org.luizlopes.domain.jogador.JogadorStatus.ESPERANDO_EXIBIR_CARTA;

@Getter
public class Contexto {

    private final Jogadores jogadores;
    private final Jogador atual;
    @Setter private Palpite palpite;
    private Jogador proximo;
    private boolean solicitou;

    public Contexto(final Jogadores jogadores, final Jogador atual) {
        this.jogadores = jogadores;
        this.atual = atual;
        this.solicitou = false;
    }

    public void exibirCarta(Carta cartaASerExibida) {
        atual.exibirCarta(this, cartaASerExibida);
    }

    public void solicitarExibirCarta() {
        solicitou = true;
        setJogadorParaExibirCartas();
        proximo.solicitarExibirCartas(this);
    }

    private void setJogadorParaExibirCartas() {
        int possuiCartas = 0;
        Jogador jogador = jogadores.proximo(atual);
        while (jogador != atual) {
            possuiCartas = jogador.possuiCartas(palpite);
            if (possuiCartas > 0) {
                break;
            }
            jogador = jogadores.proximo(jogador);
        }
        // TODO Fazer tratamento para quando ninguem tiver as cartas escolhidas
        proximo = jogador;
    }

    public boolean isProntoParaSolicitar() {
        return atual.getStatus() == ESPERANDO_EXIBIR_CARTA && !solicitou;
    }
}
