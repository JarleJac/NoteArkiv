/**
 * 
 */
angular.module('notearkiv').factory('Auth', function AuthFactory($http, AuthToken) {
	var isLoggedOn = false;
	var user;
	var userGravatarInfo;
	var logonInfo;
	var logonInProgress = false;
	return {
		getUser : function() {
			return user;
		},
		getUserGravatarInfo : function() {
			return userGravatarInfo;
		},
		getLogonInfo : function() {
			return logonInfo;
		},
		setLoggedOn : function(loggedOn) {
			logonInProgress = false;
			isLoggedOn = loggedOn;
		},
		isLoggedOn : function() {
			return isLoggedOn;
		},
		islogonInProgress : function() {
			return logonInProgress;
		},
		doLogon : function(Info, fromLogonPage) {
			logonInfo = Info;
			return $http({method: 'POST', url : 'rest/appservice/logon', data: logonInfo })
			.then(function successCallback(result) {
				logonInProgress = true;
				user = result.data.user;
				userGravatarInfo = result.data.gravatar;
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
