package org.luizlopes.domain;

import lombok.Getter;
import lombok.Setter;
import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.service.CartaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

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
                atual = jogadores.proximoJogando(atual);
                atual.escolherJogada(criaContexto());
                jogador = atual;
            }

            if (atual != null && atual.contexto() != null && atual.contexto().isProntoParaSolicitar()) {
                atual.contexto().solicitarExibirCarta();
            }

            if (atual != null && atual.contexto() != null && atual.contexto().isFezAcusacao()) {
                Palpite palpite = atual.contexto().getPalpite();
                if (cartasCrime.isAcusacaoCorreta(palpite)) {
                    atual.jogadorAcertouAcusacao(atual.contexto());
                    encerrarPartida();
                } else {
                    atual.contexto().setCartasCrime(cartasCrime);
                    atual.jogadorErrouAcusacao(atual.contexto());
                    jogador = atual;
                }
            }

            if (jogadores.existeJogadorAtivo()) {
                setChanged();
                notifyObservers(jogador);
            }
        }
    }

    private Contexto criaContexto() {
        return new Contexto(jogadores, atual);
    }

    private void iniciarPartida() {
        final SorteadorDeCartas sorteadorDeCartas = new SorteadorDeCartas(
                cartaService.getAllPersonagens(),
                cartaService.getAllArmas(),
                cartaService.getAllLocais(),
                jogadores.quantidade());

        cartasCrime = sorteadorDeCartas.getCartasCrime();

        int index = 0;
        for (Jogador jogador : jogadores.jogadores()) {
            jogador.getAnotacoes().cartasSuspeitos(sorteadorDeCartas.getMonteSuspeitos(index));
            jogador.getAnotacoes().cartasArmas(sorteadorDeCartas.getMonteArmas(index));
            jogador.getAnotacoes().cartasLocais(sorteadorDeCartas.getMonteLocais(index));
            index++;
        }

        jogadores.iniciarPartida();

        for (Jogador jogador : jogadores.jogadores()) {
            setChanged();
            notifyObservers(jogador);
        }

        status = JogoStatus.PARTIDA_INICIADA;
    }

    private void encerrarPartida() {
        status = JogoStatus.PARTIDA_ENCERRADA;
    }

    public void cancelarPartida() {
        status = JogoStatus.PARTIDA_CANCELADA;
    }

    public void reiniciarPartida() {
        status = JogoStatus.PARTIDA_NAO_INICIADA;
        atual = null;
        jogadores.reiniciar();
    }
}
