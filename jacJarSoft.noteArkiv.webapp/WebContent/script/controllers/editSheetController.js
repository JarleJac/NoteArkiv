/**
 * 
 */

angular.module('notearkiv').controller('editSheetController', function($http) {
	var controller = this;
	this.saveSheet = function(sheetData) {
		var s = sheetData;
		$http({method: 'POST', url : 'rest/noteservice/note', data: sheetData })
		.then(function successCallback(result) {
			result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			controller.sheetData = result.data;
		}, function errorCallback(result) {
			var r = result;
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
