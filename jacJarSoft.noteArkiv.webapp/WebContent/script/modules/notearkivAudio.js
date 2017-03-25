/**
 * 
 */
angular.module('notearkivAudio', [ ])
.controller('audioController', function($scope, $window, $sce) {
	$scope.ting = "Ting fra controller";
	$scope.file = $window.file;
	$scope.authInfo = $window.authInfo;
	$scope.audioUrl= '../../../rest/noteservice/notefile/' + $scope.file.fileId + '/media?authInfo=' + $scope.authInfo;
	var src = '<source src="' + $scope.audioUrl + '" type="audio/mpeg">';
	$scope.audioSource = $sce.trustAsHtml(src);
		
})
;


