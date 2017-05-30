package org.luizlopes.domain;

import org.luizlopes.domain.jogador.Jogador;
import org.luizlopes.domain.jogador.Posicao;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Jogadores extends Observable implements Observer {

    private Map<String, Jogador> jogadores = new LinkedHashMap<>();

    public void connectUser(String user, Carta personagem, Posicao posicaoInicial, Anotacoes anotacoes) {
        boolean mestre = false;
        if (jogadores.size() == 0) {
            mestre = true;
        }
        Jogador jogador = new Jogador(user, personagem, posicaoInicial, anotacoes, mestre);
        jogador.addObserver(this);
        jogadores.put(user, jogador);
    }

    public void disconnectUser(String user) {
        jogadores.remove(user);
    }

    public List<Jogador> jogadores() {
        return new ArrayList<>(jogadores.values());
    }

    public Set<String> users() {
        return jogadores.keySet();
    }

    public List<Carta> personagensEscolhidos() {
        List<Carta> personagens = new ArrayList<>();
        for (Jogador jogador : jogadores.values()) {
            if (jogador != null) {
                personagens.add(jogador.getPersonagem());
            }
        }
        return personagens;
    }

    public Jogador getByUsuario(String usuario) {
        return jogadores.get(usuario);
    }

    @Override
    public void update(Observable o, Object arg) {

        setChanged();
        notifyObservers(arg);
    }

    public boolean todosEsperando() {
        boolean todosEsperando = true;
        for (Jogador j : jogadores.values()) {
            if (j.getStatus() != JogadorStatus.ESPERANDO) {
                todosEsperando = false;
                break;
            }
        }
        return todosEsperando;
    }

    public Jogador proximo(Jogador atual) {
        if (atual == null) {
            return primeiroJogador();
        } else {
            Iterator<Map.Entry<String, Jogador>> iterator = jogadores.entrySet().iterator();
            Map.Entry<String, Jogador> jogador = iterator.next();
            while (iterator.hasNext()) {
                if (atual.equals(jogador.getValue())) {
                    return iterator.next().getValue();
                }
                jogador = iterator.next();
            }
            return primeiroJogador();
        }
    }

    private Jogador primeiroJogador() {
        Map.Entry<String, Jogador> first = jogadores.entrySet().iterator().next();
        return first.getValue();
    }

    public int quantidade() {
        return jogadores.size();
    }

    public void iniciarPartida() {
        // sortear cartas crime
        for (Jogador jogador : jogadores.values()) {
            // sortear cartas jogadores
            jogador.esperarVez();
        }
    }

    public void reiniciar() {
        for (Map.Entry<String, Jogador> entry : jogadores.entrySet()) {
            entry.setValue(null);
        }
    }
}
