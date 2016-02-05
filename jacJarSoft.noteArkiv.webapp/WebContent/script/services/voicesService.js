/**
 * 
 */
angular.module('notearkiv').factory('Voices', function VoicesFactory($q, $http) {

	var voices = null;
	
	getVoices = function() {
		var deferred = $q.defer();

		if (voices != null) {
			deferred.resolve(voices);
		} 
		else {
			$http({method: 'GET', url : 'rest/noteservice/voice'})
			.then(function successCallback(result) {
				voices = result.data;
				deferred.resolve(voices);
			}, function errorCallback(result) {
				deferred.reject(result);
			});
		}
		
		return deferred.promise;
	};
	
	return {
		getSelectedVoices : function(voicesIdString) {
			return getVoices().then(function successCallback(result) {
				var retVoices = [];
				var selectedIdArr = voicesIdString == null ? [] :  voicesIdString.split(",");
				result.forEach(function(voice) {
					var isSelected = selectedIdArr.includes(voice.id.toString());
					var newVoice = jQuery.extend({selected: isSelected}, voice);
					retVoices.push(newVoice);
				});
				return retVoices; 
			});
		},
		getSelectedAsIdString: function(selectedVoices) {
			return selectedVoices
			.filter(function(voice) {return voice.selected;})
			.map(function(voice) {return voice.id}).join();
		}
	}
}
)	
;
