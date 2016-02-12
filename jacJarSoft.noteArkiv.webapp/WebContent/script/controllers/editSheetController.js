/**
 * 
 */

angular.module('notearkiv').controller('editSheetController', function($rootScope, $scope, $q, $http,  $routeParams, $location, Voices, Tags) {
	$scope.connectedTags = [];
	$scope.availableTags = [];
	$scope.connected = null;
	$scope.available = null;
	
	var getVoices = function(selectedVoices) {
		$scope.getVoicesPromise = Voices.getSelectedVoices(selectedVoices);
		$scope.getVoicesPromise.then(function successCallback(voiceResult) {
			$scope.selectedVoices = voiceResult;
		});
	};
	var getTagIndex = function(tagArray, id) {
		for(var i = 0; i < tagArray.length; i++) {
	        if (tagArray[i].id == id) 
	        	return i;
	    }
		return -1;
	}
	var getTags = function(selectedTags) {
		var promises = [];
		promises.push(Tags.getAvailableTags(selectedTags, true));
		promises.push(Tags.getAvailableTags(selectedTags, false));
		$scope.getTagsPromise= $q.all(promises);
		$scope.getTagsPromise.then(function successCallback(resultArr) {
			$scope.connectedTags = resultArr[0];
			if ($scope.connectedTags.length > 0) {
				$scope.connected = $scope.connectedTags[0];
			}
			$scope.availableTags = resultArr[1];
			if ($scope.availableTags.length > 0) {
				$scope.available = $scope.availableTags[0];
			}
		});
	};
	$scope.isNew = $routeParams.sheetId === "new" ? true : false;
	if ($scope.isNew) {
		$scope.sheet = undefined;
		getVoices(null);
		getTags(null);
	}
	else {
		$scope.getPromise = $http({method: 'GET', url : 'rest/noteservice/note/' + $routeParams.sheetId });
		$scope.getPromise.then(function successCallback(result) {
			result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.sheet = result.data.sheet;
			getVoices($scope.sheet.voices);
			getTags($scope.sheet.tags);
		});
	}
	$scope.saveSheet = function() {
		var sheet = $.extend({} , {title: ""}, $scope.sheet);
		sheet.voices = Voices.getSelectedAsIdString($scope.selectedVoices);
		sheet.tags = $scope.connectedTags.map(function(tag) {return tag.id}).join();
		var sheetData = {sheet: sheet};
		var httpMethod = $scope.isNew ? 'POST' : 'PUT';
		$scope.savePromise = $http({method: httpMethod, url : 'rest/noteservice/note', data: sheetData });
		$scope.savePromise.then(function successCallback(result) {
			if ($scope.isNew)
				$location.path("/sheets/" + result.data.sheet.noteId).replace();
			else
		    	$rootScope.$emit('OkMessage', "Note ble lagret ok.");
		});
	}
	$scope.addTag = function() {
		if (!$scope.newTag)
			return;
		var tag = {name: $scope.newTag};
		$scope.addTagPromise = Tags.addTag(tag);
		$scope.addTagPromise.then(function successCallback(result) {
			$scope.connectedTags.push(result.data);
			$scope.connected = result.data;
	    	$rootScope.$emit('OkMessage', "Sjanger " + $scope.newTag + " ble lagt til.");
			$scope.newTag = "";
		});
	}
	var deleteFromTagList = function(i, fromList, scopeFrom) {
		fromList.splice(i,1);
		if (fromList.length <= i)
			i = fromList.length - 1;
		if (i != -1)
			$scope[scopeFrom] = fromList[i];
	}
	$scope.deleteTag = function() {
		if (!$scope.available)
			return;
		$scope.deleteTagPromise = Tags.deleteTag($scope.available);
		$scope.deleteTagPromise.then(function successCallback(result) {
	    	$rootScope.$emit('OkMessage', "Sjanger " + $scope.available.name + " ble slettet.");
			var i = getTagIndex($scope.availableTags, $scope.available.id);
			deleteFromTagList(i, $scope.availableTags, "available");
		});
	}
	var switchList = function(fromList, toList, scopeFrom, scopeTo) {
		var i = getTagIndex(fromList, $scope[scopeFrom].id);
		if (i == -1)
			return;
		toList.push($scope[scopeFrom]);
		toList.sort(function (a, b) {
			  if (a.name > b.name) {
			    return 1;
			  }
			  if (a.name < b.name) {
			    return -1;
			  }
			  return 0;
			});
		$scope[scopeTo] = $scope[scopeFrom]
		deleteFromTagList(i, fromList, scopeFrom);
	} 
	$scope.connect = function() {
		switchList($scope.availableTags, $scope.connectedTags, "available", "connected")
	}
	$scope.disconnect = function() {
		switchList($scope.connectedTags, $scope.availableTags, "connected", "available")
	}
	$scope.newSheet = function() {
		$scope.sheet = undefined;
		$location.path("/sheets/new");
	}
})
;
