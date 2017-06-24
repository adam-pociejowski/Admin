app.controller('SmsSenderController', function ($scope, smsService) {
    $scope.status = 'WAITING';
    $scope.sms = {
        to : null,
        message : null
    };

    var isNotNull = function(sms) {
        return sms.to !== null && sms.message !== null;
    };

    $scope.send = function() {
        if (isNotNull($scope.sms)) {
            var promise = smsService.sendSms($scope.sms);
            promise.success(function () {
                $scope.sms.to = null;
                $scope.sms.message = null;
                $scope.status = 'SENT';
            }).error(function(error, status) {
                status === 401 ? $scope.errorMessage = 'You have to be logged in to send sms.' : 'Problem while sending sms message.';
                $scope.status = 'ERROR';
            });
        } else {
            $scope.errorMessage = 'Both fields should be filled.';
            $scope.status = 'ERROR';
        }
    };
});