detetiveApp.factory('DetetiveApi',[function(){
    var app  = {};
    var partida = {
        imagemFundoPath: '/Imagens/padrao/fundo.jpg',
        corDaBorda: '#d6bebe',
        meuJogador: {usuario: '', personagem: {src: '', nome: ''}, posicao: {posicao: []}},
        barraAnotacao: {supeitos: [], armas: [], locais: []},
        jogadores: []
    };

 function MinhasCartas(array, indice){
        var retorno = new Array();
        for(var i = 0; i< array.length; i++){
            var item = {
                carta: array[i],
                minhaCarta: false,
                selecionado: false
            };
            if(i < indice){
                item.minhaCarta = true;
                item.selecionado = true;
            }
            retorno.push(item);
        }
        return retorno;
    }

    app.PegarDadosPartida = function(id, callbackSucesso, callbackErro){
        callbackSucesso(partida);
    }

    app.setMeuJogador = function(jogador) {
        partida.meuJogador = jogador;
    }

    app.setAnotacoes = function(anotacoes) {
        partida.barraAnotacao = {
            suspeitos: anotacoes.suspeitos,
            armas: anotacoes.armas,
            locais: anotacoes.locais
        };
    }

    app.setJogadores = function(jogadores) {
        partida.jogadores = jogadores;
    }

    return app;

}])