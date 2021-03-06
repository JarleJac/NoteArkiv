/**
 * 
 */
angular.module('notearkiv').factory('SearchSheets', function SearchSheetsFactory($http, Tags, Voices) {
	var lastSearchResult;

	internalSearch = function(searchParam) {
		return $http({method: 'POST', url : 'rest/noteservice/note/search', data: searchParam })
		.then(function successCallback(result) {
			for (var ix = 0; ix < result.data.sheetList.length; ix++) {
				result.data.sheetList[ix].sheet.registeredDate = new Date(result.data.sheetList[ix].sheet.registeredDate);
				Tags.getTagsDescrString(result.data.sheetList[ix].sheet);
				Voices.getVoicesDescrString(result.data.sheetList[ix].sheet);
				result.data.sheetList[ix].expanded = false;
				result.data.sheetList[ix].selected = false;
				result.data.sheetList[ix].files = null;
				if (result.data.sheetList[ix].sheet.arrangedBy === null)
					result.data.sheetList[ix].sheet.arrangedBy = "";
				if (result.data.sheetList[ix].sheet.composedBy === null)
					result.data.sheetList[ix].sheet.composedBy = "";
			}
			lastSearchResult = result.data;
			return lastSearchResult;
		}, function errorCallback(result) {
			throw result;
		});
	}
	return {
		searchAdvanced : function(searchParam) {
			return internalSearch(searchParam);
			
		},
		searchSimple : function(searchParam) {
			var simpleParam = {title: searchParam}
			return internalSearch(simpleParam);
			
		},
		searchNewest : function(searchParam) {
			var newestParam = {days: searchParam}
			return internalSearch(newestParam);
			
		},
		searchList : function(listId) {
			var listParam = {listId: listId}
			return internalSearch(listParam);
			
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
