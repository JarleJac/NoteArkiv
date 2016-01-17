/**
 * 
 */
angular.module('notearkiv').factory('Auth', function AuthFactory() {
	var authToken;

	return {
		setAuthToken : function(token) {
			authToken = token;
		},
		getAuthToken : function() {
			return authToken;
		},
		hasAuthToken : function() {
			return (authToken) ? true : false;
		}

	}
}
)	

;
