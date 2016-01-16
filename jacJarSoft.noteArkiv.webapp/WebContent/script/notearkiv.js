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
 	$provide.factory('myHttpInterceptor', function($q, Auth) {
		  return {
		    // optional method
		    'request': function(config) {
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
		      return $q.reject(rejection);
		    }
		  };
		});

	$httpProvider.interceptors.push('myHttpInterceptor');
	
})
.controller('mainController', function($scope, $http, $location, Auth) {
	var controller = this;
	$scope.requestedPath = $location.path();
	$scope.rquestedQuery = $location.search();
	$scope.userName = "";
	$scope.logedOn = false;
	$scope.initPath = "/";

	this.doLogon = function(logonInfo, fromLogonPage) {
		$http({method: 'POST', url : 'rest/appservice/logon', data: logonInfo })
		.then(function successCallback(result) {
			$scope.logonResult = result.data
			$scope.logonInfo = logonInfo;
			Auth.setAuthToken(result.data.authToken);
			//$http.defaults.headers.common.Authorization = $scope.authToken;
			
			if(result.data.user.mustChangePassword) {
				$location.path("/changepw").search({"mustChange": true}).replace();
			} else {
				controller.setLogonOk();
			}
		}, function errorCallback(result) {
			var r = result;
			$location.path("/logon").replace();
		})
		;
	};
	this.setLogonOk = function() {
		$scope.userName = $scope.logonResult.user.name;
		$scope.logedOn = true;
		localStorage.setItem("user", $scope.logonInfo.user);
		localStorage.setItem("password", $scope.logonInfo.password);
		$location.search("");
		if ($scope.requestedPath === null) {
			$location.path("/");
		} else {
			$location.path($scope.requestedPath);
			if ($scope.rquestedQuery != null) {
				$location.search($scope.rquestedQuery);
			}
		}
		$location.replace();
	};
	$scope.changePwOk = function() {
		controller.setLogonOk();
	}
	//Called from logon page
	$scope.logon = function(logonInfo) {
		controller.doLogon(logonInfo, true);
	}
	$scope.logoff = function() {
		localStorage.removeItem("user");
		localStorage.removeItem("password");
		$scope.logonResult = null;
		$scope.logonInfo = null;
		$scope.logedOn = false;
		$scope.userName = null;
		$location.path("/").replace();
	}

	var user = localStorage.getItem("user");
	var password = localStorage.getItem("password");
	if (user != null) {
		var logonInfo = {"user": user, "password": password};
		this.doLogon(logonInfo, false);
	} 
	else if ($location.path() != "/" && 
			$location.path() != "/about" &&
			$location.path() != "/help" &&
			$location.path() != "/logon") {
		$location.path("/logon").replace();
	}
})	
.factory('Auth', function AuthFactory() {
	 var authToken;

	 return {
	     setAuthToken : function(token){
	    	 authToken = token;
	     },
	     getAuthToken: function() {
	    	 return authToken;
	     },
	     hasAuthToken : function(){
	         return(authToken)? true : false;
	     }
	     
	   }
	 })	
;
	
