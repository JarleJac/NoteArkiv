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
			$scope.message = result.data.message;
		});
	}
	$scope.newUser = function() {
		$location.path("/mesagess/new");
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
				$location.path("/users/" + result.data.id);
		});
		
	}
//	$scope.deleteUser = function() {
//		SimpleDlg.runSimpleConfirmDlg("Bekreft sletting", "Er du sikker på at du vil slette " + $scope.user.name)
//		.result.then(function (result) {
//			$scope.promiseMsg = "Sletter bruker";
//			$scope.pagePromise = Users.deleteUser($scope.user);			
//			$scope.pagePromise.then(function successCallback(result) {
//				$rootScope.$emit('OkMessage', "Bruker ble slettet");
//				$location.path("/users");
//			});
//		}); 
//	}
	$scope.getMessageTypeText = function(type) {
		return Messages.getMessageTypeText(type);
	} 
})
;
