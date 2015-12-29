(function() {
	angular.module('notearkiv', [ 'ngRoute' ])
	.config(function($routeProvider) {
		$routeProvider
		.when('/', {
			templateUrl : 'templates/pages/home/index.html'
		})
		.when('/sheets/new', {
			templateUrl : 'templates/pages/sheets/sheet.html'
		})
		.when('/quicklists', {
			templateUrl : 'templates/pages/quicklists/index.html'
		})
		;
	})
	.controller('someController', function() {

	})
	;

})();
