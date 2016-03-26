/**
 * 
 */

angular.module('notearkiv').controller('listController', function($rootScope, $scope, $location, $routeParams, Lists, Auth) {
	$scope.isNew = $routeParams.listId === "new" ? true : false;
	if ($scope.isNew) {
		$scope.list = {};
	}
	else {
		$scope.getPromise = Lists.getList($routeParams.listId); 
		$scope.getPromise.then(function successCallback(result) {
			$scope.list = result;
		});
	}
	$scope.newList = function() {
		$location.path("/listss/new");
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
