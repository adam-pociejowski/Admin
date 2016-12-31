app.controller('HealthHistoryController', function ($scope, healthCheckerService) {

    var getAllReports = function () {
        var promise = healthCheckerService.getAllReports();
        promise.success(function(data) {
            $scope.allReports = data;
        }).error(function() {});
    };

    $scope.showReport = function (report) {
        if ($scope.selectedReport !== undefined && $scope.selectedReport !== null) {
            $scope.selectedReport.show = false;
        }
        report.show = true;
        $scope.selectedReport = report;
    };

    $scope.hideReport = function (report) {
        report.show = false;
        $scope.selectedReport = null;
    };

    $scope.init = function () {
        getAllReports();
        console.log('con');
    };
});
