/**
 * 
 */
angular.module('notearkiv').controller('searchController', function($scope, $http) {
	var controller = this;
	$scope.tab = "current";
	$scope.setTab = function(tab) {
		$scope.tab = tab;
	};
	$scope.isTabSet = function(tab) {
		return $scope.tab === tab;
	};
	$scope.searchSimple = function() {
		//TODO
	};
})
;
