/**
 * 
 */
angular.module('notearkiv').factory('Lists', function ListsFactory($http) {
	return {
		connectToList : function(listsId, sheetId) {
			return $http({method: 'POST', url : 'rest/noteservice/list/' + listsId, data: sheetId });
		},
		disconnectFromList : function(listsId, sheetId) {
			return $http({method: 'DELETE', url : 'rest/noteservice/list/' + listsId + "/" + sheetId });
		}
	}
}
)	

;
