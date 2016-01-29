/**
 * 
 */

angular.module('notearkiv').controller('userProfileController', function($scope, $http,  $routeParams) {
	$scope.user;
	var gravatarInfo;
	$http({method: "GET", url : "rest/userservice/user/"+$routeParams.userId })
	.then(function successCallback(result) {
		$scope.user = result.data.user;
		gravatarInfo = result.data.gravatar;
	});
	$scope.userGravatarUrl = function(size) {
		return GravatarUtil.getUrl(gravatarInfo,size);
	}
})
;
