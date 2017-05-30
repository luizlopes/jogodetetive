package org.luizlopes.domain.jogador;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.luizlopes.domain.Anotacoes;
import org.luizlopes.domain.Carta;
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
    @Getter @Setter
    private Anotacoes anotacoes;
    @Getter @Setter
    private Posicao posicao;
    @Getter
    private Boolean mestre;

    JogadorState state;
    final JogadorState aguardarInicioJogo;
    final JogadorState esperarVez;
    final LancarDados lancarDados;

    public Jogador(String usuario, Carta personagem, Posicao posicaoInicial, Anotacoes anotacoes) {
        this(usuario, personagem, posicaoInicial, anotacoes, false);
    }

    public Jogador(String usuario, Carta personagem, Posicao posicaoInicial, Anotacoes anotacoes, Boolean mestre) {
        this.usuario = usuario;
        this.personagem = personagem;
        this.posicao = posicaoInicial;
        this.anotacoes = anotacoes;
        this.mestre = mestre;

        // INICIALIZA STATES
        esperarVez = new EsperarVez(this);
        aguardarInicioJogo = new AguardarInicio(this);
        lancarDados = new LancarDados(this);
        //

        changeState(aguardarInicioJogo);
    }

    public int getId() {
        return hashCode();
    }

    public void esperarVez() {
        state = esperarVez;
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

    public void lancarDados() {
        changeState(lancarDados);
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
}
