/**
 * 
 */

angular.module('notearkiv').controller('editSheetController', function($scope, $http,  $routeParams, $location, Voices) {
	var getVoices = function(selectedVoices) {
		Voices.getSelectedVoices(selectedVoices).then(function successCallback(voiceResult) {
			$scope.selectedVoices = voiceResult;
		});
	};
	$scope.isNew = $routeParams.sheetId === "new" ? true : false;
	if ($scope.isNew) {
		$scope.sheet = undefined;
		getVoices(null);
	}
	else {
		$scope.getPromise = $http({method: 'GET', url : 'rest/noteservice/note/' + $routeParams.sheetId });
		$scope.getPromise.then(function successCallback(result) {
			result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.sheet = result.data.sheet;
			getVoices($scope.sheet.voices);
		});
	}
	$scope.saveSheet = function() {
		var sheet = $.extend({} , {title: ""}, $scope.sheet);
		sheet.voices = Voices.getSelectedAsIdString($scope.selectedVoices);
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
