app.controller('HealthCheckerController', function ($scope, $timeout, $interval, healthCheckerService) {
    $scope.apps = [];
    $scope.lastUpdate = 'Unknown';

    $scope.socket = {
        client: null,
        stomp: null
    };

    var getLastReport = function () {
        var promise = healthCheckerService.getLastReport();
        promise.success(function (data) {
            $scope.apps = data.appReports;
            $scope.lastUpdate = data.reportDate;
        }).error(function () {});
    };

    var reconnect = function() {
        setTimeout($scope.initSockets, 10000);
    };

    var initSockets = function() {
        $scope.socket.client = new SockJS('/gs-guide-websocket');
        $scope.socket.stomp = Stomp.over($scope.socket.client);
        $scope.socket.stomp.connect({}, function() {
            $scope.socket.stomp.subscribe('/topic/greetings', function (data) {
                $scope.apps = angular.fromJson(data.body);
                $timeout(angular.noop);
                $scope.lastUpdate = Date.now();
            });
        });
        $scope.socket.client.onclose = reconnect;
    };

    $interval(function() {
        $scope.wasLastUpdateAgo = parseInt((Date.now() - $scope.lastUpdate) / 1000);
    }, 1000);

    $scope.init = function () {
        getLastReport();
        initSockets();
    };
});