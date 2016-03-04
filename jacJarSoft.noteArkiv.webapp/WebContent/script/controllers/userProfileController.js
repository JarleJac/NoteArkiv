/**
 * 
 */

angular.module('notearkiv').controller('userProfileController', function($scope, $location, $http,  $routeParams, Auth) {
	if (Auth.getUser().no != $routeParams.userId) {
		$location.path("/").replace();
		$rootScope.$emit('ErrorMsg', "Du har ikke tilgang til den siden!");
		return;
	}
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
