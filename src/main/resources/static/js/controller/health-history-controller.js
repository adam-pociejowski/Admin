app.controller('HealthHistoryController', function ($scope, healthCheckerService) {
    var maxDisplayPages = 9;

    $scope.pageable = {
        page : 0,
        size : 25,
        sort : 'reportDate'
    };

    $scope.pager = {
        start : 1,
        current : 1,
        displayAmount : maxDisplayPages,
        array : []
    };

    $scope.selectPage = function (pageNum) {
        $scope.pageable.page = pageNum - 1;
        $scope.pager.current = pageNum;
        getPageReport();
    };

    $scope.nextPage = function () {
        if (inPagesSet(1)) {
            $scope.pager.current++;
            if ($scope.pager.current >= $scope.pager.start + $scope.pager.displayAmount) {
                $scope.pager.start++;
                $scope.pager.array = getArrayNumber();
            }
            $scope.pageable.page = $scope.pager.current - 1;
            getPageReport();
        }
    };

    $scope.previousPage = function () {
        if (inPagesSet(-1)) {
            $scope.pager.current--;
            if ($scope.pager.current < $scope.pager.start) {
                $scope.pager.start--;
                $scope.pager.array = getArrayNumber();
            }
            $scope.pageable.page = $scope.pager.current - 1;
            getPageReport();
        }
    };

    $scope.changePageSize = function (size) {
        $scope.pageable.size = size;
        getPageReport();
        getReportsDetails();
        $scope.pager.current = 1;
        $scope.pager.start = 1;
        $scope.selectPage(1);
    };

    var inPagesSet = function (change) {
        var updated =  $scope.pager.current + change;
        return updated <= $scope.pager.amount && updated > 0;
    };

    var getArrayNumber = function() {
        var array = [];
        for (var i = 0; i < $scope.pager.displayAmount; i++) {
            array.push(i + $scope.pager.start);
        }
        return array;
    };

    var getPageReport = function() {
        var promise = healthCheckerService.getReports($scope.pageable);
        promise.success(function(data) {
            $scope.reports = data;
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

    var getReportsDetails = function () {
        var promise = healthCheckerService.getReportsDetails();
        promise.success(function(data) {
            $scope.reportsDetails = data;
            console.log($scope.reportsDetails);
            $scope.pager.amount = Math.floor($scope.reportsDetails.reportsAmount / $scope.pageable.size);
            if (data / $scope.pageable.size !== 0)
                $scope.pager.amount++;
            setPagesDisplayAmount();
        }).error(function() {});
    };

    var setPagesDisplayAmount = function () {
        if ($scope.pager.amount < maxDisplayPages) {
            $scope.pager.displayAmount = $scope.pager.amount;
        } else {
            $scope.pager.displayAmount = maxDisplayPages;
        }
        $scope.pager.array = getArrayNumber();
    };

    $scope.init = function () {
        getPageReport();
        getReportsDetails();
        $scope.pager.array = getArrayNumber();
    };
});
