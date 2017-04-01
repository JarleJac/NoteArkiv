/**
 * 
 */
angular.module('notearkivAudio', [ ])
.controller('audioController', function($rootScope, $scope, $window, $sce, $timeout) {
	$scope.ting = "Ting fra controller12";
	$scope.retries=0;
	$scope.noFile=false;
	var initFile = function() {
  		$scope.file = $window.file;

  		if ($scope.file !== undefined ) {
  			$rootScope.title = $scope.file.name;
  			$scope.authInfo = $window.authInfo;
  			$scope.audioUrl= '../../../rest/noteservice/notefile/' + $scope.file.fileId + '/media?authInfo=' + $scope.authInfo;
  			var src = '<source src="' + $scope.audioUrl + '" type="audio/mpeg">';
  			$scope.audioSource = $sce.trustAsHtml(src);
  		}
	}
	var retryInitFile = function() {
		$scope.retries += 1;
		$scope.msg = "Vent litt " + $scope.retries;
		initFile();
		if ($scope.file === undefined) {
			if ($scope.retries < 6) {
				$timeout(function(){ 
					retryInitFile();
			  	}, 1000);
			} else {
				$scope.noFile=true;
				$scope.msg = undefined;
			}
		} else {
			$scope.msg = undefined;
		}
		
	}
	initFile();
	if ($scope.file === undefined) {
		retryInitFile(); //Internet explorer need some time to get the data to/from $window
	}
		
})
;


