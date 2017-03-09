/**
 * 
 */

angular.module('notearkiv').controller('listController', function($rootScope, $scope, $location, $routeParams, Lists, SimpleDlg) {
	$scope.isNew = $routeParams.listId === "new" ? true : false;
	$scope.pagePromise = null;
	if ($scope.isNew) {
		$scope.list = {};
	}
	else {
		$scope.promiseMsg = "Henter liste";
		$scope.pagePromise = Lists.getList($routeParams.listId); 
		$scope.pagePromise.then(function successCallback(result) {
			$scope.list = result;
			$scope.pagePromise = null;
		});
	}
	$scope.newList = function() {
		$location.path("/lists/new");
	}
	$scope.saveList = function() {
		$scope.promiseMsg = "Lagrer liste";
		if ($scope.isNew) {
			$scope.pagePromise = Lists.addList($scope.list);			
		} else {
			$scope.pagePromise = Lists.updateList($scope.list);			
		}
		$scope.pagePromise.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Liste ble lagret ok");
			$scope.pagePromise = null;
			if ($scope.isNew)
				$location.path("/search/list/" + result.listId);
		});
		
	}
	$scope.deleteList = function() {
		SimpleDlg.runSimpleConfirmDlg("Bekreft sletting", "Er du sikker på at du vil slette " + $scope.list.name)
		.result.then(function (result) {
			$scope.promiseMsg = "Sletter liste";
			$scope.pagePromise = Lists.deleteList($scope.list);
			$scope.pagePromise.then(function successCallback(result) {
				$location.path("/lists/new").replace();
				$rootScope.$emit('OkMessage', "Liste ble slettet.");
				$scope.pagePromise = null;
			});
		}); 
	}
})
;
