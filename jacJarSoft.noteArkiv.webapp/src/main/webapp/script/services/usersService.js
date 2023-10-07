/**
 * 
 */

angular.module('notearkiv').factory('Users', function UsersFactory($q, $http) {
	var AccessLevelTextMap = {
			SYSADMIN: "System admin",
			ADMIN: "Administrator",
			AUTHOR: "Kan endre",
			READER: "Kun les",
			DISABLED: "Sperret"
		};
	
	
	return {
		getUsers : function() {
			return $http({method: 'GET', url : 'rest/userservice/user'})
			.then(function successCallback(result) {
				var users = result.data.map(function(user) {
					var userData = {user : user, accessLevelText : AccessLevelTextMap[user.accessLevel]};
					return userData;
				});
				return users;
			}, function errorCallback(result) {
				throw result;
			});
		},
		getUser : function(userNo) {
			return $http({method: 'GET', url : 'rest/userservice/user/' + userNo });
		},
		addUser : function(user) {
			return $http({method: 'POST', url : 'rest/userservice/user', data: user});
		},
		updateUser : function(user) {
			return $http({method: 'PUT', url : 'rest/userservice/user', data: user});
		},
		deleteUser : function(user) {
			return $http({method: 'DELETE', url : 'rest/userservice/user/' + user.no});
		},
		getAccessLevelText : function(level) {
			return AccessLevelTextMap[level];
		}
	}
}
)	
;
