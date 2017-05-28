detetiveApp.controller('PartidaController', ['$scope', 'DetetiveApi', '$interval', 'WebsocketService', function ($scope, DetetiveApi, $interval, WebsocketService   ) {

    $scope.partida = {};
    $scope.personagemEscolhido = {};
    $scope.indiceJogadorAtual = 0;
    $scope.personagensDisponiveis = [];
    $scope.palpite = {};
    $scope.palpiteFeito = {};
    /*
    $scope.palpiteFeito = {
        suspeito:{nome:"Senhor Marinho",src:"/Imagens/padrao/personagens/Senhor_Marinho.png"},
        arma:{nome:"Pé de Cabra",src:"/Imagens/padrao/armas/pe_de_cabra.png"},
        local:{nome:"Hotel",src:"/Imagens/padrao/locais/hotel.png"}
    };
    */

/*
<div class="perfil" style="background-image: url({{palpiteFeito.suspeito.src}}) "></div>
                        <span class="nome">{{palpiteFeito.suspeito.nome}}</span>
    $scope.numeroJogadas = 0;

    $scope.personagens = [];
    $scope.caminho = [];
    $scope.JogadorAtual = function(){
        return $scope.partida.jogadores[$scope.indiceJogadorAtual];
    }
*/
/*
    $scope.EnviarPalpite = function() {
        $('#palpiteModal').hide();
    }
*/
    $scope.DefinirEstilo = function(imagemDoFundo, corBordas, locais) {
        var conteudo = new Array();
        conteudo.push('#divTabuleiro {');
        conteudo.push('background-image: url("' + imagemDoFundo + '");');
        conteudo.push('background-repeat: no-repeat;');
        conteudo.push('background-size: 100%;}');
        conteudo.push(" .item_tab {border: 1px solid " + corBordas + ";}");
        for (var i = 1; i <= locais.length; i++) {
            conteudo.push(' .local' + i + ' { background-image: url("' + locais[i - 1].carta.src + '"); } ');
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

/*
    $scope.MoverListaJogadores = function(jogadores){
        var jogador = jogadores.shift();
        $scope.partida.jogadores.push(jogador);
    }
*/
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

/*
    $scope.ProximaJogada = function(){
        $scope.MoverListaJogadores();
        $scope.DestacarJogadorAtual();       
        $scope.RemoverAcaoAndar();   
        $scope.AbrirModalLancarDados();
        $scope.IniciarTimer();
    }
*/

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

/*
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
        $scope.mostrarCaminhosDisponiveis();
    }
*/
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

    $scope.AbrirModalPalpite = function() {
        $('#palpiteModal').show();
        $scope.palpite = {};
    }

    $scope.setPalpite = function(nome,carta) {
        $scope.palpite[nome] = carta;
    }

    $scope.DesabilitarPalpite = function() {
        return (
            $scope.palpite.suspeito == undefined ||
            $scope.palpite.arma == undefined ||
            $scope.palpite.local == undefined 
        );
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

    //

    // ENVIA PERSONAGEM ESCOLHIDO
    $scope.enviarPersonagem = function() {
        WebsocketService.send_command(escolherPersonagemResponse($scope.personagemEscolhido), function() {
            $("#choice-characters-modal").modal("hide");
        });
    };

    // ENVIA PALPITE
    $scope.enviarPalpite = function() {
        WebsocketService.send_command(enviarPalpiteResponse($scope.palpite), function() {
            $('#palpiteModal').hide();
        });
    }

    // RECEBE COMANDOS
    WebsocketService.receive_command().then(null, null, function(data) {
        var service = data[0];
        var command = data[1];
        console.log(command);
        if (command.type == "AGUARDAR_INICIO_JOGO") {
            jogador = command.options;
            DetetiveApi.setMeuJogador(jogador);
            DetetiveApi.setAnotacoes(jogador.anotacoes);
            DetetiveApi.PegarDadosPartida(1, function(result) {
                var partida = result;
                $scope.partida = partida;
                $scope.DefinirEstilo(partida.imagemFundoPath, partida.corDaBorda, partida.barraAnotacao.locais);
                $scope.PosicionarJogador(partida.meuJogador);
            });
            WebsocketService.send_command(aguardarInicioOkResponse(), null);
        }

        if (command.type == "ESPERAR_VEZ") {
            // console.log(command);
        }

        if (command.type == "LANCAR_DADOS") {
            alert("voce vai tirar 5!")
            WebsocketService.send_command(lancarDadosResponse(5), null);
        }

        if (command.type == "MOVER_JOGADOR") {
            alert("mover peca " + command.options + " casas.");
            $scope.numeroJogadas = command.options;
            $scope.DestacarJogadorAtual($scope.partida.meuJogador);
            $scope.MostrarCasasDisponiveisParaAndar($scope.partida.meuJogador.posicao.posicao);
            $scope.HabilitarClickAndar($scope.partida.meuJogador, $scope.numeroJogadas, function(posicao, comodo) {
                WebsocketService.send_command(moverJogadorResponse(posicao, comodo), null);
            });
        }
        if (command.type == "FAZER_PALPITE") {
            $scope.AbrirModalPalpite();
        }
    });

    // USER INFO
    WebsocketService.receive_user_info().then(null, null, function(info) {
        console.log(info);
        if (info.type == "PERSONAGENS_DISPONIVEIS") {
            $scope.personagensDisponiveis = info.body;
            $("#choice-characters-modal").modal("show");
        }
    });

    // GAME INFO
    WebsocketService.receive_game_info().then(null, null, function(info) {
        console.log(info);
        if (info.type == "JOGADORES") {
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
            $("#palpiteFeitoModal").modal("show");
        }
    });

}]);