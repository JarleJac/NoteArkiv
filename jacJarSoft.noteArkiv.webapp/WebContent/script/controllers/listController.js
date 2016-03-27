/**
 * 
 */

angular.module('notearkiv').controller('listController', function($rootScope, $scope, $location, $routeParams, Lists, SimpleDlg) {
	$scope.isNew = $routeParams.listId === "new" ? true : false;
	if ($scope.isNew) {
		$scope.list = {};
	}
	else {
		$scope.getPromise = Lists.getList($routeParams.listId); 
		$scope.getPromise.then(function successCallback(result) {
			$scope.list = result;
		});
	}
	$scope.newList = function() {
		$location.path("/lists/new");
	}
	$scope.saveList = function() {
		if ($scope.isNew) {
			$scope.savePromise = Lists.addList($scope.list);			
		} else {
			$scope.savePromise = Lists.updateList($scope.list);			
		}
		$scope.savePromise.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Liste ble lagret ok");
			if ($scope.isNew)
				$location.path("/lists/" + result.listId);
		});
		
	}
	$scope.deleteList = function() {
		SimpleDlg.runSimpleConfirmDlg("Bekreft sletting", "Er du sikker på at du vil slette " + $scope.list.name)
		.result.then(function (result) {
			$scope.deletePromise = Lists.deleteList($scope.list);
			$scope.deletePromise.then(function successCallback(result) {
				$location.path("/lists/new").replace();
				$rootScope.$emit('OkMessage', "Liste ble slettet.");
			});
		}); 
	}
})
;
