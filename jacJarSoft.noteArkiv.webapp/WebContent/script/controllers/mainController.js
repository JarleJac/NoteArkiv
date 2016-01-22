/**
 * 
 */
angular.module('notearkiv').controller('mainController', function($rootScope, $scope, $http, $location, Auth) {
	var controller = this;
	$scope.requestedPath = $location.path();
	$scope.rquestedQuery = $location.search();
	$scope.userName = "";
	$scope.logedOn = false;
	$scope.initPath = "/";

	this.resetError = function() {
    	$scope.errorOccured = false;
    	$scope.errorMsg = null;
	}
    $rootScope.$on('HttpStart', function(ev, args){
    	controller.resetError();
    });
    $rootScope.$on('HttpError', function(ev, args){
    	$scope.errorOccured = true;
    	var data = args.rejection.data;
    	if (typeof data.msgType === "undefined" ) {
        	$scope.errorMsg = args.rejection.status + " - " + args.rejection.statusText;
    	} else {
        	$scope.errorMsg = data.msg;
    	}
    		
    }); 	
	
	this.doLogon = function(logonInfo, fromLogonPage) {
		$http({method: 'POST', url : 'rest/appservice/logon', data: logonInfo })
		.then(function successCallback(result) {
			$scope.logonResult = result.data
			$scope.logonInfo = logonInfo;
			Auth.setAuthToken(result.data.authToken);
			//$http.defaults.headers.common.Authorization = $scope.authToken;
			
			if(result.data.user.mustChangePassword) {
				$location.path("/changepw").search({"mustChange": true}).replace();
			} else {
				controller.setLogonOk();
			}
		}, function errorCallback(result) {
			var r = result;
			$location.path("/logon").replace();
		})
		;
	};
	this.setLogonOk = function() {
		$scope.userName = $scope.logonResult.user.name;
		$scope.logedOn = true;
		localStorage.setItem("user", $scope.logonInfo.user);
		localStorage.setItem("password", $scope.logonInfo.password);
		$location.search("");
		if ($scope.requestedPath === null) {
			$location.path("/");
		} else {
			$location.path($scope.requestedPath);
			if ($scope.rquestedQuery != null) {
				$location.search($scope.rquestedQuery);
			}
		}
		$location.replace();
	};
	$scope.changePwOk = function() {
		controller.setLogonOk();
	}
	//Called from logon page
	$scope.logon = function(logonInfo) {
		controller.doLogon(logonInfo, true);
	}
	$scope.logoff = function() {
		localStorage.removeItem("user");
		localStorage.removeItem("password");
		$scope.logonResult = null;
		$scope.logonInfo = null;
		$scope.logedOn = false;
		$scope.userName = null;
		$location.path("/").replace();
	}

	var user = localStorage.getItem("user");
	var password = localStorage.getItem("password");
	if (user != null) {
		var logonInfo = {"user": user, "password": password};
		this.doLogon(logonInfo, false);
	} 
	else if ($location.path() != "/" && 
			$location.path() != "/about" &&
			$location.path() != "/help" &&
			$location.path() != "/logon") {
		$location.path("/logon").replace();
	}
});	

