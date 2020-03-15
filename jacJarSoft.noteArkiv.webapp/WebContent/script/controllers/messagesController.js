/**
 * 
 */

angular.module('notearkiv').controller('messagesController', function($scope, $location, Messages, State) {
	$scope.getStateName = function() { return "messagesController";}
	State.getOrRegisterScopeVariables(["predicate", "reverse"],["message.id", false]);
	$scope.messages = [];
//	$scope.predicate = "user.name";
//	$scope.reverse = false;
	$scope.setSort = function(sortOn) {
		$scope.reverse = ($scope.predicate === sortOn) ? !$scope.reverse : false;		
		$scope.predicate = sortOn;
	}
	$scope.getPromise = Messages.getMessages();
	$scope.getPromise.then(function successCallback(result) {
		$scope.messages = result;
	});
	
	$scope.newMessage = function() {
		$location.path("/messages/new");
	}
})
;
