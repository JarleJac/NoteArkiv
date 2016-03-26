/**
 * 
 */
angular.module('notearkiv').controller('searchController', function($scope, $http, $routeParams, SearchSheets, Sheets, Lists, State, AuthToken) {
	var controller = this;
	$scope.tab = $routeParams.tab;
	$scope.listId = $routeParams.listId;
	$scope.getStateName = function() { return "searchController" + $scope.tab;}
	State.getOrRegisterScopeVariables(
			["predicate", "reverse", "sheetList", "title", "days"],
			["sheet.title", false, undefined, "", "7"]);

	$scope.setSort = function(sortOn) {
		$scope.reverse = ($scope.predicate === sortOn) ? !$scope.reverse : false;		
		$scope.predicate = sortOn;
	}
	$scope.authInfo = encodeURIComponent(AuthToken.getAuthToken());
	var getFiles = function(sheetData) {
		if (sheetData.files != null)
			return;
		sheetData.getFilesPromise = Sheets.getFiles(sheetData.sheet.noteId);
		sheetData.getFilesPromise.then(function successCallback(files) {
			sheetData.files = files;
		});
	}

	var getSheet = function(sheetId) {
		for(i=0; i<$scope.sheetList.length; i++) {
			if ($scope.sheetList[i].sheet.noteId == sheetId)
				return $scope.sheetList[i];
		}
		return null;
	}
	//TODO Revise this popover activation
	$(function () {
		  $('[data-toggle="popover"]').popover()
		})
	$("#searchResult").on('show.bs.collapse', function (e) {
		var sheetId = $("#"+e.target.id).data("sheet-id");
		var sheetData = getSheet(sheetId);
		sheetData.expanded = true;
		getFiles(sheetData);
		$scope.$apply();
	});	
	$("#searchResult").on('hidden.bs.collapse', function (e) {
		var sheetId = $("#"+e.target.id).data("sheet-id");
		var sheetData = getSheet(sheetId);
		sheetData.expanded = false;
		$scope.$apply();
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
	$scope.searchNewest = function() {
		$scope.getPromise = SearchSheets.searchNewest($scope.days);
		$scope.getPromise.then(function successCallback(result) {
			$scope.sheetList = result.sheetList;
		})
	};
	$scope.toggleCurrent = function(sheetData) {
		if (sheetData.inCurrent) {
			Lists.disconnectFromList(1, sheetData.sheet.noteId)
			.then(function successCallback(result) {
				sheetData.inCurrent = false;
			})
		} else {
			Lists.connectToList(1, sheetData.sheet.noteId)
			.then(function successCallback(result) {
				sheetData.inCurrent = true;
			})
		}
	};
	
	if (!State.backFwdUsed) {
		switch($scope.tab) {
		case "newest":
			$scope.searchNewest();
			break;
		}
	}
})
;
