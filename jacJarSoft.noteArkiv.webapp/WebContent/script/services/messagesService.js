/**
 * 
 */

angular.module('notearkiv').factory('Messages', function MessagesFactory($q, $http, $sce) {
	var MessagteTypeTextMap = {
			Normal: "Vanlig",
			Important: "Viktig"
		};
	var getHtmlMessage = function(msg) {
		//return $sce.trustAsHtml("<p>" + msg.replace("\n","</p><p>") + "</p>");
		return $sce.trustAsHtml(msg.replace(/\n/g,"</br>"));
	}
	var getMessagesInternal = function(onlyActive) {
		return $http({method: 'GET', url : 'rest/messages/message'})
		.then(function successCallback(result) {
			var messages = result.data.map(function(message) {
				var messageData = {	
						message : message, 
						messageTypeText : MessagteTypeTextMap[message.messageType], 
						expiredText : message.expired ? "Ja" : "Nei",
						htmlMessage : getHtmlMessage(message.message)
					};
				return messageData;
			});
			if (onlyActive)
				messages = messages.filter(function(messageData) {
					return !messageData.message.expired;
				}); 
			return messages;
		}, function errorCallback(result) {
			throw result;
		});
	};
 
	
	return {
		getMessages : function() {
			return getMessagesInternal(false);
		},
		getActiveMessages : function() {
			return getMessagesInternal(true);
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
		deleteMessage : function(message) {
			return $http({method: 'DELETE', url : 'rest/messages/message/' + message.id});
		},
		getMessageTypeText : function(messageType) {
			return MessagteTypeTextMap[messageType];
		}
	}
}
)	
;
