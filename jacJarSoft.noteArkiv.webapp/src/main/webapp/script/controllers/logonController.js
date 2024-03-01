/**
 * 
 */
angular.module('notearkiv').controller('logonController', function($rootScope, $scope, Auth) {
	$scope.forgotPwMailSendt = false;
	$scope.pagePromise = null;
	$scope.doLogon = function() {
		$scope.$parent.logon($scope.info)
	}
	$scope.doForgotPw = function() {
		$scope.promiseMsg = "Sender e-post";
		$scope.pagePromise = Auth.forgotPw($scope.userOrEmail);			
		$scope.pagePromise.then(function successCallback(result) {
			$scope.pagePromise = null;
			$scope.user=result.data.user;
			$scope.forgotPwMailSendt = true;
//			if ($scope.isNew)
//				$location.path("/search/list/" + result.listId);
		});
	}
})
;
