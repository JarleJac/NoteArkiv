/**
 * 
 */
var CacheObject = function() {
	this.expires = new Date();
	this.obj = null;
};

CacheObject.prototype.addObj = function(timeToLive, obj) {
	this.expires = new Date();
	this.expires.setMilliseconds(this.expires.getMilliseconds() + timeToLive);
	this.obj = obj;
};
CacheObject.prototype.getObj = function() {
	return this.obj;
};
CacheObject.prototype.expire = function() {
	this.expires = new Date();
	this.obj = null;
};
CacheObject.prototype.isExpired = function() {
	return this.expires <= new Date();
};

angular.module('notearkiv').factory('Tags', function TagsFactory($q, $http) {

	var timeToLiveSeconds = 30;
	var tagsCache = new CacheObject();
	var rqInProgress = null;
	
	
	getTags = function() {
		var deferred = $q.defer();

		if (rqInProgress != null) {
			rqInProgress.then(function successCallback(result) {
				deferred.resolve(tagsCache.getObj());
			}, function errorCallback(result) {
				deferred.reject(result);
			});
		}
		else if (tagsCache.getObj() != null && !tagsCache.isExpired()) {
			deferred.resolve(tagsCache.getObj());
		} 
		else {
			rqInProgress = $http({method: 'GET', url : 'rest/noteservice/tags'});
			rqInProgress.then(function successCallback(result) {
				var tags = result.data;
				tagsCache.addObj(timeToLiveSeconds*1000,tags);
				rqInProgress = null;
				deferred.resolve(tags);
			}, function errorCallback(result) {
				rqInProgress = null;
				deferred.reject(result);
			});
		}
		
		return deferred.promise;
	};
	
	return {
		getAvailableTags : function(filterIdsString, include) {
			return getTags().then(function successCallback(result) {
				var resultArr = [];
				resultArr.push.apply(resultArr,result);
				filterIdsString = filterIdsString || null;
				include = include || false;
				if (filterIdsString === null && !include)
					return resultArr;
				
				var selectedIdArr = filterIdsString === null ? [] : filterIdsString.split(",");
				return resultArr.filter(function(tag) {
					var isSelected = selectedIdArr.includes(tag.id.toString());
					return (include && isSelected) || (!include && !isSelected);
				});
			});
		},
		addTag : function(tag) {
			tagsCache.expire();
			return $http({method: 'POST', url : 'rest/noteservice/tags', data: tag});
		},
		deleteTag : function(tag) {
			tagsCache.expire();
			return $http({method: 'DELETE', url : 'rest/noteservice/tags/' + tag.id});
		}
	}
}
)	
;
