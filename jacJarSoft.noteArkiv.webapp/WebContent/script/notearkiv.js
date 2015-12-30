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
	;
