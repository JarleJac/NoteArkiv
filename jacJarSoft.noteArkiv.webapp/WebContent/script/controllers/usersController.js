/**
 * 
 */

angular.module('notearkiv').controller('usersController', function($scope, $location,  Users) {
	$scope.users = [];
	$scope.predicate = "user.name";
	$scope.reverse = false;
	
	$scope.setSort = function(sortOn) {
		$scope.reverse = ($scope.predicate === sortOn) ? !$scope.reverse : false;		
		$scope.predicate = sortOn;
	}
	$scope.getPromise = Users.getUsers();
	$scope.getPromise.then(function successCallback(result) {
		$scope.users = result;
	});
	
	$scope.newUser = function() {
		$location.path("/users/new");
	}
})
;
