/**
 * 
 */

angular.module('notearkiv').controller('listsController', function($scope, $location, Lists, State) {
	$scope.getStateName = function() { return "listsController";}
	State.getOrRegisterScopeVariables(["predicate", "reverse"],["name", false]);
	$scope.lists = [];
	$scope.setSort = function(sortOn) {
		$scope.reverse = ($scope.predicate === sortOn) ? !$scope.reverse : false;		
		$scope.predicate = sortOn;
	}
	$scope.getPromise = Lists.getLists();
	$scope.getPromise.then(function successCallback(result) {
		$scope.lists = result;
	});
	
	$scope.newList = function() {
		$location.path("/lists/new");
	}
})
;
