/**
 * 
 */
angular.module('notearkiv').controller('changepwController', function($scope, $http, $routeParams) {
	var controller = this;
	$scope.changePwMsg = null;
	$scope.info = {"user": $scope.logonResult.user.no};
	if($routeParams.mustChange) {
		$scope.changePwMsg="En administrator har satt ditt passord. Du må bytte passord.";
	}
	$scope.doChangePassword = function() {
		//TODO do the password change
		$scope.$parent.changePwOk();
	}
})
;
