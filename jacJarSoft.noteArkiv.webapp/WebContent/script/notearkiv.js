var currentUser;
var currentUserName;

//register the interceptor as a service

angular.module('notearkiv', [ 'ngRoute' ])
.config(function($routeProvider, $provide, $httpProvider) {
	$routeProvider
	.when('/', {
		templateUrl : 'templates/pages/home/index.html'
	})
	.when('/help', {
		templateUrl : 'templates/pages/help/index.html'
	})
	.when('/about', {
		templateUrl : 'templates/pages/home/about.html'
	})
	.when('/search', {
		templateUrl : 'templates/pages/search/index.html',
		controller : 'searchController'
	})
	.when('/sheets/new', {
		templateUrl : 'templates/pages/sheets/sheet.html',
		controller : 'editSheetController',
		controllerAs : 'editSheetCtrl'
	})
	.when('/quicklists', {
		templateUrl : 'templates/pages/quicklists/index.html'
	})
	.when('/logon', {
		templateUrl : 'templates/pages/logon/logon.html',
		controller : 'logonController',
		controllerAs : 'logonCtrl'
	})
	.when('/changepw', {
		templateUrl : 'templates/pages/logon/changepw.html',
		controller : 'changepwController',
		controllerAs : 'changepwCtrl'
	})
	;
 	$provide.factory('myHttpInterceptor', function($q, $rootScope, Auth) {
		  return {
		    // optional method
		    'request': function(config) {
		    	$rootScope.$emit('HttpStart', {config: config});
		    	if (config.url.startsWith("rest/") && Auth.hasAuthToken()) {
		    		 config.headers['Authorization'] = Auth.getAuthToken();
		    	}
		    	return config;
		    },

		    // optional method
		   'requestError': function(rejection) {
		      // do something on error
//		      if (canRecover(rejection)) {
//		        return responseOrNewPromise
//		      }
		      return $q.reject(rejection);
		    },



		    // optional method
		    'response': function(response) {
		      // do something on success
		      return response;
		    },

		    // optional method
		   'responseError': function(rejection) {
		      // do something on error
//		      if (canRecover(rejection)) {
//		        return responseOrNewPromise
//		      }
			   $rootScope.$emit('HttpError', {rejection: rejection});
		      return $q.reject(rejection);
		    }
		  };
		});

	$httpProvider.interceptors.push('myHttpInterceptor');
	
})
;
	
