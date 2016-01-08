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
	;
})
.controller('mainController', function($scope, $http) {
	var controller = this;
	$scope.userName = "<>";
	$scope.logedOn = false;
	
	$scope.logon = function(logonInfo) {
		$http({method: 'POST', url : 'rest/appservice/logon', data: logonInfo })
		.then(function successCallback(result) {
			var x = result.data;
			$scope.userName = result.data.user.name;
			$scope.logedOn = true;
			window.location.replace("#/");
		}, function errorCallback(result) {
			var r = result;
		})
		;
	}
})	
	.factory('httpErrorsInterceptor', function ($q, $rootScope, EventsDict) {

	    function successHandler(response) {
	        return response;
	    }

	    function errorHandler(response) {
	        return response;
//	        $rootScope.$broadcast(EventsDict.httpError, response.data.cause);
//	        return $q.reject(response);
	    }

	    return function(httpPromise) {
	        return httpPromise.then(successHandler, errorHandler);
	    };

	})	
	;
	
