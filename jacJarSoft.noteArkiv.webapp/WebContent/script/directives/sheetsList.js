/**
 * 
 */

angular.module('notearkiv').directive('sheetsList', ['Sheets', '$timeout', 'Auth', 'Lists', 'AuthToken', function(Sheets, $timeout, Auth, Lists, AuthToken) {

  function link(scope, element, attrs) {
	scope.authInfo = encodeURIComponent(AuthToken.getAuthToken());
	  
	scope.hasResult = function() {
		return scope.searchListData.sheetList !== undefined;
	}
	scope.setSort = function(sortOn) {
		scope.searchListData.reverse = (scope.searchListData.predicate === sortOn) ? !scope.searchListData.reverse : false;		
		scope.searchListData.predicate = sortOn;
	}
	scope.hasAccess = function(strLevel) {
		return Auth.isUserAuthStr(strLevel);
	}
	scope.toggleCurrent = function(sheetData) {
		if (sheetData.inCurrent) {
			Lists.disconnectFromList(1, sheetData.sheet.noteId)
			.then(function successCallback(result) {
				sheetData.inCurrent = false;
			})
		} else {
			Lists.connectToList(1, sheetData.sheet.noteId)
			.then(function successCallback(result) {
				sheetData.inCurrent = true;
			})
		}
	};

	var getFiles = function(sheetData) {
		if (sheetData.files != null)
			return;
		sheetData.getFilesPromise = Sheets.getFiles(sheetData.sheet.noteId);
		sheetData.getFilesPromise.then(function successCallback(files) {
			sheetData.files = files;
		});
	}
	var getSheet = function(sheetId) {
		for(i=0; i<scope.searchListData.sheetList.length; i++) {
			if (scope.searchListData.sheetList[i].sheet.noteId == sheetId)
				return scope.searchListData.sheetList[i];
		}
		return null;
	}
	$timeout(function() { //Run this after the html has been processed by agular to get the correct element
		$("#" + scope.resultId).on('show.bs.collapse', function (e) {
			var sheetId = $("#"+e.target.id).data("sheet-id");
			var sheetData = getSheet(sheetId);
			sheetData.expanded = true;
			getFiles(sheetData);
			scope.$apply();
		});	
		$("#" + scope.resultId).on('hidden.bs.collapse', function (e) {
			var sheetId = $("#"+e.target.id).data("sheet-id");
			var sheetData = getSheet(sheetId);
			sheetData.expanded = false;
			scope.$apply();
		});	
     });
  }

  return {
	restrict: 'E',
    link: link,
    scope: {
    	searchListData: '=data',
    	resultId: '=resultid'
      },
    templateUrl: 'templates/directives/sheets-list.html'
  };
}]);