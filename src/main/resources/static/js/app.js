var app = angular.module('App', ['ngAnimate', 'ngResource', 'ngRoute', 'ngMaterial'])
    .config(function ($routeProvider) {
        $routeProvider.when('/login', {
            templateUrl : 'login.html',
            controller : 'LoginController'
        }).when('/healthchecker', {
            templateUrl : 'healthchecker.html',
            controller : 'HealthCheckerController'
        }).otherwise(
            { redirectTo: '/healthchecker'}
        );
    });