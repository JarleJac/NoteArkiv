/**
 * 
 */
angular.module('notearkiv').controller('mainController', function($rootScope, $scope, $http, $location, Auth) {
	var controller = this;
	$scope.requestedPath = $location.path();
	$scope.rquestedQuery = $location.search();
	$scope.userName = "";
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
	$scope.isLoggedOn = function() {
		return Auth.isLoggedOn();
	}
	this.doLogon = function(logonInfo, fromLogonPage) {
		Auth.doLogon(logonInfo, fromLogonPage)
		.then(function successCallback(result) {
			if(result.user.mustChangePassword) {
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
		$scope.userName = Auth.getUser().name;
		Auth.setLoggedOn(true);
		localStorage.setItem("user", Auth.getLogonInfo().user);
		localStorage.setItem("password", Auth.getLogonInfo().password);
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
		Auth.setLoggedOn(false);
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

