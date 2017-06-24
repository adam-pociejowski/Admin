app.factory('smsService', ['$http', function ($http) {

    return {
        sendSms: function (sms) {
            return $http.post('/sms/send', sms);
        }
    };
}]);