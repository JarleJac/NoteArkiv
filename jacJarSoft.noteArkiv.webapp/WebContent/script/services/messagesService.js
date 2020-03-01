/**
 * 
 */

angular.module('notearkiv').factory('Messages', function MessagesFactory($q, $http) {
	var MessagteTypeTextMap = {
			Normal: "Vanlig",
			Important: "Viktig"
		};
	
	
	return {
		getUsers : function() {
			return $http({method: 'GET', url : 'rest/userservice/user'})
			.then(function successCallback(result) {
				var users = result.data.map(function(user) {
					var userData = {user : user, accessLevelText : '???'};
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
		getMessageTypeText : function(level) {
			return MessagteTypeTextMap[level];
		}
	}
}
)	
;
