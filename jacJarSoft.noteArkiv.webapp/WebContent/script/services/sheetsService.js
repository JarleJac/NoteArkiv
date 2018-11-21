/**
 * 
 */
angular.module('notearkiv').factory('Sheets', function SheetsFactory($http, $window, AuthToken) {
	return {
		getFiles : function(sheetId) {
			return $http({method: 'GET', url : 'rest/noteservice/note/' + sheetId +'/file' })
			.then(function successCallback(result) {
				for (var ix = 0; ix < result.data.length; ix++) {
					result.data[ix].registeredDate = new Date(result.data[ix].registeredDate);
				}
				return result.data;
			}, function errorCallback(result) {
				throw result;
			});
		},
		deleteSheet : function(sheet) {
			return $http({method: 'DELETE', url : 'rest/noteservice/note/' + sheet.noteId  });
		},
		deleteFile : function(fileId) {
			return $http({method: 'DELETE', url : 'rest/noteservice/notefile/' + fileId  });
		},
		saveFile : function(file) {
			fileToSave = {
					fileId: file.fileId,
					noteId: file.noteId,
					name: file.name,
					description: file.description,
					fileSize: file.fileSize,
					registeredDate: file.registeredDate,
					registeredBy: file.registeredBy,
			}
			return $http({method: 'PUT', url : 'rest/noteservice/notefile/' + file.fileId, data: fileToSave});
		},
		openFile : function(file, sheet) {
			$window.transferObject = { auth: encodeURIComponent(AuthToken.getAuthToken()), file: file, sheet: sheet}
			var newWindow = $window.open("templates/pages/root/audio.html","_blank");
		}
	}
}
)	

;
