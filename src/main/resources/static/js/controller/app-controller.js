app.controller('AppController', function ($scope, $location, loginService) {
    $scope.menu = {
        items: [{label : 'HealthChecker', path : "healthchecker"},
                {label: 'Sms sender', path : "sms"}]
    };
    $scope.isAuthenticated = false;
    $scope.loggedUser = {};

    $scope.isActiveItem = function (item) {
        return angular.lowercase($location.url()).indexOf(angular.lowercase(item)) !== -1;
    };

    $scope.goToLocation = function (url) {
        $location.path(url);
    };

    $scope.logout = function() {
        var promise = loginService.logout();
        promise.success(function () {
            $location.path('login');
        }).error(function() {});
    };

    $scope.authenticate = function (callback) {
        var promise = loginService.authenticate();
        promise.success(function (response) {
            $scope.isAuthenticated = true;
            $scope.loggedUser = response.name;
            callback();
        }).error(function () {
            $scope.isAuthenticated = false;
            $scope.loggedUser = null;
        });
    };
});