angular.module('dojo').controller('ProcessoController', function ($scope, $http, ) {

    $scope.addProcesso = addProcesso;
    $scope.rmProcesso = rmProcesso;
    $scope.processamento = processamento;

    $scope.processos = [];
    $scope.processo = {
        nomeProcesso: null,
        tempoProcesso: null,
        tempoRestante: 0
    };
    $scope.msg = "";

    function addProcesso(p) {
        console.log($scope.processo);
        $scope.newProcesso = false;

        if (!$scope.processos)
            $scope.processos = [];

        var hasp = false;
        angular.forEach($scope.processos, function (opt, index) {
            if (opt.nomeProcesso == p.nomeProcesso)
                hasp = true;
        });

        if (!hasp) {
            $scope.processos.push(p);
            limparProcesso();
        }

    }

    function rmProcesso(p) {
        angular.forEach($scope.processos, function (opt, index) {
            if (opt == p)
                $scope.processos.splice(index, 1);
        });
    }

    function limparProcesso() {
        $scope.processo = {
            nomeProcesso: null,
            tempoProcesso: null,
            tempoRestante: 0
        };
    };

    function processamento() {

        angular.forEach($scope.processos, function (opt, index) {
            if (opt.tempoProcesso)
                opt.tempoRestante = opt.tempoProcesso;
        });

        var quantum = 2;
        var niveis = 3;
        var processosRestantes = 0;

        processosRestantes = $scope.processos.length;

        for (i = 1; i <= niveis; i++) {
            $scope.msg += " --- Nível " + i + "\n";
            for (j = 0; j < $scope.processos.length; j++) {
                $scope.msg += " ";
                if ($scope.processos[j].tempoRestante != 0) {
                    $scope.msg += " Processo " + $scope.processos[j].nomeProcesso + "\n";
                    $scope.msg += " Tempo Atual: " + $scope.processos[j].tempoRestante +"\n";
                    var conta = $scope.processos[j].tempoRestante - quantum;

                    $scope.processos[j].tempoRestante = (conta < 0 ? 0 : conta);
                    if ($scope.processos[j].tempoRestante == 0) {
                        processosRestantes--;
                    }
                    $scope.msg += " Tempo Restante: " + $scope.processos[j].tempoRestante +"\n";
                }
            }
        }

        while (processosRestantes > 0) {
            for (j = 0; j < $scope.processos.length; j++) {
                if ($scope.processos[j].tempoRestante > 0) {
                    $scope.msg += " Processo " + $scope.processos[j].nomeProcesso + "\n";
                    $scope.msg += " Tempo Atual: " + $scope.processos[j].tempoRestante +"\n";
                    var conta = $scope.processos[j].tempoRestante - quantum;

                    $scope.processos[j].tempoRestante = (conta < 0 ? 0 : conta);
                    $scope.msg += " Tempo Restante: " + $scope.processos[j].tempoRestante + "\n";
                } else {
                    processosRestantes--;
                }
            }
        }
    }
});