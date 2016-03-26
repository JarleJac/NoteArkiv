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
		},
		getList : function(listsId) {
			return $http({method: 'GET', url : 'rest/noteservice/list/' + listsId })
				.then(function successCallback(result) {
					var list = result.data
					list.listDate = new Date(list.listDate);
					return list;
				}, function errorCallback(result) {
					throw result;
				});
		},
		getLists : function() {
			return $http({method: 'GET', url : 'rest/noteservice/list/' })
				.then(function successCallback(result) {
					var lists = result.data.map(function(list) {
						list.registeredDate = new Date(list.registeredDate);
						list.listDate = new Date(list.listDate);
						return list;
					});
					return lists;
				}, function errorCallback(result) {
					throw result;
				});
		}
	}
}
)	

;
