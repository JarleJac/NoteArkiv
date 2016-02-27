/**
 * 
 */
angular.module('notearkiv').factory('SimpleDlg', function AuthFactory($uibModal) {
	return {
		runSimpleConfirmDlg : function(title, message) {
		    var modalInstance = $uibModal.open({
		        animation: false,
		        templateUrl: 'templates/dialogs/SimpleConfirmDlg.html',
		        controller: 'SimpleConfirmDlg',
		        backdrop: 'static',
		        //size: size,
		        resolve: {
		          title: function () {
		            return title;
		          },
		          message: function () {
			            return message;
			          },
		        }
		      });
		    return modalInstance;

		},
		getxxx : function() {
			return null;
		}
	}
}
)	

;
