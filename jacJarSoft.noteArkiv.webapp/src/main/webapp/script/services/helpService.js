/**
 * 
 */
angular.module('notearkiv').factory('Help', function ListsFactory($http) {
	return {
		getHelpFilesInfo : function() {
			return $http({method: 'GET', url : 'rest/appservice/gethelpfilesinfo/' })
				.then(function successCallback(result) {
					var helpFiles = result.data;
					return helpFiles;
				}, function errorCallback(result) {
					throw result;
				});
		}
	}
}
)	

;
