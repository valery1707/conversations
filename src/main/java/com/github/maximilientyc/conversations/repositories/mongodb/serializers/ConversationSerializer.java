package com.github.maximilientyc.conversations.repositories.mongodb.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.maximilientyc.conversations.domain.Conversation;

import java.io.IOException;

/**
 * Created by @maximilientyc on 14/02/2016.
 */
public class ConversationSerializer extends JsonSerializer<Conversation> {

	@Override
	public void serialize(Conversation conversation, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("conversationId", conversation.getConversationId());
		jsonGenerator.writeObjectField("participants", conversation.getParticipants());
		jsonGenerator.writeEndObject();
	}
}