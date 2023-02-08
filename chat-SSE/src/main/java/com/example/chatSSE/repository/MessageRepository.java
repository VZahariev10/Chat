package com.example.chatSSE.repository;

import com.example.chatSSE.model.Message;
import com.example.chatSSE.model.User;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository implements MessageDAO {

    /**
     * An Object that helps us to run queries
     */
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    /**
     * A list where we keep pending messages
     */
    private final List<Message> messages;

    public MessageRepository(NamedParameterJdbcOperations namedParameterJdbcOperations,
                             List<Message> messages) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.messages = messages;
    }

    /**
     * Sql query to get sender from the database by id
     */
    @Override
    public User getSender(Integer id) {
        String sql = "SELECT * "
                + "   FROM users "
                + "   WHERE user_id = :user_id";

        return namedParameterJdbcOperations.queryForObject(sql,
                new MapSqlParameterSource("user_id", id),
                (rs, rowNum) -> {
                    User currentUser = new User();
                    currentUser.setUserId(rs.getInt("user_id"));
                    currentUser.setUserName(rs.getString("userName"));
                    currentUser.setPassword(rs.getString("password"));
                    currentUser.setIsOnline(rs.getInt("isOnline"));

                    return currentUser;
                });
    }

    /**
     * Sql query to get receiver from the database by id
     */
    @Override
    public User getReceiver(Integer id) {
        String sql = "SELECT * "
                + "   FROM users "
                + "   WHERE user_id = :user_id";

        return namedParameterJdbcOperations.queryForObject(sql,
                new MapSqlParameterSource("user_id", id),
                (rs, rowNum) -> {
                    User currentUser = new User();
                    currentUser.setUserId(rs.getInt("user_id"));
                    currentUser.setUserName(rs.getString("userName"));
                    currentUser.setPassword(rs.getString("password"));
                    currentUser.setIsOnline(rs.getInt("isOnline"));

                    return currentUser;
                });
    }

    /**
     * Sql query to delete pending messages from the database by user id
     */
    @Override
    public void deleteMessageFromPendingTable(Integer id) {
        String sql = "DELETE "
                + "   FROM PENDINGMESSAGES "
                + "   WHERE receiver_id = :user_id";

        namedParameterJdbcOperations.update(sql, new MapSqlParameterSource("user_id", id));
    }
    
    /**
     * Sql query to set the given user online
     */
    @Override
    public void setUserOnline(String username) {
        String sql = "UPDATE users "
                + "       SET isOnline = 1 "
                + "       WHERE userName = :username";

        namedParameterJdbcOperations.update(sql, new MapSqlParameterSource("username", username));
    }

    /**
     * Sql query to set the given user offline
     */
    @Override
    public void logOut(String username) {
        String sql = "UPDATE users "
                + "       SET isOnline = 0 "
                + "       WHERE userName = :username";

        namedParameterJdbcOperations.update(sql, new MapSqlParameterSource("username", username));
    }

    /**
     * Query to insert a message in the database when the receiver is offline
     */
    @Override
    public void updatePending(Message message) {
        String sql = "INSERT INTO pendingMessages(sender_id, receiver_id, message) "
                + "   VALUES (:sender_id, :receiver_id, :message)";

        try {
            namedParameterJdbcOperations.update(sql, new MapSqlParameterSource("sender_id", message.getSenderId())
                    .addValue("receiver_id", message.getReceiverId())
                    .addValue("message", message.getMessage()));
        } catch (NullPointerException ex) {
            ex.getMessage();
        }
    }

    /**
     * Query to find pending messages by receiver id
     */
    @Override
    public List<Message> findPendingMessagesById(Integer id) {
        String sql = "SELECT *"
                + "   FROM pendingMessages p"
                + "   WHERE p.receiver_id = :user_id";

        return namedParameterJdbcOperations.query(sql,
                new MapSqlParameterSource("user_id", id),
                (rs, rowNum) -> {
                    Message currentMessage = new Message();
                    currentMessage.setSenderId(rs.getInt("sender_id"));
                    currentMessage.setReceiverId(rs.getInt("receiver_id"));
                    currentMessage.setMessage(rs.getString("message"));
                    messages.add(currentMessage);
                    return currentMessage;
                });
    }
}