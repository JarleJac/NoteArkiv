/**
 * 
 */
angular.module('notearkiv').controller('searchController', function($scope, $http, $routeParams, SearchSheets) {
	var controller = this;
	$scope.tab = $routeParams.tab;

	//TODO Revise this popover activation
	$(function () {
		  $('[data-toggle="popover"]').popover()
		})
		
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
		$scope.getPromise = SearchSheets.searchSimple($scope.title);
		$scope.getPromise.then(function successCallback(result) {
//			var r = result;
		})
	};
})
;
