/**
 * 
 */
angular.module('notearkiv').controller('searchController', function($scope, $http, $routeParams, SearchSheets, State) {
	var controller = this;
	$scope.tab = $routeParams.tab;
	$scope.listId = $routeParams.listId;
	$scope.getStateName = function() { return "searchController" + $scope.tab;}
	State.getOrRegisterScopeVariables(
			["searchListData", "title", "days"],
			[{predicate: 'sheet.title', reverse: false, sheetList: undefined}, "", "7"]);

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
	$scope.searchSimple = function() {
		$scope.getPromise = SearchSheets.searchSimple($scope.title);
		$scope.getPromise.then(function successCallback(result) {
			$scope.searchListData.sheetList = result.sheetList;
		})
	};
	$scope.searchNewest = function() {
		$scope.getPromise = SearchSheets.searchNewest($scope.days);
		$scope.getPromise.then(function successCallback(result) {
			$scope.searchListData.sheetList = result.sheetList;
		})
	};
	$scope.searchCurrent = function() {
		$scope.getPromise = SearchSheets.searchList(1);
		$scope.getPromise.then(function successCallback(result) {
			$scope.searchListData.sheetList = result.sheetList;
		})
	};
	
	if (!State.backFwdUsed) {
		switch($scope.tab) {
		case "newest":
			$scope.searchNewest();
			break;
		case "current":
			$scope.searchCurrent();
			break;
		}
	}
})
;
