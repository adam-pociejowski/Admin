app.factory('healthCheckerService', ['$http', function ($http) {
    var BASE_URL = '/healthchecker/rest';

    return {
        getLastReport: function() {
            return $http.get(BASE_URL + '/getlastreport');
        },
        getActualStatuses: function() {
            return $http.get(BASE_URL + '/getstatuses')
        }
    };
}]);