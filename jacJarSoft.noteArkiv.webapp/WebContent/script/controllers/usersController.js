/**
 * 
 */

angular.module('notearkiv').controller('usersController', function($scope, $http,  $routeParams, Users) {
	$scope.users = [];
	$scope.getPromise = Users.getUsers();
	$scope.getPromise.then(function successCallback(result) {
		$scope.users = result.data;
	});
	
})
;
