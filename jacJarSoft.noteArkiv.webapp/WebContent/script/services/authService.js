/**
 * 
 */
noteArkiv.Auth = {}
noteArkiv.Auth.AccessLevel = {
	SYSADMIN: 100,
	ADMIN: 50,
	AUTHOR: 40,
	READER: 10,
	DISABLED: 0
}
angular.module('notearkiv').factory('Auth', function AuthFactory($http, AuthToken) {
	var isLoggedOn = false;
	var user;
	var userGravatarInfo;
	var logonInfo;
	var logonInProgress = false;
	return {
		isUserAuth : function(level) {
			if (!isLoggedOn)
				return false;
			if (this.getAccessLevel(user.accessLevel) >= level)
				return true;
			return false;
		},
		getAccessLevel : function(strLevel) {
			return noteArkiv.Auth.AccessLevel[strLevel];
		},
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
			
		},
		changePw : function(info) {
			return $http({method: "PUT", url : "rest/userservice/user/"+info.user+"/changepw", data: info })
			.then(function successCallback(result) {
				logonInfo.password = info.newpassword;
				return result;
			}, function errorCallback(result) {
				throw result;
			});
			
		}

	}
}
)	

;
