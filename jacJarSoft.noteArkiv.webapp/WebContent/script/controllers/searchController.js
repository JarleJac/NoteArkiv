/**
 * 
 */
angular.module('notearkiv').controller('searchController', function($scope, $http, $routeParams, SearchSheets, State) {
	var controller = this;
	$scope.tab = $routeParams.tab;
	$scope.getStateName = function() { return "searchController" + $scope.tab;}
	State.getOrRegisterScopeVariables(
			["predicate", "reverse", "sheetList", "title"],
			["sheet.title", false, undefined, ""]);

	$scope.setSort = function(sortOn) {
		$scope.reverse = ($scope.predicate === sortOn) ? !$scope.reverse : false;		
		$scope.predicate = sortOn;
	}

	//TODO Revise this popover activation
	$(function () {
		  $('[data-toggle="popover"]').popover()
		})
	$("#searchResult").on('shown.bs.collapse', function (e) {
		var ix = $("#"+e.target.id).data("ix");
		$scope.sheetList[ix].expanded = true;
	});	
	$("#searchResult").on('hidden.bs.collapse', function (e) {
		var ix = $("#"+e.target.id).data("ix");
		$scope.sheetList[ix].expanded = false;
	});	
	$scope.setTab = function(tab) {
		$scope.tab = tab;
	};
	$scope.isTabSet = function(tab) {
		return $scope.tab === tab;
	};
	$scope.hasResult = function() {
		return $scope.sheetList !== undefined;
	}
	$scope.searchSimple = function() {
		$scope.getPromise = SearchSheets.searchSimple($scope.title);
		$scope.getPromise.then(function successCallback(result) {
			$scope.sheetList = result.sheetList;
		})
	};
})
;
