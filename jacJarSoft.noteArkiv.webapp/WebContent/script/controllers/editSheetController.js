/**
 * 
 */

angular.module('notearkiv').controller('editSheetController', function($scope, $http) {
	var controller = this;
	$scope.saveSheet = function() {
		var sheet = $.extend({} , {title: ""}, $scope.sheet)
		var sheetData = {sheet: sheet};
		$http({method: 'POST', url : 'rest/noteservice/note', data: sheetData })
		.then(function successCallback(result) {
			result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.sheet = result.data.sheet;
		});
//		.success(function(returnData) {
//			returnData.sheet.registeredDate = new Date(returnData.sheet.registeredDate);
//			controller.sheetData = returnData;
//		})
//		.error(function(result) {
//			var r = result;
//		})
//		.catch(function(result) {
//			var r = result;
//		})
	}
})
;
