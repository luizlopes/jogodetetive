package org.luizlopes.domain.jogador.estados;

import org.luizlopes.domain.jogador.Contexto;
import org.luizlopes.domain.jogador.JogadorStatus;
import org.luizlopes.websocket.model.Command;
import org.luizlopes.websocket.model.CommandType;
import org.luizlopes.websocket.model.Info;
import org.luizlopes.websocket.model.InfoType;

public class JogadorErrouAcusacao implements JogadorState {
    private Contexto contexto;

    public JogadorErrouAcusacao(Contexto contexto) {
        this.contexto = contexto;
    }

    @Override
    public Command sendCommand() {
        Command command = new Command();
        command.setType(CommandType.FORA_DA_PARTIDA);
        Object[] objects = {
                contexto.getPalpite(),
                contexto.getCartasCrime().getPersonagemCrime(),
                contexto.getCartasCrime().getArmaCrime(),
                contexto.getCartasCrime().getLocalCrime()
        };
        command.setOptions(objects);
        return command;
    }

    @Override
    public JogadorState receiveReponse(Command response) {
        return new JogadorForaDaPartida(contexto);
    }

    @Override
    public Info sendInfo() {
        Object[] objects = {contexto.getPalpite(),
                contexto.getAtual().getAnotacoes().getCartasSuspeitos(),
                contexto.getAtual().getAnotacoes().getCartasArmas(),
                contexto.getAtual().getAnotacoes().getCartasLocais()};
        return new Info(objects, InfoType.JOGADOR_FORA_DA_PARTIDA);
    }

    @Override
    public JogadorStatus status() {
        return JogadorStatus.ERROU_ACUSACAO;
    }

    @Override
    public Contexto getContexto() {
        return contexto;
    }
}
