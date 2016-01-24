/**
 * 
 */
angular.module('notearkiv').factory('Auth', function AuthFactory($http, AuthToken) {
	var isLoggedOn = false;
	var user;
	var logonInfo;
	return {
		getUser : function() {
			return user;
		},
		getLogonInfo : function() {
			return logonInfo;
		},
		setLoggedOn : function(loggedOn) {
			isLoggedOn = loggedOn;
		},
		isLoggedOn : function() {
			return isLoggedOn;
		},
		doLogon : function(Info, fromLogonPage) {
			logonInfo = Info;
			return $http({method: 'POST', url : 'rest/appservice/logon', data: logonInfo })
			.then(function successCallback(result) {
				user = result.data.user;
				AuthToken.setAuthToken(result.data.authToken);
				return {
					user: result.data.user
				}
			}, function errorCallback(result) {
				throw result;
			});
			
		}

	}
}
)	

;
