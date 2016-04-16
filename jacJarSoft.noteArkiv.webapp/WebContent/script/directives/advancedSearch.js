/**
 * 
 */

angular.module('notearkiv').directive('advancedSearch', ['Sheets', '$timeout', 'Auth', 'Lists', 'SearchSheets', function(Sheets, $timeout, Auth, Lists, SearchSheets) {

  function link(scope, element, attrs) {
	scope.search = function() {
		scope.getPromise = SearchSheets.searchAdvanced(scope.searchParam);
		scope.getPromise.then(function successCallback(result) {
			scope.searchListData.sheetList = result.sheetList;
		})
	}
//	$timeout(function() { //Run this after the html has been processed by agular to get the correct element
//		$("#" + scope.resultId).on('show.bs.collapse', function (e) {
//			var sheetId = $("#"+e.target.id).data("sheet-id");
//			var sheetData = getSheet(sheetId);
//			sheetData.expanded = true;
//			getFiles(sheetData);
//			scope.$apply();
//		});	
//		$("#" + scope.resultId).on('hidden.bs.collapse', function (e) {
//			var sheetId = $("#"+e.target.id).data("sheet-id");
//			var sheetData = getSheet(sheetId);
//			sheetData.expanded = false;
//			scope.$apply();
//		});	
//     });
  }

  return {
	restrict: 'E',
    link: link,
    scope: {
    	searchListData: '=data',
    	searchParam: '=searchParam'
      },
    templateUrl: 'templates/directives/advanced-search.html'
  };
}]);