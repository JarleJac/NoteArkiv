/**
 * 
 */

angular.module('notearkiv').controller('userController', function($scope, $http,  $routeParams, Users) {
	$scope.isNew = $routeParams.userNo === "new" ? true : false;
	if ($scope.isNew) {
		$scope.user = undefined;
	}
	else {
		$scope.getPromise = $http({method: 'GET', url : 'rest/userservice/user/' + $routeParams.userNo });
		$scope.getPromise.then(function successCallback(result) {
			//result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.user = result.data.user;
		});
	}
	
})
;
