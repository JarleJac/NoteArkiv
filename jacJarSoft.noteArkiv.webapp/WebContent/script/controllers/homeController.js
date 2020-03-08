/**
 * 
 */

angular.module('notearkiv').controller('homeController', function($rootScope, $scope, $location, $timeout, $routeParams, Messages, Auth) {
	
	$scope.init = function() {
      	$timeout(function(){
      		$scope.refresh()
      	}, 200);
		
	}
	$scope.refresh = function() {
		$scope.promiseMsg = "Sjekker om det er noe meldinger";
		$scope.pagePromise = Messages.getActiveMessages(); 
		$scope.pagePromise.then(function successCallback(result) {
			//result.data.sheet.registeredDate = new Date(result.data.sheet.registeredDate);
			$scope.messages = result;
		});
	}	

	$scope.pagePromise = null;
	if (Auth.isLoggedOn()) {
		$scope.refresh();
	}
})
;
