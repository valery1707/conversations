package com.github.maximilientyc.conversations.domain;

import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.infrastructure.searches.PaginatedList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class SampleConversationRepository implements ConversationRepository {

	private List<Conversation> conversationList = new ArrayList<>();

	@Override
	public void add(Conversation conversation) {
		conversationList.add(conversation);
	}

	@Override
	public void update(Conversation conversation) {
		String conversationId = conversation.getConversationId();
		List<Conversation> toBeRemovedConversations = new ArrayList<Conversation>();
		List<Conversation> toBeAddedConversations = new ArrayList<Conversation>();
		for (Conversation conversation1 : conversationList) {
			if (conversation.getConversationId().equals(conversation1.getConversationId())) {
				toBeRemovedConversations.remove(conversation1);
				toBeAddedConversations.add(conversation);
			}
		}
		for (Conversation conversation1 : toBeRemovedConversations) {
			conversationList.remove(conversation1);
		}
		for (Conversation conversation1 : toBeAddedConversations) {
			conversationList.add(conversation1);
		}
	}

	@Override
	public boolean exists(String conversationId) {
		for (Conversation conversation : conversationList) {
			if (conversation.getConversationId().equals(conversationId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Conversation get(String conversationId) {
		if (conversationId == null) {
			throw new IllegalArgumentException("Conversation Id cannot be empty.");
		}
		for (Conversation conversation : conversationList) {
			if (conversation.getConversationId().equals(conversationId)) {
				return conversation;
			}
		}
		return null;
	}

	@Override
	public PaginatedList<Conversation> find(ConversationSearchCriteria conversationSearchCriteria) {
		List<Conversation> foundConversationList = new ArrayList<Conversation>();
		for (Conversation conversation : conversationList) {
			if (conversation.containsParticipant(conversationSearchCriteria.getUserId())) {
				foundConversationList.add(conversation);
			}
		}
		PaginatedList<Conversation> conversationPaginatedList = new PaginatedList<>(foundConversationList.size(), foundConversationList);
		return conversationPaginatedList;
	}

	@Override
	public long count(ConversationSearchCriteria criteria) {
		return conversationList.size();
	}
}
