/**
 * 
 */

angular.module('notearkiv').controller('userController', function($rootScope, $scope, $location, $routeParams, Users, Auth) {
	$scope.isNew = $routeParams.userNo === "new" ? true : false;
	if ($scope.isNew) {
		$scope.user = {accessLevel: "READER"};
	}
	else {
		$scope.getPromise = Users.getUser($routeParams.userNo); 
		$scope.getPromise.then(function successCallback(result) {
			//result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.user = result.data.user;
		});
	}
	$scope.newUser = function() {
		$location.path("/users/new");
	}
	$scope.saveUser = function() {
		if ($scope.isNew) {
			if($scope.user.password !== $scope.repeatPassword) {
				$rootScope.$emit('ErrorMsg', "Du må angi samme passord i begge passord feltene");
				return;
			} else {
				$scope.savePromise = Users.addUser($scope.user);			
			}
		} else {
			$scope.savePromise = Users.updateUser($scope.user);			
		}
		$scope.savePromise.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Bruker ble lagret ok");
			if ($scope.isNew)
				$location.path("/users/" + result.data.no);
		});
		
	}
	$scope.deleteUser = function() {
//		$location.path("/users/new");
	}
	$scope.getAccessLevelText = function(level) {
		return Users.getAccessLevelText(level);
	} 
})
;
