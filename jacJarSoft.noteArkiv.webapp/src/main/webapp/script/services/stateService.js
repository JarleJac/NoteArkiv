/**
 * 
 */

angular.module('notearkiv').factory('State', function StateFactory($route, $rootScope) {
	//var lastRouteChangeNormal = undefined;
	var getOrRegisterScopeVariable = function (scope, name, defaultValue, storedScope, useDefault) {
        if (storedScope[name] == null || useDefault) {
            storedScope[name] = defaultValue;
        }
        scope[name] = storedScope[name];
    }
	
	var service =  {
		backFwdUsed: false, 
		
		scopeStorage: {},

		getOrRegisterScopeVariables: function (names, defaultValues) {
            var scope = $route.current.locals.$scope;
			if (scope.getStateName === undefined)
				throw "No stateName defined for this scope!";
            
            var stateName = scope.getStateName();
            var storedScope = service.scopeStorage[stateName];
            if (storedScope == null) {
                storedScope = {};
            }
            if (typeof names === "string") {
                getOrRegisterScopeVariable(scope, names, defaultValues, storedScope, !service.backFwdUsed);
            } else if (Array.isArray(names)) {
                angular.forEach(names, function (name, i) {
                    getOrRegisterScopeVariable(scope, name, defaultValues[i], storedScope, !service.backFwdUsed);
                });
            } else {
                throw "First argument to GetOrRegisterScopeVariables is not a string or array";
            }
            
            // save stored scope back off
            service.scopeStorage[stateName] = storedScope;
        },
		saveState : function() {
			if ($route.current === undefined || $route.current.locals === undefined || $route.current.locals.$scope === undefined)
				return;
			var scope = $route.current.locals.$scope;
			if (scope.getStateName === undefined)
				return;
            var stateName = scope.getStateName();

			// save off scope based on registered indexes
            angular.forEach(service.scopeStorage[stateName], function (value, key) {
            	service.scopeStorage[stateName][key] = scope[key];
            });
        }

	}
	$rootScope.$on("savestate", service.saveState);
	return service;
	}
)	

;
