/**
 * 
 */
angular.module('notearkivAudio', [ ])
.controller('audioController', function($rootScope, $scope, $window, $sce, $timeout) {
	$scope.noFile=false;
	var initFile = function() {
		var transferObject = $window.opener == null ? undefined : $window.opener.transferObject;

  		if (transferObject !== undefined ) {
  			if (transferObject.appSettings.backgroundImageUrl != null) {
  				var url = "url(../../../" + transferObject.appSettings.backgroundImageUrl + ")"
  				$('body').css('background-image', url);
  			}

  			$scope.file = transferObject.file;
  	  		$scope.sheet = transferObject.sheet;
  			$rootScope.title = $scope.file.name;
  			$scope.authInfo = transferObject.auth;
  			$scope.audioUrl= '../../../rest/noteservice/notefile/' + $scope.file.fileId + '/media?authInfo=' + $scope.authInfo;
  			var src = '<source src="' + $scope.audioUrl + '" type="' + $scope.file.mimeType + '">'; 
  			$scope.audioSource = $sce.trustAsHtml(src);
  		} else {
			$scope.noFile=true;
  		}
	}

	initFile();
		
})
;


