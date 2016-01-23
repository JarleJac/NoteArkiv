/**
 * 
 */
angular.module('notearkiv').controller('logonController', function($scope, $http) {
	var controller = this;
	$scope.doLogon = function() {
		$scope.$parent.logon($scope.info)
	}
})
;
