package com.tipi.conversations.domain;

import java.util.Date;
import java.util.Objects;

/**
 * Created by @maximilientyc on 10/01/2016.
 */
public class Message {

	private final String messageId;
	private final Date postedOn;
	private Participant postedBy;
	private String conversationId;
	private String content;

	public Message(String messageId, Date postedOn) {
		this.messageId = messageId;
		this.postedOn = postedOn;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Message setConversationId(String conversationId) {
		this.conversationId = conversationId;
		return this;
	}

	public String getContent() {
		return content;
	}

	public Message setContent(String content) {
		this.content = content;
		return this;
	}

	public Participant getPostedBy() {
		return postedBy;
	}

	public Message setPostedBy(Participant postedBy) {
		this.postedBy = postedBy;
		return this;
	}

	public Date getPostedOn() {
		return postedOn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Message message = (Message) o;
		return Objects.equals(messageId, message.messageId) &&
				Objects.equals(postedOn, message.postedOn) &&
				Objects.equals(content, message.content) &&
				Objects.equals(postedBy, message.postedBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId, postedOn, content, postedBy);
	}


}
