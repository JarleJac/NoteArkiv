/**
 * 
 */

angular.module('notearkiv').factory('Admin', function AdminFactory($q, $http) {
	
	return {
		importUsers : function() {
			return $http({method: 'POST', url : 'rest/adminservice/importuseremail'});
		},
		createCsvExport : function() {
			return $http({method: 'POST', url : 'rest/adminservice/createcsvexport'});
		}		
	}
}
)	
;
