app.controller('AppController', function ($scope, $location) {
    $scope.menu = {
        items: ['HealthChecker']
    };

    $scope.isActiveItem = function (item) {
        return angular.lowercase($location.url()).indexOf(angular.lowercase(item)) !== -1;
    };

    $scope.goToLocation = function (url) {
        $location.path(url);
    };
});