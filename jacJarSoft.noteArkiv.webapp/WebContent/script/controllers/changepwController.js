/**
 * 
 */
angular.module('notearkiv').controller('changepwController', function($rootScope, $scope, $http, $routeParams, Auth) {
	var controller = this;
	$scope.changePwMsg = null;
	$scope.errorMsg = null;
	$scope.info = {"user": Auth.getUser().no};
	var isMustChange=false;
	if($routeParams.mustChange) {
		$scope.changePwMsg="En administrator har satt ditt passord, eller det har blitt resatt. Du må bytte passord.";
		isMustChange = true;
	}
	$scope.doChangePassword = function() {
		if ($scope.info.newpassword != $scope.info.repeatpassword) {
			$scope.errorMsg = "Du må skrive inn samme passord i Nytt og Gjenta!";
			return;
		}
		$scope.errorMsg = null;
		
		Auth.changePw($scope.info )
		.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Passord er endret");
			$scope.$parent.changePwOk(isMustChange);
		});

	}
})
;
