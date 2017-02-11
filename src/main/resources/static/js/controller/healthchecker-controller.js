app.controller('HealthCheckerController', function ($scope, $timeout, $interval, healthCheckerService) {
    $scope.lastUpdate = 'Unknown';
    $scope.details = [];

    $scope.socket = {
        client: null,
        stomp: null
    };

    $scope.reset = function () {
        $scope.editedApp = null;
        $scope.deletingApp = null;
    };

    $scope.setEditedApp = function (app) {
        $scope.editedApp = app;
    };

    $scope.setDeletingApp = function (app) {
        $scope.deletingApp = app;
    };

    $scope.saveApp = function () {
        var promise = healthCheckerService.saveApp($scope.editedApp);
        promise.success(function () {
            $scope.init();
        }).error(function () {});
    };

    $scope.deleteApp = function () {
        var promise = healthCheckerService.deleteApp($scope.deletingApp);
        promise.success(function () {
            $scope.init();
        }).error(function () {});
    };

    var getDetails = function () {
        var promise = healthCheckerService.getDetails();
        promise.success(function (data) {
            $scope.details = data.details;
            $scope.lastUpdate = data.reportDate;
        }).error(function () {});
    };

    var reconnect = function () {
        setTimeout($scope.initSockets, 10000);
    };

    var initSockets = function () {
        $scope.socket.client = new SockJS('/gs-guide-websocket');
        $scope.socket.stomp = Stomp.over($scope.socket.client);
        $scope.socket.stomp.connect({}, function() {
            $scope.socket.stomp.subscribe('/topic/greetings', function (data) {
                refreshAppsStatuses(angular.fromJson(data.body));
                $timeout(angular.noop);
                $scope.lastUpdate = Date.now();
            });
        });
        $scope.socket.client.onclose = reconnect;
    };

    $interval(function () {
        $scope.wasLastUpdateAgo = parseInt((Date.now() - $scope.lastUpdate) / 1000);
    }, 1000);


    $scope.refreshStatuses = function () {
        var promise = healthCheckerService.getActualStatuses();
        promise.success(function(data) {
            refreshAppsStatuses(data);
            $scope.lastUpdate = Date.now();
        }).error(function () {});
    };

    var refreshAppsStatuses = function (applicationDetails) {
        for (var i = 0; i < applicationDetails.length; i++) {
            var details = applicationDetails[i];
            var index = getDetailsByAppId(details.application.id);
            if (index !== -1) {
                $scope.details[index].state = applicationDetails[i].state;
            }
        }
    };

    var getDetailsByAppId = function (appId) {
        for (var i = 0; i < $scope.details.length; i++) {
            var application = $scope.details[i].application;
            if (application.id === appId) {
                return i;
            }
        }
        return -1;
    };

    $scope.init = function () {
        getDetails();
        initSockets();
    };
});