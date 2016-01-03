/**
 * 
 */

angular.module('notearkiv').controller('editSheetController', function($http) {
	var controller = this;
	this.saveSheet = function(sheetData) {
		var s = sheetData;
		$http({method: 'POST', url : 'rest/noteservice/note', data: sheetData })
		.success(function(returnData) {
			returnData.sheet.registeredDate = new Date(returnData.sheet.registeredDate);
			controller.sheetData = returnData;
		})
		.catch(function(result) {
			var r = result;
		})
	}
})
;
