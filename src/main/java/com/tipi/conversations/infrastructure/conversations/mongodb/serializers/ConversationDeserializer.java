package com.tipi.conversations.infrastructure.conversations.mongodb.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipi.conversations.domain.conversations.Conversation;
import com.tipi.conversations.domain.conversations.Participant;

import java.io.IOException;

/**
 * Created by @maximilientyc on 14/02/2016.
 */
public class ConversationDeserializer extends JsonDeserializer<Conversation> {

	@Override
	public Conversation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		String conversationId = node.get("conversationId").textValue();
		Conversation conversation = new Conversation(conversationId);

		JsonNode participantsNode = node.get("participants");
		for (JsonNode participantNode : participantsNode) {
			Participant participant = participantNode.traverse(jsonParser.getCodec()).readValueAs(Participant.class);
			conversation.addParticipant(participant);
		}

		return conversation;
	}
}
