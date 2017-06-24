app.controller('LoginController', function ($scope, $location, loginService) {

    $scope.login = function() {
        var promise = loginService.login($scope.credentials);
        promise.success(function () {
            $location.path('healthchecker');
        }).error(function() {
            $scope.login.error = true;
        });
    };
});