var currentUser;
var currentUserName;

//register the interceptor as a service

angular.module('notearkiv', [ 'ngRoute' ])
.config(function($routeProvider, $provide, $httpProvider) {
	$routeProvider
	.when('/', {
		templateUrl : 'templates/pages/home/index.html'
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
	
	$provide.factory('myHttpInterceptor', function($q) {
		  return {
		    // optional method
		    'request': function(config) {
		    	if (config.url.startsWith("rest/")) {
		    		 config.headers['Authorization'] = 'Basic Token';
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
.controller('mainController', function($scope, $http, $location) {
	var controller = this;
	this.requestedPath = null;
	this.rquestedQuery = null;
	$scope.userName = "";
	$scope.logedOn = false;
	$scope.initPath = "/";
	var user = localStorage.getItem("user");
	var password = localStorage.getItem("password");
	if (user != null) {
		var logonInfo = {"user": user, "pasword": password};
		doLogon(info)
	}
	if (!$scope.logedOn) {
		if ($location.path() != "/" && $location.path() != "/logon") {
			requestedPath = $location.path();
			rquestedQuery = $location.search();
			$location.path("/logon").replace();
		}
	}

	this.doLogon = function(logonInfo) {
		$http({method: 'POST', url : 'rest/appservice/logon', data: logonInfo })
		.then(function successCallback(result) {
			$scope.logonResult = result.data
			$http.defaults.headers.common.Authorization = $scope.authToken;
			
			if(result.data.user.mustChangePassword) {
				$location.path("/changepw").search({"mustChange": true}).replace();
			} else {
				controller.setLogonOk();
			}
		}, function errorCallback(result) {
			var r = result;
		})
		;
	};
	this.setLogonOk = function() {
		$scope.userName = $scope.logonResult.user.name;
		$scope.logedOn = true;
		$location.search("");
		if (controller.requestedPath === null) {
			$location.path("/");
		} else {
			$location.path(controller.requestedPath);
			if (controller.rquestedQuery != null) {
				$location.search(controller.rquestedQuery);
			}
		}
		$location.replace();
	};
	$scope.changePwOk = function() {
		controller.setLogonOk();
	}
	//Called from logon page
	$scope.logon = function(logonInfo) {
		controller.doLogon(logonInfo);
	}
})	
;
	
