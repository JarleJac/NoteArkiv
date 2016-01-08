/**
 * 
 */
angular.module('notearkiv').controller('logonController', function($scope, $http) {
	var controller = this;
	$scope.doLogon = function() {
		$scope.$parent.logon($scope.info)
//		var y = $scope;
//		var x = "";
//		$http({method: 'POST', url : 'rest/appservice/logon', data: $scope.info })
//		.then(function successCallback(result) {
//			var x = result.data;
//			currentUserName = result.data.user.name;
//		}, function errorCallback(result) {
//			var r = result;
//		})
//		;
	}
})
;
