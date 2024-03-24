/**
 * 
 */

angular.module('notearkiv').controller('userProfileController', function($scope, $rootScope, $location, $routeParams, Auth, Users) {
	if (Auth.getUser().no != $routeParams.userId) {
		$location.path("/").replace();
		$rootScope.$emit('ErrorMsg', "Du har ikke tilgang til den siden!");
		return;
	}
	$scope.changeMode = false;
	$scope.user;
	var gravatarInfo;
	var readUser = function(userId) {
		Users.getUser(userId)
		.then(function successCallback(result) {
			$scope.user = result.data.user;
			gravatarInfo = result.data.gravatar;
			Auth.updateUserInfo(result.data);
		});
	};
	readUser($routeParams.userId);
	$scope.userGravatarUrl = function(size) {
		return GravatarUtil.getUrl(gravatarInfo,size);
	}
	$scope.getAcccessLevelText = function() {
		if ($scope.user === undefined)
			return "";
		return Users.getAccessLevelText($scope.user.accessLevel);
	}
	$scope.startChanges = function() {
		$scope.changeMode = true;
	}
	$scope.cancelChanges = function() {
		readUser($scope.user.no);
		$scope.changeMode = false;
	}
	$scope.saveChanges = function() {
		$scope.promiseMsg = "Lagrer endringer";
		$scope.pagePromise = Users.updateUser($scope.user);			
		$scope.pagePromise.then(function successCallback(result) {
			$rootScope.$emit('OkMessage', "Bruker ble lagret ok");
			$scope.changeMode = false;
			readUser($scope.user.no)
		});
	}
})
;
