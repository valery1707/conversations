package com.tipi.conversations.infrastructure.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tipi.conversations.domain.Conversation;
import com.tipi.conversations.domain.ConversationRepository;
import com.tipi.conversations.domain.Message;
import com.tipi.conversations.domain.Participant;
import com.tipi.conversations.domain.User;
import com.tipi.conversations.infrastructure.mongodb.serializers.*;
import org.bson.Document;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by @maximilientyc on 10/02/2016.
 */
public class MongoDbConversationRepository implements ConversationRepository {

	private final MongoCollection<Document> collection;
	private final ObjectMapper conversationObjectMapper;

	public MongoDbConversationRepository(MongoDatabase mongoDatabase) {
		this.collection = mongoDatabase.getCollection("conversations");

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		SimpleModule customSerializerModule = new SimpleModule();
		customSerializerModule.addSerializer(Conversation.class, new ConversationSerializer());
		customSerializerModule.addSerializer(Participant.class, new ParticipantSerializer());
		customSerializerModule.addSerializer(User.class, new UserSerializer());
		customSerializerModule.addSerializer(Message.class, new MessageSerializer());
		customSerializerModule.addDeserializer(Conversation.class, new ConversationDeserializer());
		customSerializerModule.addDeserializer(Participant.class, new ParticipantDeserializer());
		customSerializerModule.addDeserializer(User.class, new UserDeserializer());
		customSerializerModule.addDeserializer(Message.class, new MessageDeserializer());
		objectMapper.registerModule(customSerializerModule);

		this.conversationObjectMapper = objectMapper;
	}

	@Override
	public void add(Conversation conversation) {
		try {
			insertOneConversation(conversation);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private void insertOneConversation(Conversation conversation) throws JsonProcessingException {
		String conversationAsJson = conversationObjectMapper.writeValueAsString(conversation);
		Document document = Document.parse(conversationAsJson);
		collection.insertOne(document);
	}

	@Override
	public void update(Conversation conversation) {

	}

	@Override
	public boolean exists(Conversation conversation) {
		long conversationCount = collection.count(eq("conversationId", conversation.getConversationId()));
		return conversationCount > 0;
	}

	@Override
	public Conversation get(String conversationId) {
		if (conversationId == null) {
			throw new IllegalArgumentException("Conversation Id cannot be empty.");
		}
		try {
			return findOneConversation(conversationId);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Conversation findOneConversation(String conversationId) throws IOException {
		FindIterable<Document> documents = collection.find(eq("conversationId", conversationId));
		Document document = documents.first();
		return conversationObjectMapper.readValue(document.toJson(), Conversation.class);
	}

}