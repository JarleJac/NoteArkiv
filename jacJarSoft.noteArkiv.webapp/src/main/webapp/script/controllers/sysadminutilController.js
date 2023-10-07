/**
 * 
 */

angular.module('notearkiv').controller('sysadminutilController', function($rootScope, $scope, Admin) {
	$scope.pagePromise = null;
	$scope.importUsers = function() {
		$scope.promiseMsg = "Imorterer e-post adresser";
		$scope.pagePromise = Admin.importUsers();	
		$scope.pagePromise.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Epostadresser ble importert ok");
			$scope.pagePromise = null;
		});
		
	}
	$scope.createCsvExport = function() {
		$scope.promiseMsg = "Eksporterer note data";
		$scope.pagePromise = Admin.createCsvExport();	
		$scope.pagePromise.then(function successCallback(result) {
			if (result.data.code == 0)
				$rootScope.$emit('OkMessage', result.data.message);
			else
				$rootScope.$emit('ErrorMsg', result.data.message);
			$scope.pagePromise = null;
		});
		
	}
})
;
