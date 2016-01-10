var currentUser;
var currentUserName;

angular.module('notearkiv', [ 'ngRoute' ])
.config(function($routeProvider) {
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

//.factory('httpErrorsInterceptor', function ($q, $rootScope, EventsDict) {
//
//    function successHandler(response) {
//        return response;
//    }
//
//    function errorHandler(response) {
//        return response;
////	        $rootScope.$broadcast(EventsDict.httpError, response.data.cause);
////	        return $q.reject(response);
//    }
//
//    return function(httpPromise) {
//        return httpPromise.then(successHandler, errorHandler);
//    };
//
//})	
;
	
