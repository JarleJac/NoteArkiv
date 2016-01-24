/**
 * 
 */
angular.module('notearkiv').controller('searchController', function($scope, $http, SearchSheets) {
	var controller = this;
	$scope.tab = "current";
	$scope.setTab = function(tab) {
		$scope.tab = tab;
	};
	$scope.isTabSet = function(tab) {
		return $scope.tab === tab;
	};
	$scope.hasResult = function() {
		return SearchSheets.hasResult();
	}
	$scope.getSheetList = function() {
		return SearchSheets.getSheetList();
	}
	$scope.searchSimple = function() {
		SearchSheets.searchSimple($scope.title).then(function successCallback(result) {
			var r = result;
		})
	};
})
;
