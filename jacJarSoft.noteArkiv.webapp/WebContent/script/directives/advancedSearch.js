/**
 * 
 */

angular.module('notearkiv').directive('advancedSearch', 
		['Sheets', '$timeout', 'Auth', 'Lists', 'SearchSheets', 'Voices', 'Tags', 
            function(Sheets, $timeout, Auth, Lists, SearchSheets, Voices, Tags) {

  function link(scope, element, attrs) {
	  if (scope.searchParam === undefined)
		  scope.searchParam = {};
	  scope.selectedVoices = [];
	  scope.selectedTags = [];
	  
	  scope.search = function() {
		scope.searchParam.voices = Voices.getSelectedAsIdString(scope.selectedVoices);
		scope.searchParam.tags = scope.selectedTags.map(function(tag) {return tag.id}).join();
		scope.prom.getPromise = SearchSheets.searchAdvanced(scope.searchParam);
		scope.prom.getPromise.then(function successCallback(result) {
			if (scope.filterData !== undefined) {
				scope.searchListData.sheetList = result.sheetList.filter(function(sheetData) {
					for (i = 0; i < scope.filterData.sheetList.length; i++) { 
					    if (scope.filterData.sheetList[i].sheet.noteId === sheetData.sheet.noteId)
					    	return false;
					}
					return true;
				});				
			} else {
				scope.searchListData.sheetList = result.sheetList;
			}
		})
	  }
	  scope.clear = function() {
		  scope.selectedVoices.forEach(function(voice) {
			  voice.selected = false;
		  });
		  scope.selectedTags = [];
		  scope.searchParam.title = "";
		  scope.searchParam.composedBy = "";
		  scope.searchParam.arrangedBy = "";
		  scope.searchListData.sheetList = {};
	  }
	  scope.$on("currentChanged", function (event, args) {
		  if (!angular.equals({}, scope.searchListData.sheetList))
			  scope.search();
 	  });
	  scope.getVoicesPromise = Voices.getSelectedVoices(null)
	  scope.getVoicesPromise.then(function successCallback(voiceResult) {
		scope.selectedVoices = voiceResult;
	  });
	  scope.getTagsPromise = Tags.getAvailableTags(null, false);
	  scope.getTagsPromise.then(function successCallback(tagsResult) {
		  scope.availableTags = tagsResult;
	  });
	
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
    	searchParam: '=searchParam',
    	filterData: '=filterdata',
    	prom: '=prom'
      },
    templateUrl: 'templates/directives/advanced-search.html'
  };
}]);