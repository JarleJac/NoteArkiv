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
//	$scope.saveUser = function() {
//		$scope.promiseMsg = "Lagrer bruker";
//		if ($scope.isNew) {
//			if($scope.user.password !== $scope.repeatPassword) {
//				$rootScope.$emit('ErrorMsg', "Du må angi samme passord i begge passord feltene");
//				return;
//			} else {
//				$scope.pagePromise = Users.addUser($scope.user);			
//			}
//		} else {
//			$scope.pagePromise = Users.updateUser($scope.user);			
//		}
//		$scope.pagePromise.then(function successCallback(result) {
//			$rootScope.$emit('OkMessage', "Bruker ble lagret ok");
//			if ($scope.isNew)
//				$location.path("/users/" + result.data.no);
//		});
//		
//	}
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
