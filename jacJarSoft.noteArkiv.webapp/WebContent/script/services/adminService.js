/**
 * 
 */

angular.module('notearkiv').factory('Admin', function AdminFactory($q, $http) {
	
	return {
		importUsers : function(user) {
			return $http({method: 'POST', url : 'rest/adminservice/importuseremail'});
		}
	}
}
)	
;
