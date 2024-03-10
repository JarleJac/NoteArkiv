/**
 * 
 */
angular.module('notearkiv').controller('setpwController', function($rootScope, $scope,  $location, Auth) {
	var controller = this;
	$scope.$parent.clearLocalStorageUser();
	$scope.errorMsg = null;
	var searchObject = $location.search();
	var token = searchObject.spts
	if (token === undefined)
		$scope.errorMsg = "Du har benyttet en ugyldig link til denne siden";
	else {
		Auth.checkToken(token)
		.then(function successCallback(result) {
			if (result.data.statusOk)
			{
				$scope.user = result.data.user;
				$scope.info = {token : result.data.token};
			}
			else
				$scope.errorMsg = result.data.errorMsg;
		});
	}
		
	$scope.doSetPassword = function() {
		if ($scope.info.newpassword != $scope.info.repeatpassword) {
			$scope.errorMsg = "Du må skrive inn samme passord i Nytt og Gjenta!";
			return;
		}
		$scope.errorMsg = null;
		
		Auth.setPw($scope.info)
		.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Passord er endret");
			$scope.$parent.setPwOk();
		});

	}
})
;
