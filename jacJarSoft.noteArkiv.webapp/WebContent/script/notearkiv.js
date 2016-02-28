var currentUser;
var currentUserName;

var GravatarUtil = {
		getUrl : function(info, size) {
			if (info !== undefined)
				return "http://www.gravatar.com/avatar/" + info.hash + "?d=" +info.dParam + "&s=" + size;
			else
				return ""
		}
} 


angular.module('notearkiv', [ 'ngRoute' ,'cgBusy', 'angularFileUpload', 'ui.bootstrap' ])
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
	.when('/search/:tab', {
		templateUrl : 'templates/pages/search/index.html',
		controller : 'searchController',
		access: {
            requiresLogin: true
        }
	})
	.when('/sheets/:sheetId', {
		templateUrl : 'templates/pages/sheets/sheet.html',
		controller : 'editSheetController',
		controllerAs : 'editSheetCtrl',
		access: {
            requiresLogin: true
        }
		
	})
	.when('/quicklists', {
		templateUrl : 'templates/pages/quicklists/index.html',
		access: {
            requiresLogin: true
        }
		
	})
	.when('/logon', {
		templateUrl : 'templates/pages/logon/logon.html',
		controller : 'logonController',
		controllerAs : 'logonCtrl'
	})
	.when('/changepw', {
		templateUrl : 'templates/pages/logon/changepw.html',
		controller : 'changepwController',
		controllerAs : 'changepwCtrl',
		access: {
            requiresLogin: true
        }
		
	})
	.when('/users', {
		templateUrl : 'templates/pages/users/users.html',
		access: {
            requiresLogin: true
        }
	})
	.when('/users/profile/:userId', {
		templateUrl : 'templates/pages/users/profile.html',
		controller : 'userProfileController',
		access: {
            requiresLogin: true
        }
		
	})
	;
 	$provide.factory('myHttpInterceptor', function($q, $rootScope, AuthToken) {
		  return {
		    // optional method
		    'request': function(config) {
		    	$rootScope.$emit('HttpStart', {config: config});
		    	if (config.url.substr(0, 5) === "rest/" && AuthToken.hasAuthToken()) {
		    		 config.headers['Authorization'] = AuthToken.getAuthToken();
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
.run(function($rootScope, $location, Auth) {
	$rootScope.$on('$routeChangeStart', function (event, next) {
        var authorised;
        if (next.access !== undefined) {
        	if (next.access.requiresLogin && !Auth.isLoggedOn() && !Auth.islogonInProgress()) {
        		$location.path("/logon").replace();
        	}
        		
//            authorised = authorization.authorize(next.access.loginRequired,
//                                                 next.access.permissions,
//                                                 next.access.permissionCheckType);
//            if (authorised === jcs.modules.auth.enums.authorised.loginRequired) {
//                $location.path(jcs.modules.auth.routes.login);
//            } else if (authorised === jcs.modules.auth.enums.authorised.notAuthorised) {
//                $location.path(jcs.modules.auth.routes.notAuthorised).replace();
//            }
        }
    });	
})
;
	
