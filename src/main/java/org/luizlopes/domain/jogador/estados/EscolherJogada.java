package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.CommandType;
import org.luizlopes.websocket.model.Info;

public class EscolherJogada implements JogadorState {

    private Contexto contexto;

    public EscolherJogada(Contexto contexto) {
        this.contexto = contexto;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(CommandType.ESCOLHER_JOGADA);
        command.setOptions(new String[]{"Lançar dados", "Fazer acusação"});
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        JogadorState jogadorState = null;
        if (response.getResponse().equals("Fazer acusação")) { // Fazer acusação
            jogadorState = new FazerAcusacao(contexto);
        } else { // Lançar dados
            jogadorState = new LancarDados(contexto);
        }
        return jogadorState;
    }

    @Override
    public Info sendInfo() {
        return null;
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.ESCOLHENDO_JOGADA;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
