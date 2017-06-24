var app = angular.module('App', ['ngAnimate', 'ui.bootstrap', 'ngResource',
    'ngRoute', 'ngMaterial'])
    .config(function ($routeProvider) {
        $routeProvider.when('/login', {
            templateUrl : 'login.html',
            controller : 'LoginController'
        }).when('/healthchecker', {
            templateUrl : 'healthchecker.html',
            controller : 'HealthCheckerController'
        }).when('/healthchecker/history', {
            templateUrl : 'health-history.html',
            controller : 'HealthCheckerController'
        }).when('/sms', {
            templateUrl : 'sms-sender.html',
            controller : 'SmsSenderController'
        }).otherwise(
            { redirectTo: '/healthchecker'}
        );
    });