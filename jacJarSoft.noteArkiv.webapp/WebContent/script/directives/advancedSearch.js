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
		scope.getPromise = SearchSheets.searchAdvanced(scope.searchParam);
		scope.getPromise.then(function successCallback(result) {
			scope.searchListData.sheetList = result.sheetList;
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
    	searchParam: '=searchParam'
      },
    templateUrl: 'templates/directives/advanced-search.html'
  };
}]);