package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class UserDAO {
    
    public Account getAccountByID(int account_id) {
        Connection connect = ConnectionUtil.getConnection();
        try {
            String request = "Select * from account where account.account_id = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    public Account getAccountByUsername(String user) {
        Connection connect = ConnectionUtil.getConnection();
        try {
            String request = "Select * from account where account.username = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setString(1, user);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    public Account getAccountByLoginCred(String user, String pass) {
        Connection connect = ConnectionUtil.getConnection();
        try {
            String request = "Select * from account where account.username = ? and account.password = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    public Account registerNewAccount(Account newAccount) 
    {
        Connection connect =  ConnectionUtil.getConnection();
        try {
            String request = "Insert into account(username, password) values(?,?)";
            PreparedStatement ps = connect.prepareStatement(request);
            if (newAccount.username.length() > 0 && newAccount.password.length() >= 4) {
                ps.setString(1, newAccount.username);
                ps.setString(2, newAccount.password);
            }
            ps.executeUpdate();
            Account updatedAccount = getAccountByUsername(newAccount.username);
            return updatedAccount;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }

    public Account loginToAccount(Account account) {
        Connection connect = ConnectionUtil.getConnection();
        try {
            String request = "Select * from account where account.username = ? and account.password = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setString(1, account.username);
            ps.setString(2, account.password);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account loggedInAccount = new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
                System.out.println(loggedInAccount.toString());
                return loggedInAccount;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    public Message checkMessageValidity(Message message) {
        Connection connect = ConnectionUtil.getConnection();
        try {
            String request = "Select * from message inner join account on message.posted_by = account.account_id where account.account_id = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setInt(1, message.posted_by);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message checkedMessage = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                return checkedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    private Message getMessageByText(Message message) {
        Connection connect = ConnectionUtil.getConnection();
        try {
            String request = "Select * from message where message.message_text = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setString(1, message.message_text);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message returnedMessage = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                return returnedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    public Message getMessages(Message message) {
        Connection connect = ConnectionUtil.getConnection();
        try {
            String request = "Select * from message";
            Statement statement = connect.createStatement(); 

            ResultSet rs = statement.executeQuery(request);
            while(rs.next()){
                Message returnedMessage = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                return returnedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    public Message createNewMessage(Message newMessage) 
    {
        Connection connect =  ConnectionUtil.getConnection();
        try {
            String request = "Insert into message(posted_by, message_text, time_posted_epoch) values(?,?,?)";
            PreparedStatement ps = connect.prepareStatement(request);
            if (newMessage.message_text.length() <= 255 
            && newMessage.message_text.length() > 0) {
                ps.setInt(1, newMessage.posted_by);
                ps.setString(2, newMessage.message_text.toString());
                ps.setLong(3, newMessage.time_posted_epoch);
            }
            ps.executeUpdate();
            Message postedMessage = getMessageByText(newMessage);
            return postedMessage;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }

    public List<Message> getAllMessages() 
    {
        Connection connect =  ConnectionUtil.getConnection();
        try {
            List<Message> messages = new ArrayList<>();
            String request = "Select * from message";
            PreparedStatement ps = connect.prepareStatement(request);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }

    public List<Message> getAllUserMessages(int account_id) 
    {
        Connection connect =  ConnectionUtil.getConnection();
        try {
            List<Message> messages = new ArrayList<>();
            String request = "Select * from message inner join account on account_id where account.account_id = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setInt(1, account_id);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }


    public Message getMessageByID(int id) 
    {
        Connection connect =  ConnectionUtil.getConnection();
        try {
            String request = "Select * from message where message.message_id = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }

    public Message deleteMessage(int id) 
    {
        Connection connect =  ConnectionUtil.getConnection();
        try {
            Message deletedMessage = getMessageByID(id);
            String request = "Delete from message where message.message_id = ?";
            PreparedStatement ps = connect.prepareStatement(request);

            ps.setInt(1, id);
            ps.executeUpdate();
            
            return deletedMessage;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }

    public Message updateMessage(Message message) 
    {
        Connection connect =  ConnectionUtil.getConnection();
        try {
            
            String request = "Update message set message_text = ? where message.message_id = ?";
            PreparedStatement ps = connect.prepareStatement(request);
            ps.setString(1, message.message_text);
            ps.setInt(2, message.message_id);
            ps.executeUpdate();

            return getMessageByID(message.message_id); // Retrieve the updated message
                
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }
}
