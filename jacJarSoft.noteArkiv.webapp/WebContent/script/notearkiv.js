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
		;
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
	
