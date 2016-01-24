/**
 * 
 */
angular.module('notearkiv').factory('SearchSheets', function SearchSheetsFactory($http) {
	var lastSearchResult;

	internalSearch = function(searchParam) {
		return $http({method: 'POST', url : 'rest/noteservice/note/search', data: searchParam })
		.then(function successCallback(result) {
			for (var ix = 0; ix < result.data.sheetList.length; ix++) {
				result.data.sheetList[ix].sheet.registeredDate = new Date(result.data.sheetList[ix].sheet.registeredDate)
			}
			lastSearchResult = result.data;
			return lastSearchResult;
		}, function errorCallback(result) {
			throw result;
		});
	}
	return {
		searchSimple : function(searchParam) {
			var simpleParam = {title: searchParam}
			return internalSearch(simpleParam);
			
		},
		hasResult : function() {
			return lastSearchResult !== undefined;
		}, 
		getSheetList : function() {
			if (this.hasResult()) {
				return lastSearchResult.sheetList;
			}
			return null;
		}
	}
}
)	

;
