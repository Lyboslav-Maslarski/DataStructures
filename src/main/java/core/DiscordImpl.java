package core;

import models.Message;

import java.util.*;
import java.util.stream.Collectors;

public class DiscordImpl implements Discord {
    Map<String, Message> messagesById;
    Map<String, Set<Message>> messagesByChannel;

    public DiscordImpl() {
        messagesById = new LinkedHashMap<>();
        messagesByChannel = new HashMap<>();
    }

    @Override
    public void sendMessage(Message message) {
        messagesById.put(message.getId(), message);
        messagesByChannel.putIfAbsent(message.getChannel(), new LinkedHashSet<>());
        messagesByChannel.get(message.getChannel()).add(message);
    }

    @Override
    public boolean contains(Message message) {
        return messagesById.containsKey(message.getId());
    }

    @Override
    public int size() {
        return messagesById.size();
    }

    @Override
    public Message getMessage(String messageId) {
        if (!messagesById.containsKey(messageId)) {
            throw new IllegalArgumentException();
        }
        return messagesById.get(messageId);
    }

    @Override
    public void deleteMessage(String messageId) {
        if (!messagesById.containsKey(messageId)) {
            throw new IllegalArgumentException();
        }
        Message toRemove = messagesById.remove(messageId);
        if (messagesByChannel.containsKey(toRemove.getChannel())) {
            messagesByChannel.get(toRemove.getChannel()).remove(toRemove);
        }
    }

    @Override
    public void reactToMessage(String messageId, String reaction) {
        if (!messagesById.containsKey(messageId)) {
            throw new IllegalArgumentException();
        }
        messagesById.get(messageId).getReactions().add(reaction);
    }

    @Override
    public Iterable<Message> getChannelMessages(String channel) {
        if (!messagesByChannel.containsKey(channel)) {
            throw new IllegalArgumentException();
        }
        return messagesByChannel.get(channel);
    }

    @Override
    public Iterable<Message> getMessagesByReactions(List<String> reactions) {
        return messagesById.values().stream()
                .filter(m -> new HashSet<>(m.getReactions()).containsAll(reactions))
                .sorted((l, r) -> {
                    if (l.getReactions().size() == r.getReactions().size()) {
                        return l.getTimestamp() - r.getTimestamp();
                    }
                    return r.getReactions().size() - l.getReactions().size();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getMessageInTimeRange(Integer lowerBound, Integer upperBound) {
        return messagesById.values().stream()
                .filter(m -> m.getTimestamp() >= lowerBound && m.getTimestamp() <= upperBound)
                .sorted((l, r) -> messagesByChannel.get(r.getChannel()).size() - messagesByChannel.get(l.getChannel()).size())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getTop3MostReactedMessages() {
        return messagesById.values().stream()
                .sorted((l, r) -> r.getReactions().size() - l.getReactions().size())
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getAllMessagesOrderedByCountOfReactionsThenByTimestampThenByLengthOfContent() {
        return messagesById.values().stream()
                .sorted((l, r) -> {
                    int cmp = r.getReactions().size() - l.getReactions().size();
                    if (cmp == 0) {
                        cmp = l.getTimestamp() - r.getTimestamp();
                        if (cmp == 0) {
                            cmp = l.getContent().length() - r.getContent().length();
                        }
                    }
                    return cmp;
                })
                .collect(Collectors.toList());
    }
}
