/**
 * 
 */
angular.module('notearkiv').controller('listSearchController', function($scope, $http, $routeParams, SearchSheets, State, Lists) {
	$scope.listId = $routeParams.listId;
	$scope.getStateName = function() { return "listSearchController" + $scope.listId;}
	State.getOrRegisterScopeVariables(
			["searchListData", "currentListData", "editMode", "tab" ],
			[{predicate: 'sheet.title', reverse: false, sheetList: undefined},
			 {predicate: 'sheet.title', reverse: false, sheetList: undefined},
			 false, "current"]);

	$scope.getPromise = Lists.getList($routeParams.listId); 
	$scope.getPromise.then(function successCallback(result) {
		$scope.list = result;
	});
	$scope.setTab = function(tab) {
		$scope.tab = tab;
	};
	$scope.isTabSet = function(tab) {
		return $scope.tab === tab;
	};

	$scope.toggleEditMode = function() {
		$scope.editMode = !$scope.editMode;
	};
	$scope.searchCurrent = function() {
		$scope.getListPromise = SearchSheets.searchList($scope.listId);
		$scope.getListPromise.then(function successCallback(result) {
			$scope.currentListData.sheetList = result.sheetList;
		})
	};
	
	if (!State.backFwdUsed) {
		$scope.searchCurrent();
	}
})
;
