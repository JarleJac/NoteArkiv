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
				$scope.user = data.user;
			}
			else
				$scope.errorMsg = result.data.errorMsg;
		});
	}
		

/*	if($routeParams.userNo !== undefined && Auth.getUser().no != $routeParams.userNo) {
		$scope.info = {"user": $routeParams.userNo};
		isOtherUser = true;
		$scope.currentLbl = "Ditt passord";
		$scope.currentPlaceHolder = "Pasord som du logger på med";
	} else {
		$scope.info = {"user": Auth.getUser().no};
	}
*/
	$scope.doChangePassword = function() {
		if ($scope.info.newpassword != $scope.info.repeatpassword) {
			$scope.errorMsg = "Du må skrive inn samme passord i Nytt og Gjenta!";
			return;
		}
		$scope.errorMsg = null;
		
		Auth.changePw($scope.info, isOtherUser)
		.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Passord er endret");
			$scope.$parent.changePwOk(isMustChange);
		});

	}
})
;
