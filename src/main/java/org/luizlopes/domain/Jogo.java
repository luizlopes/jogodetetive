package org.luizlopes.domain;

import lombok.Getter;
import lombok.Setter;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.service.CartaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import static org.luizlopes.domain.jogador.JogadorStatus.INICIANDO;
import static org.luizlopes.domain.jogador.JogadorStatus.PRONTO;

@Component
public class Jogo extends Observable implements Observer {

    public static final int QUANTIDADE_JOGADORES = 2;

    @Autowired
    private Jogadores jogadores;

    @Autowired
    private CartaService cartaService;

    @Getter @Setter
    private Jogador atual;

    @Getter
    private JogoStatus status;

    @Getter
    private CartasCrime cartasCrime;

    @PostConstruct
    public void init() {
        jogadores.addObserver(this);
        status = JogoStatus.PARTIDA_NAO_INICIADA;
    }

    @Override
    public void update(Observable o, Object arg) {
        Jogador jogador = (Jogador) arg;
        if (status == JogoStatus.PARTIDA_NAO_INICIADA) {
            if (jogador.getStatus() == INICIANDO) {
                setChanged();
                notifyObservers(jogador);
            } else if (jogador.getStatus() == PRONTO) {
                iniciarPartida();
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

    private void iniciarPartida() {
        cartasCrime = new CartasCrime(cartaService.getAllPersonagens(), cartaService.getAllArmas(), cartaService.getAllLocais());

        jogadores.distribuirCartas(
                cartasCrime.createSorteadorSuspeitos(jogadores.quantidade()),
                cartasCrime.createSorteadorArmas(jogadores.quantidade()),
                cartasCrime.createSorteadorLocais(jogadores.quantidade()));

        jogadores.iniciarPartida();
        for (Jogador jogador : jogadores.jogadores()) {
            setChanged();
            notifyObservers(jogador);
        }
        status = JogoStatus.PARTIDA_INICIADA;
    }

    public void cancelarPartida() {
        status = JogoStatus.PARTIDA_CANCELADA;
        // AVISAR JOGADORES QUE PARTIDA FOI CANCELADA
        // NAO RECEBER MAIS MENSAGENS
    }

    public void reiniciarPartida() {
        status = JogoStatus.PARTIDA_NAO_INICIADA;
        atual = null;
        jogadores.reiniciar();
        // AVISAR JOGADORES QUE PARTIDA SERÁ REINICIADA
        // NAO RECEBER MENSAGENS
        // RETORNAR JOGADORES PARA SUAS POSICOES INICIAIS
        // INICIAR PARTIDA
    }
}
