"use strict";
(function (window) {
    angular.module("buttonClicker", []).controller("clickController", ["$scope", "$http", "$interval", function ($scope, $http, $interval) {
        // Variables
        $scope.model = {};
        $scope.model.playerName = "Unknown player";
        $scope.model.winners = [];
        $scope.model.messageShown = false;
        $scope.model.messageText = "No error";
        $scope.model.clicksShown = false;
        $scope.model.clickCount = 0;

        // Functions
        $scope.actions = {};
        $scope.actions.sendClick = function() {
            $http.post("/", []).then(function success(response) {
                $scope.model.clickCount = response.data.clicks;
                $scope.model.clicksShown = true;
                if (response.data.price != null) {
                    $scope.model.messageText = "You've won " + response.data.price + "!";
                    $scope.model.messageShown = true;
                    document.getElementById("message").className = "success";
                    $scope.actions.updateList();
                }
            }, function fail(reason) {
                $scope.model.messageText = "Click failed :< Try reloading the page";
                $scope.model.messageShown = true;
                document.getElementById("message").className = "error";
                console.log(reason);
            });
        };
        $scope.actions.updateList = function () {
            $http.get("/winners").then(
                function (response) {
                    $scope.model.winners = response.data;
                }, function (reason) {
                    console.log(reason);
                }
            );
        };

        // Other
        $scope.actions.updateList();
        $http.get("/name").then(function (response) {
            $scope.model.playerName = response.data.name;
        }, function (reason) {
            $scope.model.messageText = "Could not retrieve player name. Try reloading the page";
            $scope.model.messageShown = true;
            document.getElementById("message").className = "error";
            console.log(reason);
        });
        $interval($scope.actions.updateList, 1000 * 20);
    }]);
})(window);