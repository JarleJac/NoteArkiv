/**
 * 
 */

angular.module('notearkiv').controller('editSheetController', 
		function($rootScope, $scope, $q, $http,  $routeParams, $location, 
				Sheets, Lists, Voices, Tags, AuthToken, FileUploader, SimpleDlg) {
	$scope.sheetPromise = null;
	$scope.connectedTags = [];
	$scope.availableTags = [];
	$scope.files = [];
	$scope.connected = null;
	$scope.available = null;
	$scope.authInfo = encodeURIComponent(AuthToken.getAuthToken());
	$scope.uploader = new FileUploader({
        url: 'rest/noteservice/note/' + $routeParams.sheetId + '/file',
	    headers : {
	        'Authorization': AuthToken.getAuthToken()
	    }
    });
	$("#fileDrop").on('dragleave', function () {
	    $(this).removeClass('drag-over');
	});
	$scope.uploader.onErrorItem = function(fileItem, response, status, headers) {
		$rootScope.$emit('ErrorMsg', "Opplasting av fil " + fileItem.file.name + " feilet. Feilkode: " + status);
    };
	$scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {
		$rootScope.$emit('OkMessage', "Opplasting av fil " + fileItem.file.name + " gikk bra.");
    };
    
	$scope.uploader.onCompleteAll = function() {
        getFiles($scope.sheet.noteId);
        $scope.uploader.clearQueue();
    };
    $scope.uploader.onAfterAddingFile = function(fileItem) {
    	fileItem.myData = {description: "", splitName: getSplitFileName(fileItem.file.name)};
    };
    $scope.uploader.onBeforeUploadItem = function(fileItem) {
    	fileItem.formData.push({description: fileItem.myData.description});
    	fileItem.formData.push({name: fileItem.myData.splitName.name + "." + fileItem.myData.splitName.ext});
    	fileItem.formData.push({sizeStr: fileItem.file.size.toString()});
    };

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
	var getSplitFileName = function(fName) {
		var ext = fName.slice((Math.max(0, fName.lastIndexOf(".")) || Infinity) + 1).toLowerCase();
		var name = fName.substr(0,fName.length - (ext.length+1));
		return {name: name, ext : ext};
	}
	var getFiles = function(sheetId) {
		$scope.getFilesPromise = Sheets.getFiles(sheetId);
		$scope.getFilesPromise.then(function successCallback(files) {
			$scope.files = files.map(function(file) {
				file.isEditMode = false;
				file.splitName = getSplitFileName(file.name);
				return file;
			});
		});
	}
	$scope.isNew = $routeParams.sheetId === "new" ? true : false;
	if ($scope.isNew) {
		$scope.sheet = undefined;
		getVoices(null);
		getTags(null);
	}
	else {
		$scope.sheetMsg = "Henter note";
		$scope.sheetPromise = $http({method: 'GET', url : 'rest/noteservice/note/' + $routeParams.sheetId });
		$scope.sheetPromise.then(function successCallback(result) {
			result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.sheet = result.data.sheet;
			$scope.inCurrent = result.data.inCurrent
			getVoices($scope.sheet.voices);
			getTags($scope.sheet.tags);
			getFiles($scope.sheet.noteId);
			$scope.sheetPromise = null;
		});
	}
	$scope.saveSheet = function() {
		var sheet = $.extend({} , {title: ""}, $scope.sheet);
		sheet.voices = Voices.getSelectedAsIdString($scope.selectedVoices);
		sheet.tags = $scope.connectedTags.map(function(tag) {return tag.id}).join();
		var sheetData = {sheet: sheet};
		var httpMethod = $scope.isNew ? 'POST' : 'PUT';
		$scope.sheetMsg = "Lagrer note";
		$scope.sheetPromise = $http({method: httpMethod, url : 'rest/noteservice/note', data: sheetData });
		$scope.sheetPromise.then(function successCallback(result) {
			if ($scope.isNew)
				$location.path("/sheets/" + result.data.sheet.noteId).replace();
			$rootScope.$emit('OkMessage', "Note ble lagret ok.");
			$scope.sheetPromise = null;
		});
	}
	$scope.deleteSheet = function() {
		SimpleDlg.runSimpleConfirmDlg("Bekreft sletting", "Er du sikker på at du vil slette " + $scope.sheet.title)
		.result.then(function (result) {
			$scope.deletePromise = Sheets.deleteSheet($scope.sheet);
			$scope.deletePromise.then(function successCallback(result) {
				$location.path("/sheets/new").replace();
				$rootScope.$emit('OkMessage', "Note ble slettet.");
			});
		}); 
	}
	$scope.uploadFiles = function() {
		$scope.uploader.uploadAll();
	}
	$scope.selectFiles = function() {
		$("#fileSelect").click();
	}
	$scope.deleteFile = function(fileId, fileName) {
		SimpleDlg.runSimpleConfirmDlg("Bekreft sletting", "Er du sikker på at du vil slette " + fileName)
			.result.then(function (result) {
				Sheets.deleteFile(fileId).then(function successCallback(result) {
			    	$rootScope.$emit('OkMessage', "Fil " + fileName + " ble slettet.");
					getFiles($scope.sheet.noteId);
				});
				
			}); 
	}
	var markEditFile = function(fileId) {
		$scope.files.forEach(function(file) {
			if (fileId >= 0 && file.fileId === fileId)
				file.isEditMode = true;
			else
				file.isEditMode = false;
		});	
	}
	$scope.editFile = function(fileId) {
		markEditFile(fileId);
	}
	$scope.saveFile = function(fileId) {
		for(ix=0; ix < $scope.files.length; ix++) {
			file = $scope.files[ix];
			if (file.fileId === fileId) {
				file.name = file.splitName.name + "." + file.splitName.ext
				file.isEditMode = false;
				Sheets.saveFile(file).then(function successCallback(result) {
			    	$rootScope.$emit('OkMessage', "Fil " + file.name + " ble lagret.");
					getFiles($scope.sheet.noteId);
				});
				break;
			}
		};	
	}
	$scope.cancelEditFile = function() {
		markEditFile(-1);
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
		SimpleDlg.runSimpleConfirmDlg("Bekreft sletting", "Er du sikker på at du vil slette sjanger " + $scope.available.name)
		.result.then(function (result) {
			$scope.deleteTagPromise = Tags.deleteTag($scope.available);
			$scope.deleteTagPromise.then(function successCallback(result) {
		    	$rootScope.$emit('OkMessage', "Sjanger " + $scope.available.name + " ble slettet.");
				var i = getTagIndex($scope.availableTags, $scope.available.id);
				deleteFromTagList(i, $scope.availableTags, "available");
			});
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
	$scope.newFile = function() {
	}
	$scope.openFile = function(file) {
		Sheets.openFile(file, $scope.sheet, $rootScope.appSettings);
	}
	$scope.toggleCurrent = function() {
		if ($scope.inCurrent) {
			Lists.disconnectFromList(1, $scope.sheet.noteId)
			.then(function successCallback(result) {
				$scope.inCurrent = false;
			})
		} else {
			Lists.connectToList(1, $scope.sheet.noteId)
			.then(function successCallback(result) {
				$scope.inCurrent = true;
			})
		}
	};
	
})
;
