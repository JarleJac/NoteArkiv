/**
 * 
 */
angular.module('notearkiv').factory('Auth', function AuthFactory() {
	var authToken;
	var isLoggedOn = false;

	return {
		setAuthToken : function(token) {
			authToken = token;
		},
		getAuthToken : function() {
			return authToken;
		},
		hasAuthToken : function() {
			return (authToken) ? true : false;
		},
		setLoggedOn : function(loggedOn) {
			isLoggedOn = loggedOn;
		},
		isLoggedOn : function() {
			return isLoggedOn;
		}

	}
}
)	

;
