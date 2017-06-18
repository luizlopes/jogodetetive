package org.luizlopes.domain.jogador;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.luizlopes.domain.Anotacoes;
import org.luizlopes.domain.Carta;
import org.luizlopes.domain.Palpite;
import org.luizlopes.domain.jogador.estados.*;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.Info;

import java.util.Observable;

@EqualsAndHashCode(of = {"usuario", "personagem"}, callSuper = false)
public class Jogador extends Observable {

    @Getter
    private String usuario;
    @Getter
    private Carta personagem;
    @Getter @Setter @JsonIgnore
    private Anotacoes anotacoes;
    @Getter @Setter
    private Posicao posicao;
    @Getter
    private Boolean mestre;

    JogadorState state;

    public Jogador(String usuario, Carta personagem, Posicao posicaoInicial, Anotacoes anotacoes) {
        this(usuario, personagem, posicaoInicial, anotacoes, false);
    }

    public Jogador(String usuario, Carta personagem, Posicao posicaoInicial, Anotacoes anotacoes, Boolean mestre) {
        this.usuario = usuario;
        this.personagem = personagem;
        this.posicao = posicaoInicial;
        this.anotacoes = anotacoes;
        this.mestre = mestre;

        changeState(new AguardarInicio(this));
    }

    public int getId() {
        return hashCode();
    }

    public void esperarVez() {
        state = new EsperarVez(this);
    }

    public Command sendCommand() {
        return state.sendCommand();
    }

    public void receiveCommand(Command response) {
        changeState(state.receiveReponse(response));
    }

    public Info sendInfo() {
        return state.sendInfo();
    }

    public void lancarDados(Contexto contexto) {
        changeState(new LancarDados(contexto));
    }

    public void solicitarExibirCartas(Contexto contexto) {
        changeState(new SolicitarExibirCarta(contexto));
    }

    public void exibirCarta(Contexto contexto, Carta cartaASerExibida) {
        changeState(new VerCarta(contexto, cartaASerExibida));
    }

    public JogadorStatus getStatus() {
        return state.status();
    }

    void changeState(JogadorState state) {
        if (this.state != state) {
            this.state = state;
            setChanged();
            notifyObservers(this);
        }
    }

    public int possuiCartas(Palpite palpite) {
        return anotacoes.possuiCartas(palpite);
    }

    public Contexto contexto() {
        return state.getContexto();
    }
}
