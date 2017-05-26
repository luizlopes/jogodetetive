package org.luizlopes.domain;

import lombok.Getter;
import lombok.Setter;
import org.luizlopes.domain.jogador.Jogador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

import static org.luizlopes.domain.jogador.estados.JogadorStatus.INICIANDO;

@Component
public class Jogo extends Observable implements Observer {

    public static final int QUANTIDADE_JOGADORES = 2;

    @Autowired
    private Jogadores jogadores;

    @Getter @Setter
    private Jogador atual;

    private JogoStatus status;

    @PostConstruct
    public void init() {
        jogadores.addObserver(this);
        status = JogoStatus.PARTIDA_NAO_INICIADA;
    }

    @Override
    public void update(Observable o, Object arg) {
        Jogador jogador = (Jogador) arg;
        if (status == JogoStatus.PARTIDA_NAO_INICIADA) {
            if (jogador.status() == INICIANDO) {
                setChanged();
                notifyObservers(jogador);
            } else {
                if (jogadores.quantidade() >= QUANTIDADE_JOGADORES) {
                    jogadores.iniciarPartida();
                    status = JogoStatus.PARTIDA_INICIADA;
                }
            }
        }

        if (status == JogoStatus.PARTIDA_INICIADA) {
            if (jogadores.todosEsperando()) {
                atual = jogadores.proximo(atual);
                atual.lancarDados();
            }
            setChanged();
            notifyObservers(jogador);
        }
    }
}
