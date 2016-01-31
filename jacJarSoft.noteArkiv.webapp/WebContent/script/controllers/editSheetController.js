/**
 * 
 */

angular.module('notearkiv').controller('editSheetController', function($scope, $http,  $routeParams, $location) {
	$scope.isNew = $routeParams.sheetId === "new" ? true : false;
	if ($scope.isNew) {
		$scope.sheet = undefined;
	}
	else {
		$scope.getPromise = $http({method: 'GET', url : 'rest/noteservice/note/' + $routeParams.sheetId });
		$scope.getPromise.then(function successCallback(result) {
			result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.sheet = result.data.sheet;
		});
	}
	$scope.saveSheet = function() {
		var sheet = $.extend({} , {title: ""}, $scope.sheet)
		var sheetData = {sheet: sheet};
		var httpMethod = $scope.isNew ? 'POST' : 'PUT';
		$scope.savePromise = $http({method: httpMethod, url : 'rest/noteservice/note', data: sheetData });
		$scope.savePromise.then(function successCallback(result) {
			if ($scope.isNew)
				$location.path("/sheets/" + result.data.sheet.noteId).replace();
		});
	}
	$scope.newSheet = function() {
		$scope.sheet = undefined;
		$location.path("/sheets/new");
	}
})
;
