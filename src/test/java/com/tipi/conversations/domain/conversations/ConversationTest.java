package com.tipi.conversations.domain.conversations;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class ConversationTest {

	private ConversationService conversationService;
	private ConversationFactory conversationFactory;
	private MessageFactory messageFactory;
	private ParticipantFactory participantFactory;

	@Rule
	public ExpectedException expectedException;

	public ConversationTest() {
		conversationService = new ConversationService();
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		participantFactory = new ParticipantFactory(conversationService);
		expectedException = ExpectedException.none();
	}

	@Test
	public void should_contain_at_least_two_participants_when_new() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");

		// when
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// then
		assertThat(conversation.countParticipants()).isGreaterThanOrEqualTo(2);
	}

	@Test
	public void should_not_contain_messages_when_new() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// then
		assertThat(conversation.countMessages()).isEqualTo(0);
	}

	@Test
	public void should_contain_one_message() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message message = messageFactory.buildMessage().setContent("This is the message content !").setPostedBy(maximilien);
		conversation.postMessage(message);

		// then
		assertThat(conversation.countMessages()).isEqualTo(1);
	}

	@Test
	public void should_contain_two_messages() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("This is the first message content !").setPostedBy(maximilien);
		conversation.postMessage(firstMessage);
		Message secondMessage = messageFactory.buildMessage().setContent("This is the second message content !").setPostedBy(bob);
		conversation.postMessage(secondMessage);

		// then
		assertThat(conversation.countMessages()).isEqualTo(2);
	}

	@Test
	public void should_contain_a_message_posted_by_maximilien() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("This is the first message content !").setPostedBy(maximilien);
		conversation.postMessage(firstMessage);

		// then
		Message message = conversation.getMessage(firstMessage.getMessageId());
		assertThat(message.postedBy().getName()).isEqualTo("maximilien");
	}

	@Test
	public void should_return_an_error_when_a_participant_leaves_a_two_participants_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot leave conversation, reason: not enough participants.");

		// when
		maximilien.leaveConversation(conversation);
	}

	@Test
	public void should_contain_two_participants_when_a_participant_leaves_a_three_participant_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Participant alice = participantFactory.buildParticipant().setName("alice");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob)
				.addParticipant(alice);

		// when
		maximilien.leaveConversation(conversation);

		// then
		assertThat(conversation.countParticipants()).isEqualTo(2);
	}

	@Test
	public void should_return_an_error_when_a_participant_post_a_message_in_the_wrong_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Participant alice = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot post message, reason: " + alice.getName() + " is not a participant.");

		// when
		Message message = messageFactory.buildMessage().setContent("Hey eveyone, please go to http://viagra.com/12123.js").setPostedBy(alice);
		conversation.postMessage(message);
	}

}
