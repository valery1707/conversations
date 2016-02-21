package com.tipi.conversations.domain.conversations;

import java.util.Date;

/**
 * Created by @maximilientyc on 10/01/2016.
 */
public class MessageFactory {

	private final ConversationService conversationService;

	public MessageFactory(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	public Message buildMessage() {
		Date postedOn = new Date();
		String messageId = conversationService.getNextMessageId();
		return new Message(messageId, postedOn);
	}
}
