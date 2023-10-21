/**
 * 
 */

angular.module('notearkiv').controller('helpController', function($scope, $location, Help, State) {
	$scope.getStateName = function() { return "helpController";}
	State.getOrRegisterScopeVariables(["predicate", "reverse"],["name", false]);
	$scope.helpFiles = [];
	$scope.getPromise = Help.getHelpFilesInfo();
	$scope.getPromise.then(function successCallback(result) {
		$scope.helpFiles = result;
	});
})
;
