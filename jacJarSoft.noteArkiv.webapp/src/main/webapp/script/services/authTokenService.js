/**
 * 
 */
angular.module('notearkiv').factory('AuthToken', function AuthTokenFactory() {
	var authToken;
	return {
		hasAuthToken : function() {
			return (authToken) ? true : false;
		},
		setAuthToken : function(token) {
			authToken = token;
		},
		getAuthToken : function() {
			return authToken;
		}
	}
});
