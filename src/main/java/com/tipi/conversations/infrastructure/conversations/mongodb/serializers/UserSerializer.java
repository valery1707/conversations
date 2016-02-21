package com.tipi.conversations.infrastructure.conversations.mongodb.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tipi.conversations.domain.users.User;

import java.io.IOException;

/**
 * Created by @maximilientyc on 19/02/2016.
 */
public class UserSerializer extends JsonSerializer<User> {

	@Override
	public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeObjectField("userId", user.getUserId());
		jsonGenerator.writeObjectField("firstName", user.getFirstName());
		jsonGenerator.writeObjectField("lastName", user.getLastName());
		jsonGenerator.writeEndObject();
	}
}
