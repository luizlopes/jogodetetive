package org.luizlopes.service;

import org.luizlopes.domain.Anotacao;
import org.luizlopes.domain.Anotacoes;
import org.luizlopes.domain.Carta;
import org.luizlopes.domain.jogador.Posicao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartaService {

    // FIXME hard code
    public List<Carta> getAllLocais() {
        ArrayList<Carta> locais = new ArrayList<>();
        locais.add(new Carta("Restaurante", "/Imagens/padrao/locais/restaurante.png"));
        locais.add(new Carta("Prefeitura", "/Imagens/padrao/locais/prefeitura.png"));
        locais.add(new Carta("Banco", "/Imagens/padrao/locais/banco.png"));
        locais.add(new Carta("Hospital", "/Imagens/padrao/locais/hospital.png"));
        locais.add(new Carta("Praça", "/Imagens/padrao/locais/praca.png"));
        locais.add(new Carta("Mansão", "/Imagens/padrao/locais/mansao.png"));
        locais.add(new Carta("Floricultura", "/Imagens/padrao/locais/floricultura.png"));
        locais.add(new Carta("Hotel", "/Imagens/padrao/locais/hotel.png"));
        locais.add(new Carta("Cemitério", "/Imagens/padrao/locais/cemiterio.png"));
        locais.add(new Carta("Estação de Trem", "/Imagens/padrao/locais/estacao_trem.png"));
        locais.add(new Carta("Boate", "/Imagens/padrao/locais/boate.png"));
        return locais;
    }

    // FIXME hard code
    public List<Carta> getAllArmas() {
        ArrayList<Carta> armas = new ArrayList<>();
        armas.add(new Carta("Arma química", "/Imagens/padrao/armas/arma_quimica.png"));
        armas.add(new Carta("Espingarda", "/Imagens/padrao/armas/espingarda.png"));
        armas.add(new Carta("Faca", "/Imagens/padrao/armas/faca.png"));
        armas.add(new Carta("Pá", "/Imagens/padrao/armas/pa.png"));
        armas.add(new Carta("Pé de Cabra", "/Imagens/padrao/armas/pe_de_cabra.png"));
        armas.add(new Carta("Soco Inglês", "/Imagens/padrao/armas/soco_ingles.png"));
        armas.add(new Carta("Tesoura", "/Imagens/padrao/armas/tesoura.png"));
        armas.add(new Carta("Veneno", "/Imagens/padrao/armas/veneno.png"));
        return armas;
    }

    // FIXME hard code
    public List<Carta> getAllPersonagens() {
        List<Carta> personagens = new ArrayList<>();
        personagens.add(new Carta("Dona Branca", "/Imagens/padrao/personagens/Dona_Branca.png"));
        personagens.add(new Carta("Dona Violeta", "/Imagens/padrao/personagens/Dona_Violeta.png"));
        personagens.add(new Carta("Mordomo James", "/Imagens/padrao/personagens/Mordomo_James.png"));
        personagens.add(new Carta("Sargento Bigode", "/Imagens/padrao/personagens/Sargento_Bigode.png"));
        personagens.add(new Carta("Senhor Marinho", "/Imagens/padrao/personagens/Senhor_Marinho.png"));
        personagens.add(new Carta("Senhorita Rosa", "/Imagens/padrao/personagens/Senhorita_Rosa.png"));
        personagens.add(new Carta("Sergio Soturno", "/Imagens/padrao/personagens/Sergio_Soturno.png"));
        personagens.add(new Carta("Tony Gourmet", "/Imagens/padrao/personagens/Tony_Gourmet.png"));
        return personagens;
    }

    public Carta findPersonagemByName(String name) {
        for (Carta personagem : getAllPersonagens()) {
            if (personagem.getNome().equals(name)) {
                return personagem;
            }
        }
        return null;
    }

    public Posicao getPosicaoInicial(Carta personagem) {
        return getPosicoesIniciais().get(personagem);
    }

    private Map<Carta, Posicao> getPosicoesIniciais() {
        Map<Carta, Posicao> posicoes = new LinkedHashMap<>();

        posicoes.put(new Carta("Dona Branca", "/Imagens/padrao/personagens/Dona_Branca.png"), new Posicao(1, 8));
        posicoes.put(new Carta("Dona Violeta", "/Imagens/padrao/personagens/Dona_Violeta.png"), new Posicao(1, 9));
        posicoes.put(new Carta("Mordomo James", "/Imagens/padrao/personagens/Mordomo_James.png"), new Posicao(1, 10));
        posicoes.put(new Carta("Sargento Bigode", "/Imagens/padrao/personagens/Sargento_Bigode.png"), new Posicao(1, 17));
        posicoes.put(new Carta("Senhor Marinho", "/Imagens/padrao/personagens/Senhor_Marinho.png"), new Posicao(1, 18));
        posicoes.put(new Carta("Senhorita Rosa", "/Imagens/padrao/personagens/Senhorita_Rosa.png"), new Posicao(1, 19));
        posicoes.put(new Carta("Sergio Soturno", "/Imagens/padrao/personagens/Sergio_Soturno.png"), new Posicao(6, 1));
        posicoes.put(new Carta("Tony Gourmet", "/Imagens/padrao/personagens/Tony_Gourmet.png"), new Posicao(7, 1));

        return posicoes;
    }

    public Anotacoes anotacoesIniciais() {
        ArrayList<Anotacao> suspeitos = new ArrayList<>();
        for (Carta personagem : getAllPersonagens()) {
            suspeitos.add(new Anotacao(personagem));
        }

        ArrayList<Anotacao> armas = new ArrayList<>();
        for (Carta arma : getAllArmas()) {
            armas.add(new Anotacao(arma));
        }

        ArrayList<Anotacao> locais = new ArrayList<>();
        for (Carta local : getAllLocais()) {
            locais.add(new Anotacao(local));
        }

        return new Anotacoes(suspeitos, armas, locais);
    }

    public List<Carta> personagensDisponiveis(List<Carta> personagensEscolhidos) {
        final List<Carta> personagensDisponiveis = new ArrayList<>();
        for (Carta character : getAllPersonagens()) {
            if (!personagensEscolhidos.contains(character)) {
                personagensDisponiveis.add(character);
            }
        }
        return personagensDisponiveis;
    }

}
