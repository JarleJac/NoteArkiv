/**
 * 
 */
angular.module('notearkiv').controller('changepwController', function($scope, $http, $routeParams) {
	var controller = this;
	$scope.changePwMsg = null;
	$scope.errorMsg = null;
	$scope.info = {"user": $scope.logonResult.user.no};
	if($routeParams.mustChange) {
		$scope.changePwMsg="En administrator har satt ditt passord. Du må bytte passord.";
	}
	$scope.doChangePassword = function() {
		if ($scope.info.newpassword != $scope.info.repeatpassword) {
			$scope.errorMsg = "Du må skrive inn samme passord i Nytt og Gjenta!";
			return;
		}
		$scope.errorMsg = null;
		
		$http({method: "PUT", url : "rest/userservice/user/"+$scope.info.user+"/changepw", data: $scope.info })
		.then(function successCallback(result) {
			$scope.$parent.changePwOk();
		});

	}
})
;
