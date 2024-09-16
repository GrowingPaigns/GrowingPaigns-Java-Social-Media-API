package Service;

import java.util.List;

import DAO.UserDAO;
import Model.Account;

import Model.Message;

public class UserService {
    public UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Account registerAccount(Account account) {
        Account newAccount = userDAO.getAccountByUsername(account.getUsername());
        if (newAccount != null) return null;

        return userDAO.registerNewAccount(account);
    }

    public Account loginToAccount(Account account) {
        Account createdAccount = userDAO.getAccountByLoginCred(account.getUsername(), account.getPassword());
        if (createdAccount == null) return null;

        return userDAO.loginToAccount(account);
    }

    public Message createMessage(Message message) {
        Message newMessage = userDAO.checkMessageValidity(message);
        if (newMessage == null) return null;

        return userDAO.createNewMessage(message);
    }

    public List<Message> getAllMessages() {
        List<Message> allMessages = userDAO.getAllMessages();
        return allMessages;
    }

    public List<Message> getAllUserMessages(int account_id) {
        List<Message> allMessages = userDAO.getAllUserMessages(account_id);
        return allMessages;
    }

    public Message getMessageByID(int messageID) {
        return userDAO.getMessageByID(messageID);
    }

    public Message deleteMessage(int messageID) {
        return userDAO.deleteMessage(messageID);
    }

    public Message updateMessage(Message message) {
        Message updatedMessage = userDAO.updateMessage(message);
        if (updatedMessage != null) return updatedMessage;
        return null;
    }
}
