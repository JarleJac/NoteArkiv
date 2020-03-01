/**
 * 
 */

angular.module('notearkiv').controller('messageController', function($rootScope, $scope, $location, $routeParams, Messages, Auth, SimpleDlg) {
	$scope.isNew = $routeParams.messageNo === "new" ? true : false;
	$scope.pagePromise = null;
	if ($scope.isNew) {
		$scope.message = {messageType: "Normal"};
	}
	else {
		$scope.promiseMsg = "Henter melding";
		$scope.pagePromise = Messages.getMessage($routeParams.messageNo); 
		$scope.pagePromise.then(function successCallback(result) {
			//result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.message = result.data;
		});
	}
	$scope.newUser = function() {
		$scope.message = {messageType: "Normal"}
		$location.path("/messages/new");
	}
	$scope.saveMessage = function() {
		$scope.promiseMsg = "Lagrer melding";
		if ($scope.isNew) {
			$scope.pagePromise = Messages.addMessage($scope.message);			
		} else {
			$scope.pagePromise = Messages.updateMessage($scope.message);			
		}
		$scope.pagePromise.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Meldingen ble lagret ok");
			if ($scope.isNew)
				$location.path("/messages/" + result.data.id);
		});
		
	}
	$scope.deleteMessage = function() {
		SimpleDlg.runSimpleConfirmDlg("Bekreft sletting", "Er du sikker på at du vil slette melding " + $scope.message.id)
		.result.then(function (result) {
			$scope.promiseMsg = "Sletter melding";
			$scope.pagePromise = Messages.deleteMessage($scope.message);			
			$scope.pagePromise.then(function successCallback(result) {
				$rootScope.$emit('OkMessage', "Meldingen ble slettet");
				$location.path("/messages");
			});
		}); 
	}
	$scope.getMessageTypeText = function(type) {
		return Messages.getMessageTypeText(type);
	} 
})
;
