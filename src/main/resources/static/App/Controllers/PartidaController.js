detetiveApp.controller('PartidaController', ['$scope', 'DetetiveApi', '$interval', 'WebsocketService', function ($scope, DetetiveApi, $interval, WebsocketService   ) {

    $scope.partida = {};
    $scope.personagemEscolhido = {};
    $scope.indiceJogadorAtual = 0;
    $scope.personagensDisponiveis = [];
    $scope.palpite = {};
    $scope.palpiteFeito = {};
    $scope.cartaSelecionada = {};
    $scope.jogoStatus = "AGUARDAR_INICIO_JOGO"
    $scope.jogadores = [];
    $scope.messages = [];
    $scope.message = "";
    $scope.max = 140;
    $scope.mestre = false;
    $scope.jogando = false;
    $scope.jogadasPossiveis = [];
    $scope.jogadaEscolhida = "Lançar dados";
    $scope.acusacao = {};

    var local_carta = [];

    $scope.DefinirEstilo = function(imagemDoFundo, corBordas, locais) {
        var conteudo = new Array();
        conteudo.push('#divTabuleiro {');
        conteudo.push('background-image: url("' + imagemDoFundo + '");');
        conteudo.push('background-repeat: no-repeat;');
        conteudo.push('background-size: 100%;}');
        conteudo.push(" .item_tab {border: 1px solid " + corBordas + ";}");
        for (var i = 1; i <= locais.length; i++) {
            conteudo.push(' .local' + i + ' { background-image: url("' + locais[i - 1].carta.src + '"); } ');
            local_carta.push({ local: "local"+i, carta: locais[i - 1].carta }); //locais[i - 1].carta;
        }
        $('#styleDynamic').html(conteudo.join(''));
    }
    
    $scope.PosicionarJogadores = function(jogadores) {
      for(var i = 0; i < jogadores.length; i++){
          var jogador = jogadores[i];
          $scope.PosicionarJogador(jogador);
      }
    }

    $scope.PosicionarJogador = function(jogador) {
        if(jogador.posicao){
            var id = jogador.posicao.posicao.join('_');
            var img = document.createElement('img');
            img.src = jogador.personagem.src;
            img.className = 'peao';
            img.id = 'peao_'+jogador.id;
            $('#'+id).html(img);
        }
    }

    $scope.VerCarta = function(id, tipo) {
        var nomeObj = ['suspeitos','armas','locais'];
        var itensAnotacao = $scope.partida.barraAnotacao[nomeObj[tipo - 1]];
        var item = Enumerable.from(itensAnotacao).singleOrDefault(function(x){
            return x.carta.id == id;
        });
        item.selecionado = true;
        item.minhaCarta = true;
    }

    $scope.switcheryOptions = {
        color: 'green',
        secondaryColor: '#a52323',
        size: 'small'
    };

    $scope.RemoverDestaqueJogador = function(){
         $('div').removeClass('jogador_ativo');
    }

    $scope.DestacarJogadorAtual = function(jogadorAtual){
         $scope.RemoverDestaqueJogador();
         $('#peao_'+jogadorAtual.id).parent().addClass('jogador_ativo');
    }

    $scope.AbrirModalLancarDados = function(){
        $('.lancar_dados').show();        
    }

    $scope.FecharModalLancarDados = function(){
        $('.lancar_dados').hide();
    }

    $scope.RemoverAcaoAndar = function(){
        $('div.casaDispo').unbind( "click" );
        $('div.casaDispo').removeClass('casaDispo');
    }

    $scope.PegarNumeroDeJogadas = function(){
        var valor1 = Math.floor(Math.random() * 6) + 1;
        var valor2 = Math.floor(Math.random() * 6) + 1;
        return valor1 + valor2;
    }

    $scope.AbrirModalNumeroJogadas = function(){ 
        $('.resultado').show();
        setTimeout(function(){
            $('.resultado').hide();
        },2000);
    }

    $scope.PararDados = function(){
        $scope.FecharModalLancarDados();
        $scope.numeroJogadas = $scope.PegarNumeroDeJogadas();
        $scope.AbrirModalNumeroJogadas();
        WebsocketService.send_command(lancarDadosResponse($scope.numeroJogadas), null);
        //$scope.mostrarCaminhosDisponiveis();
    }

    $scope.MostrarCasasDisponiveisParaAndar = function(posicao) {
        var disponibilidades = [
            {
                row: posicao[0] - 1,
                col: posicao[1]
            },
            {
                row: posicao[0] + 1,
                col: posicao[1]
            },
            {
                row: posicao[0] ,
                col: posicao[1] - 1
            },
            {
                row: posicao[0] ,
                col: posicao[1] + 1
            }
        ];

        for(var i = 0; i < disponibilidades.length; i++) {
            var row = disponibilidades[i].row;
            var col = disponibilidades[i].col;
            var div = $('#'+row+'_'+col);
            
            if($scope.PossoAndarNaCasa(div))
                div.addClass('casaDispo');

        }
    }

    $scope.mostrarCaminhosDisponiveis = function(jogadorAtual, numeroJogadas, callback) {
        if(jogadorAtual.casasParaAndar == undefined){
            jogadorAtual._posicao = jogadorAtual.posicao.posicao;
        }       

        $scope.MostrarCasasDisponiveisParaAndar(jogadorAtual.posicao.posicao);
        $scope.HabilitarClickAndar(jogadorAtual, numeroJogadas, callback);
    }

    $scope.HabilitarClickAndar = function(jogadorAtual, numeroJogadas, callback) {
        $('.casaDispo').click(function() {

            $scope.RemoverAcaoAndar();

            numeroJogadas -= 1;

            var div = $(this);
            if ($scope.PosicaoEhPorta(jogadorAtual, numeroJogadas, div, callback)) {
                return;
            }

            var id = div.attr('id').split('_');
            jogadorAtual.posicao.posicao = [+id[0], +id[1]];
            $scope.DeslocarImg(jogadorAtual);

            if(numeroJogadas > 0) {
                $scope.mostrarCaminhosDisponiveis(jogadorAtual, numeroJogadas, callback);
            } else {
                alert('Sua vez acabou');
                $scope.DesativarTimer();
                if (callback)
                    callback(jogadorAtual.posicao.posicao, null);
            }
        });
    }

    $scope.DeslocarImg = function(jogadorAtual) {
        var id = '#peao_'+jogadorAtual.id;
        var img = $(id);
        img.parent().empty();
        var novaPosicao = jogadorAtual.posicao.posicao.join('_');
        $('#'+novaPosicao).append(img);
    }

    $scope.DesativarTimer = function() {
        $interval.cancel(interval);
        $scope.mostrarTimer = false;
        //$scope.ProximaJogada();
    }

    $scope.PegarLocalComodo = function(local) {
        var qtdeImg = 0;
        var div;
        do{
            var random = Math.floor(Math.random()*10);
            div =  $("."+local).eq(random);
            qtdeImg = div.find('img');
        } while(qtdeImg == 0);
        return div;
    }

    $scope.PosicionarImgNoComodo = function(jogadorAtual, local) {
        var div = $scope.PegarLocalComodo(local);
        var img = $('#peao_'+jogadorAtual.id);
        img.parent().empty();
        div.append(img);
    }

    $scope.JogadorPodeFazerAcusacao = function() {
        
    }

    $scope.AbrirModalPalpite = function(posicao) {
        for (var i = 0; i <= local_carta.length -1; i++) {
            if (local_carta[i].local == posicao.comodo) {
                $scope.carta_comodo_jogador = {carta: local_carta[i].carta};
            }
        }
        $scope.palpite = {};
        $('#palpiteModal').modal({backdrop: "static"});
    }

    $scope.setPalpite = function(nome, carta) {
        $scope.palpite[nome] = carta;
    }

    $scope.DesabilitarPalpite = function() {
        return (
            $scope.palpite.suspeito == undefined ||
            $scope.palpite.arma == undefined ||
            $scope.palpite.local == undefined 
        );
    }

    $scope.AbrirModalAcusacao = function() {
        $scope.acusacao = {};
        $('#acusacaoModal').modal({backdrop: "static"});
    }

    $scope.setAcusacao = function(nome, carta) {
        $scope.acusacao[nome] = carta;
    }

    $scope.DesabilitarAcusacao = function() {
        return (
            $scope.acusacao.suspeito == undefined ||
            $scope.acusacao.arma == undefined ||
            $scope.acusacao.local == undefined
        );
    }

    $scope.desabilitarExibirCarta = function() {
        return ($scope.cartaSelecionada.nome == undefined);
    }

    $scope.setCartaSelecionada = function(carta) {
        $scope.cartaSelecionada = carta;
    }

    $scope.PosicaoEhPorta = function(jogadorAtual, numeroJogadas, div, callback) {
        if(div.hasClass('porta') && numeroJogadas > 1){
            var resultado = confirm('Deseja entrar?');
            if(resultado){
                $scope.RemoverAcaoAndar();
                numeroJogadas = 0;

                var local = div.attr('comodo');
                var posicao = div.attr('id').split('_');
                jogadorAtual.posicao.posicao = [+posicao[0], +posicao[1]];
                $scope.PosicionarImgNoComodo(jogadorAtual, local);
                if (callback)
                    callback(jogadorAtual.posicao.posicao, local);
                return true;
            }
            return false;
        }
        return false;
    }

    $scope.PossoAndarNaCasa = function(div){
        if(div.attr('id') == undefined)
            return false;
        
        var classes = div.attr('class').split(' ');

        for(var i = 0; i< classes.length;i++){
            if(classes[i].indexOf('local') != -1)
                return false;
        }

        if(div.find('img').length != 0)
             return false;

         return true;
    }
   
    $scope.PerdeuAVez = function(){
        $interval.cancel(interval);
        alert('perdeu a vez');
        $scope.mostrarTimer = false;
        $('.lancar_dados').hide();
        $('.resultado').hide();
        $('#palpiteModal').hide();
        //$scope.ProximaJogada();
    }

    var interval;
    $scope.IniciarTimer = function(){
        $scope.mostrarTimer = true;
        $scope.tempo = 180;
        interval = $interval(function(){
            $scope.tempo -= 1;
            if($scope.tempo < 0){
                $scope.PerdeuAVez();
            }
        },1000);
    }

    // ESCOLHER PERSONAGEM RESPONSE
    escolherPersonagemResponse = function(personagem) {
        return JSON.stringify({ type: 'ESCOLHER_PERSONAGEM', response: { nome: personagem.nome, src: personagem.src }});
    }

    // AGUARDAR INICIO RESPONSE
    aguardarInicioOkResponse = function() {
        return JSON.stringify({ type: 'AGUARDAR_INICIO_JOGO', response: "OK" });
    }

    // ESCOLHER JOGADA RESPONSE
    escolherJogadaResponse = function(jogada) {
        return JSON.stringify({ type: 'ESCOLHER_JOGADA', response: jogada });
    }

    // LANCAR DADOS RESPONSE
    lancarDadosResponse = function(resultado) {
        return JSON.stringify({ type: 'LANCAR_DADOS', response: resultado });
    }

    // MOVER JOGADOR RESPONSE
    moverJogadorResponse = function(posicao, comodo) {
        return JSON.stringify({ type: 'MOVER_JOGADOR', response: { posicao: posicao, comodo: comodo }});
    }

    // ENVIAR PALPITE
    enviarPalpiteResponse = function(palpite) {
        return JSON.stringify({ type: 'FAZER_PALPITE', response: { palpite }});
    }

    // ENVIAR ACUSACAO
    enviarAcusacaoResponse = function(acusacao) {
        return JSON.stringify({ type: 'FAZER_ACUSACAO', response: { acusacao }});
    }

    // ENVIAR CARTA SELECIONADA
    enviarCartaSelecionadaResponse = function(carta) {
        return JSON.stringify({ type: 'EXIBIR_CARTA', response: { carta }});
    }

    // ENVIAR CARTA VISTA OK
    enviarCartaVistaOkResponse = function() {
        return JSON.stringify({ type: 'VER_CARTA', response: "OK" });
    }

    // ENVIAR ERROU ACUSACAO OK
    enviarErrouAcusacaoOkResponse = function() {
        return JSON.stringify({ type: 'FORA_DA_PARTIDA', response: "OK" });
    }

    //
    $scope.IniciarPartida = function() {
        WebsocketService.send_command(aguardarInicioOkResponse(), function() {});
    }

    // ENVIA PERSONAGEM ESCOLHIDO
    $scope.enviarPersonagem = function() {
        WebsocketService.send_command(escolherPersonagemResponse($scope.personagemEscolhido), function() {
            $("#choice-characters-modal").modal("hide");
        });
    };

    // ENVIA JOGADA ESCOLHIDA
    $scope.enviarJogadaEscolhida = function() {
        WebsocketService.send_command(escolherJogadaResponse($scope.jogadaEscolhida), function() {
            $("#escolherJogadaModal").modal("hide");
            $scope.jogadaEscolhida = "Lançar dados";
        });
    };

    // ENVIA PALPITE
    $scope.enviarPalpite = function() {
        WebsocketService.send_command(enviarPalpiteResponse($scope.palpite), function() {
            $('#palpiteModal').modal("hide");
            $("#palpiteAguardarRetornoModal").modal({backdrop: "static"});
        });
    }

    // ENVIA ACUSACAO
    $scope.enviarAcusacao = function() {
        WebsocketService.send_command(enviarAcusacaoResponse($scope.acusacao), function() {
            $('#acusacaoModal').modal("hide");
            $scope.acusacao = {};
        });
    }

    // ENVIA CARTA SELECIONADA
    $scope.enviarCartaSelecionada = function() {
        WebsocketService.send_command(enviarCartaSelecionadaResponse($scope.cartaSelecionada), function() {
            $('#selecionarCartaModal').modal("hide");
        });
    }

    // ENVIA OK PARA CARTA VISTA
    $scope.enviarCartaVistaOk = function() {
        WebsocketService.send_command(enviarCartaVistaOkResponse(), function() {
            $('#exibeCartaModal').modal("hide");
        });
    }

    // ENVIA OK PARA FORA DA PARTIDA
    $scope.enviarForaDaPartidaOk = function() {
        WebsocketService.send_command(enviarErrouAcusacaoOkResponse(), function() {});
    }

    // RECEBE COMANDOS
    WebsocketService.receive_command().then(null, null, function(data) {
        var service = data[0];
        var command = data[1];

        if (command.type == "AGUARDAR_INICIO_JOGO") {
            jogador = command.options;
            $scope.jogoStatus = "AGUARDAR_INICIO_JOGO"
            $scope.mestre = jogador.mestre;
            $("#chatModal").modal({backdrop: "static"});
        }

        if (command.type == "ESPERAR_VEZ") {
            if (!$scope.jogando) {
                $("#chatModal").modal("hide");
                $scope.jogando = true;
                jogador = command.options[0];
                anotacoes = command.options[1];
                DetetiveApi.setMeuJogador(jogador);
                DetetiveApi.setAnotacoes(anotacoes);
                DetetiveApi.PegarDadosPartida(1, function(result) {
                    var partida = result;
                    $scope.partida = partida;
                    $scope.DefinirEstilo(partida.imagemFundoPath, partida.corDaBorda, partida.barraAnotacao.locais);
                    $scope.PosicionarJogador(partida.meuJogador);
                });
            }
        }

        if (command.type == "ESCOLHER_JOGADA") {
            $scope.jogadasPossiveis = command.options;
            $("#escolherJogadaModal").modal({backdrop: "static"});
        }

        if (command.type == "LANCAR_DADOS") {
            $scope.AbrirModalLancarDados()
        }

        if (command.type == "MOVER_JOGADOR") {
            $scope.numeroJogadas = command.options;
            $scope.DestacarJogadorAtual($scope.partida.meuJogador);
            $scope.MostrarCasasDisponiveisParaAndar($scope.partida.meuJogador.posicao.posicao);
            $scope.HabilitarClickAndar($scope.partida.meuJogador, $scope.numeroJogadas, function(posicao, comodo) {
                WebsocketService.send_command(moverJogadorResponse(posicao, comodo), null);
            });
        }

        if (command.type == "FAZER_PALPITE") {
            $scope.AbrirModalPalpite(command.options);
        }

        if (command.type == "EXIBIR_CARTA") {
            $scope.palpiteFeito = command.options;
            $("#palpiteAguardarRetornoModal").modal("hide");
            $("#selecionarCartaModal").modal({backdrop: "static"});
        }

        if (command.type == "VER_CARTA") {
            $scope.cartaExibida = command.options;
            $("#palpiteAguardarRetornoModal").modal("hide");
            $("#exibeCartaModal").modal({backdrop: "static"});
        }

        if (command.type == "FAZER_ACUSACAO") {
            $scope.AbrirModalAcusacao();
        }

        if (command.type == "FORA_DA_PARTIDA") {
            $scope.enviarForaDaPartidaOk();
            $scope.palpitePerdedor = command.options[0];
            $scope.personagemCrime = command.options[1];
            $scope.armaCrime = command.options[2];
            $scope.localCrime = command.options[3];
            $("#jogadorForaVeCartasCrimeModal").modal({backdrop: "static"});
        }
    });

    // USER INFO
    WebsocketService.receive_user_info().then(null, null, function(info) {
        if (info.type == "PERSONAGENS_DISPONIVEIS") {
            $scope.personagensDisponiveis = info.body;
            $("#choice-characters-modal").modal({backdrop: "static"});
        }
    });

    // GAME INFO
    WebsocketService.receive_game_info().then(null, null, function(info) {
        if (info.type == "JOGADORES") {
            $scope.jogadores = info.body
            DetetiveApi.setJogadores(info.body);
            $scope.PosicionarJogadores(info.body);
        }

        if (info.type == "JOGADOR_ATUAL") {
            $scope.jogadorDaVez = info.body;
        }

        if (info.type == "ULTIMO_JOGADOR") {
            var jogadorAtual = info.body;
            if (jogadorAtual.posicao.comodo) {
                $scope.PosicionarImgNoComodo(jogadorAtual, jogadorAtual.posicao.comodo);
            } else {
                $scope.DeslocarImg(jogadorAtual);
            }
        }

        if (info.type == "PALPITE_FEITO") {
            $scope.palpiteFeito = info.body;
            if ($scope.palpiteFeito.jogador.usuario != DetetiveApi.getMeuJogador().usuario) {
                $("#palpiteFeitoModal").modal("show");
            }
        }

        if (info.type == "GANHOU_PARTIDA") {
            $scope.palpiteGanhador = info.body;
            if ($scope.palpiteGanhador.jogador.usuario == DetetiveApi.getMeuJogador().usuario) {
                $scope.mensagemGanhador = "VOCÊ GANHOU A PARTIDA!!!";
            } else {
                $scope.mensagemGanhador = "O jogador " + $scope.palpiteGanhador.jogador.personagem.nome + " (" + $scope.palpiteGanhador.jogador.usuario + ") acertou a acusação e ganhou a partida!";
            }
            $("#jogadorGanhouPartidaModal").modal({backdrop: "static"});
        }

        if (info.type == "JOGADOR_FORA_DA_PARTIDA") {
            $scope.palpitePerdedor = info.body[0];
            if ($scope.palpitePerdedor.jogador.usuario != DetetiveApi.getMeuJogador().usuario) {
                $scope.cartasSuspeitosPerdedor = info.body[1];
                $scope.cartasArmasPerdedor = info.body[2];
                $scope.cartasLocaisPerdedor = info.body[3];
                $scope.mensagemPerdedor = "O jogador " + $scope.palpitePerdedor.jogador.personagem.nome + " (" + $scope.palpitePerdedor.jogador.usuario + ") errou a acusação e está fora da partida!";
                $("#jogadorForaExibeCartasModal").modal({backdrop: "static"});
            }
        }
    });

    // CHAT
    $scope.addMessage = function() {
        WebsocketService.send_message($scope.message);
        $scope.message = "";
    };

    WebsocketService.receive_message().then(null, null, function(message) {
        $scope.messages.push(message);
    });

}]);