/**
 * 
 */

angular.module('notearkiv').factory('Users', function UsersFactory($q, $http) {

	return {
		getUsers : function() {
			return $http({method: 'GET', url : 'rest/userservice/user'});
		},
		addUser : function(user) {
			return $http({method: 'POST', url : 'rest/userservice/user', data: user});
		},
		deleteUser : function(user) {
			return $http({method: 'DELETE', url : 'rest/userservice/user/' + user.no});
		}
	}
}
)	
;
