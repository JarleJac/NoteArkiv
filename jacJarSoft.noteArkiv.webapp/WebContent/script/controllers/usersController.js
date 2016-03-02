/**
 * 
 */

angular.module('notearkiv').controller('usersController', function($scope, $location,  Users) {
	$scope.users = [];
	$scope.getPromise = Users.getUsers();
	$scope.getPromise.then(function successCallback(result) {
		$scope.users = result.data;
	});
	
	$scope.newUser = function() {
		$location.path("/users/new");
	}
})
;
