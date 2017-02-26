/**
 * 
 */
angular.module('notearkiv').factory('Lists', function ListsFactory($http) {
	var fixListAfterRead = function(list) {
		list.listDate = new Date(list.listDate);
		return list;
	}
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
					return fixListAfterRead(result.data);
				}, function errorCallback(result) {
					throw result;
				});
		},
		addList : function(list) {
			delete list.listDateStr;
			return $http({method: 'POST', url : 'rest/noteservice/list', data: list })
				.then(function successCallback(result) {
					return fixListAfterRead(result.data);
				}, function errorCallback(result) {
					throw result;
				});
		},
		updateList : function(list) {
			delete list.listDateStr;
			return $http({method: 'PUT', url : 'rest/noteservice/list', data: list })
				.then(function successCallback(result) {
					return fixListAfterRead(result.data);
				}, function errorCallback(result) {
					throw result;
				});
		},
		deleteList : function(list) {
			return $http({method: 'DELETE', url : 'rest/noteservice/list/' + list.listId });
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
