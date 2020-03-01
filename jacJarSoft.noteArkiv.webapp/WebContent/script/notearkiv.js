var currentUser;
var currentUserName;
var noteArkiv = {}

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
		templateUrl : 'templates/pages/search/search.html',
		controller : 'searchController',
		access: {
            requiresLogin: true,
            accessLevel: "READER"
        }
	})
	.when('/search/list/:listId', {
		templateUrl : 'templates/pages/lists/search.html',
		controller : 'listSearchController',
		access: {
            requiresLogin: true,
            accessLevel: "READER"
        }
	})
	.when('/sheets/:sheetId', {
		templateUrl : 'templates/pages/sheets/sheet.html',
		controller : 'editSheetController',
		controllerAs : 'editSheetCtrl',
		access: {
            requiresLogin: true,
            accessLevel: "AUTHOR"
        }
		
	})
	.when('/lists', {
		templateUrl : 'templates/pages/lists/lists.html',
		controller : 'listsController',
		access: {
            requiresLogin: true,
            accessLevel: "READER"
        }
		
	})
	.when('/lists/:listId', {
		templateUrl : 'templates/pages/lists/list.html',
		controller : 'listController',
		access: {
            requiresLogin: true,
            accessLevel: "AUTHOR"
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
            requiresLogin: true,
            accessLevel: "READER"
        }
		
	})
	.when('/changepw/:userNo', {
		templateUrl : 'templates/pages/logon/changepw.html',
		controller : 'changepwController',
		controllerAs : 'changepwCtrl',
		access: {
            requiresLogin: true,
            accessLevel: "READER"
        }
		
	})
	.when('/users', {
		templateUrl : 'templates/pages/users/users.html',
		controller : 'usersController',
		access: {
            requiresLogin: true,
            accessLevel: "ADMIN"
        }
	})
	.when('/users/:userNo', {
		templateUrl : 'templates/pages/users/user.html',
		controller : 'userController',
		access: {
            requiresLogin: true,
            accessLevel: "ADMIN"
        }
	})
	.when('/users/profile/:userId', {
		templateUrl : 'templates/pages/users/profile.html',
		controller : 'userProfileController',
		access: {
            requiresLogin: true,
            accessLevel: "READER"
        }
		
	})
	.when('/messages', {
		templateUrl : 'templates/pages/messages/messages.html',
		controller : 'messagesController',
		access: {
            requiresLogin: true,
            accessLevel: "ADMIN"
        }
	})
	.when('/messages/:messageNo', {
		templateUrl : 'templates/pages/messages/message.html',
		controller : 'messageController',
		access: {
            requiresLogin: true,
            accessLevel: "ADMIN"
        }
	})
	.when('/sysadminutil', {
		templateUrl : 'templates/pages/admin/sysadminutil.html',
		controller : 'sysadminutilController',
		access: {
            requiresLogin: true,
            accessLevel: "SYSADMIN"
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
.run(function($rootScope, $location, Auth, State) {
	$rootScope.$on('$routeChangeStart', function (event, next, current) {
		State.backFwdUsed = true;
		if ($rootScope.normalLink === true) {
			State.backFwdUsed = false;
		}
		$rootScope.normalLink = undefined;
		$rootScope.$broadcast('savestate');		
        var authorised;
        if (next.access !== undefined) {
        	if (next.access.requiresLogin && !Auth.isLoggedOn() && !Auth.islogonInProgress()) {
        		$location.path("/logon").replace();
        	}
        	if (next.access.accessLevel !== undefined && !Auth.islogonInProgress()) {
        		if (!Auth.isUserAuth(Auth.getAccessLevel(next.access.accessLevel))) {
        			$location.path("/").replace();
    				$rootScope.$emit('ErrorMsg', "Du har ikke tilgang til den siden!");
        		}
        	}
        		
        }
    });	
//	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
//		if ($rootScope.normalLink !== undefined) {
//		}
//    });	
})
.run(function($rootScope, $location, $route, $window) {
	$rootScope.$on('$locationChangeSuccess', function() {
	    $rootScope.actualLocation = $location.path();
	});	

	$rootScope.$watch(function () {return $location.path()}, function (newLocation, oldLocation) {

	    //true only for onPopState
	    if($rootScope.actualLocation === newLocation) {

//	    	$route.current.scope.isBack = true;
//	        var back,
//	            historyState = $window.history.state;
//
//	        back = !!(historyState && historyState.position <= $rootScope.stackPosition);
//
//	        if (back) {
//	            //back button
//	            $rootScope.stackPosition--;
//	        } else {
//	            //forward button
//	            $rootScope.stackPosition++;
//	        }

	    } else {
	        //normal-way change of page (via link click)
	    	$rootScope.normalLink = true;
//	        if ($route.current) {
//		    	$route.current.scope.isBack = false;
//
////	            $window.history.replaceState({
////	                position: $rootScope.stackPosition
////	            });
//
//	            $rootScope.stackPosition++;
//
//	        }

	    }

	 });
})
;
	
