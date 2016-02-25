/**
 * 
 */
angular.module('notearkiv').factory('Sheets', function SheetsFactory($http) {
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
		deleteFile : function(fileId) {
			return $http({method: 'DELETE', url : 'rest/noteservice/notefile/' + fileId  });
		}
	}
}
)	

;
