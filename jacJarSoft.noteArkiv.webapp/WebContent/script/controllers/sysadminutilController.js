/**
 * 
 */

angular.module('notearkiv').controller('sysadminutilController', function($rootScope, $scope, Admin) {
	$scope.mmm = "";
	$scope.importUsers = function() {
		$scope.mmm = "Importerer epost adresser";
		$scope.importPromise = Admin.importUsers();	
		$scope.importPromise.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Epostadresser ble importert ok");
		});
		
	}
})
;
