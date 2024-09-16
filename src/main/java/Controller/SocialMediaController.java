package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    UserService userService;
    
    public SocialMediaController() {
        this.userService = new UserService();
    }
    
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getUserMessages);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerUser(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account newAccount = userService.registerAccount(account);
        
        if (newAccount != null) context.json(mapper.writeValueAsString(newAccount));
        else context.status(400);
    }

    private void loginUser(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account newAccount = userService.loginToAccount(account);
        
        if (newAccount != null) context.json(mapper.writeValueAsString(newAccount));
        else context.status(401);
    }

    private void createMessage(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = userService.createMessage(message);
        
        if (newMessage != null) context.json(mapper.writeValueAsString(newMessage));
        else context.status(400);
    }

    private void getAllMessages(Context context) throws JsonProcessingException{
        List<Message> messages = userService.getAllMessages();
        context.json(messages);
    }

    

    private void getMessageByID(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
    
        // Fetch the message by ID using the service layer
        Message message = userService.getMessageByID(messageId);
            
        if (message != null) context.json(mapper.writeValueAsString(message));
        else context.status(200).result("");
    }

    private void deleteMessage(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
    
        // Fetch the message by ID using the service layer
        Message deletedMessage = userService.deleteMessage(messageId);
            
        if (deletedMessage != null) context.json(mapper.writeValueAsString(deletedMessage));
        else context.status(200).result("");
    }

    private void updateMessage(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        message.setMessage_id(messageId);

        // Fetch the message by ID using the service layer
        Message updatedMessage = userService.updateMessage(message);

        if (updatedMessage != null && !updatedMessage.message_text.toString().isEmpty()) 
            context.json(mapper.writeValueAsString(updatedMessage)); 
        else context.status(400);
    }

    private void getUserMessages(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int accountID = Integer.parseInt(context.pathParam("account_id"));

        List<Message> messages = userService.getAllUserMessages(accountID);
        if (messages == null) System.out.println("NULL MESSAGES FROM ACCOUNT: " + accountID);

        context.json(mapper.writeValueAsString(messages));
    }

}