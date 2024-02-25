/**
 * 
 */
angular.module('notearkiv').controller('logonController', function($scope, Auth, $http) {
	var controller = this;
	$scope.doLogon = function() {
		$scope.$parent.logon($scope.info)
	}
	$scope.doForgotPw = function() {
		$scope.promiseMsg = "Sender e-post";
		$scope.pagePromise = Auth.forgotPw($scope.userOrEmail);			
		$scope.pagePromise.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "E-post er sendt til ");
			$scope.pagePromise = null;
//			if ($scope.isNew)
//				$location.path("/search/list/" + result.listId);
		});
	}
})
;
