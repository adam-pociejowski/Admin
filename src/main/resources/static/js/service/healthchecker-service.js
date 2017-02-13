app.factory('healthCheckerService', ['$http', function ($http) {
    var BASE_URL = '/healthchecker/rest/';

    return {
        getActualStatuses: function() {
            return $http.get(BASE_URL + 'getstatuses');
        },
        getReports: function (pageable) {
            return $http.get(BASE_URL + 'getreports', {
                    params: {
                        page : pageable.page,
                        size : pageable.size,
                        sort : pageable.sort
                    }
            });
        },
        saveApp: function (app) {
            return $http.post(BASE_URL + 'save-app', app);
        },
        deleteApp: function (app) {
            return $http.post(BASE_URL + 'delete-app', app);
        },
        getDetails: function () {
            return $http.get(BASE_URL + 'getdetails');
        },
        getReportsDetails: function() {
            return $http.get(BASE_URL + 'getReportsDetails');
        }
    };
}]);