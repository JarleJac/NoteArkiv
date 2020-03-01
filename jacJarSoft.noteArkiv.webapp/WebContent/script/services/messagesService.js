/**
 * 
 */

angular.module('notearkiv').factory('Messages', function MessagesFactory($q, $http) {
	var MessagteTypeTextMap = {
			Normal: "Vanlig",
			Important: "Viktig"
		};
	
	
	return {
		getMessages : function() {
			return $http({method: 'GET', url : 'rest/messages/message'})
			.then(function successCallback(result) {
				var messages = result.data.map(function(message) {
					var messageData = {message : message, messageTypeText : MessagteTypeTextMap[message.messageType]};
					return messageData;
				});
				return messages;
			}, function errorCallback(result) {
				throw result;
			});
		},
		getMessage : function(id) {
			return $http({method: 'GET', url : 'rest/messages/message/' + id });
		},
		addMessage : function(message) {
			return $http({method: 'POST', url : 'rest/messages/message', data: message});
		},
		updateMessage : function(message) {
			return $http({method: 'PUT', url : 'rest/messages/message', data: message});
		},
		deleteUser : function(user) {
			return $http({method: 'DELETE', url : 'rest/userservice/user/' + user.no});
		},
		getMessageTypeText : function(messageType) {
			return MessagteTypeTextMap[messageType];
		}
	}
}
)	
;
