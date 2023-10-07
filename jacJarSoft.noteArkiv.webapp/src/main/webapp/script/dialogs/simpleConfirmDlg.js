/**
 * 
 */

angular.module('notearkiv').controller('SimpleConfirmDlg',
		function($scope, $uibModalInstance, title, message) {
			$scope.title = title;
			$scope.message = message;
			$scope.ok = function() {
				$uibModalInstance.close(true);
			};

			$scope.cancel = function() {
				$uibModalInstance.dismiss('cancel');
			};
		});
