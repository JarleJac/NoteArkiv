/**
 * 
 */

angular.module('notearkiv').controller('homeController', function($rootScope, $scope, $location, $timeout, $routeParams, Messages, Auth) {
	
	$scope.init = function() {
  		if (Auth.isLoggedOn()) {
  			$scope.refresh();
  		}
  		else {
  	      	$timeout(function(){
  	      		if (Auth.isLoggedOn()) {
  	      			$scope.refresh();
  	      		}
  	      	}, 500);
  		}
		
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
})
;
