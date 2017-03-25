/**
 * 
 */
angular.module('notearkivAudio', [ ])
.controller('audioController', function($scope, $window, $sce, $timeout) {
	$scope.ting = "Ting fra controller12";
	$timeout(function(){ //Internet explorer need some time to get the data to/from $window
  		$scope.file = $window.file;

  		if ($scope.file !== undefined ) {
  			$scope.authInfo = $window.authInfo;
  			$scope.audioUrl= '../../../rest/noteservice/notefile/' + $scope.file.fileId + '/media?authInfo=' + $scope.authInfo;
  			var src = '<source src="' + $scope.audioUrl + '" type="audio/mpeg">';
  			$scope.audioSource = $sce.trustAsHtml(src);
  		}
  	}, 200);
		
})
;


