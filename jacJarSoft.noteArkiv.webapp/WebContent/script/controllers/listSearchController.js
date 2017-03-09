/**
 * 
 */
angular.module('notearkiv').controller('listSearchController', function($rootScope, $scope, $q, $http, $routeParams, SearchSheets, State, Lists) {
	$scope.listId = $routeParams.listId;
	$scope.getStateName = function() { return "listSearchController" + $scope.listId;}
	$scope.prom = {};
	State.getOrRegisterScopeVariables(
			["searchListData", "currentListData", "editMode", "tab" ],
			[{predicate: 'sheet.title', reverse: false, sheetList: undefined},
			 {predicate: 'sheet.title', reverse: false, sheetList: undefined},
			 false, "current"]);
	$scope.brodast = false;
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

	$scope.addSelected = function() {
		var deferred = $q.defer();
		var promises = [];
		$scope.searchListData.sheetList.forEach(function(sheetData) {
			if (sheetData.selected) {
				promises.push(Lists.connectToList($scope.listId, sheetData.sheet.noteId))				
			}
		  });
		$scope.updatePromise = $q.all(promises); 
		$scope.updatePromise.then(function successCallback(result) {
			$scope.brodast = true;
			$scope.searchCurrent();
			$rootScope.$emit('OkMessage', "Valgte noter er lagt til i lista.");
		});
	};
	$scope.deleteSelected = function() {
		var deferred = $q.defer();
		var promises = [];
		$scope.currentListData.sheetList.forEach(function(sheetData) {
			if (sheetData.selected) {
				promises.push(Lists.disconnectFromList($scope.listId, sheetData.sheet.noteId))				
			}
		  });
		$scope.updatePromise = $q.all(promises); 
		$scope.updatePromise.then(function successCallback(result) {
			$scope.brodast = true;
			$scope.searchCurrent();
			$rootScope.$emit('OkMessage', "Valgte noter er fjernet fra lista.");
		});
	};
	$scope.toggleEditMode = function() {
		$scope.editMode = !$scope.editMode;
		$scope.searchListData.sheetList = {};
	};
	$scope.searchCurrent = function() {
		$scope.getListPromise = SearchSheets.searchList($scope.listId);
		$scope.getListPromise.then(function successCallback(result) {
			$scope.currentListData.sheetList = result.sheetList;
			if ($scope.brodast) {
				$scope.$broadcast("currentChanged", $scope.currentListData);
				$scope.brodast = false;
			}
		})
	};
	
	if (!State.backFwdUsed) {
		$scope.searchCurrent();
	}
})
;
