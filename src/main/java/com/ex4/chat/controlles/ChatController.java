package com.ex4.chat.controlles;

import com.ex4.chat.Constants;
import com.ex4.chat.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * This class is responsible of controlling the chat
 */
@Controller
public class ChatController {
    // the application context is injected via ctor
    private ApplicationContext context;

    @Autowired
    public ChatController(ApplicationContext c) {
        this.context = c;
    }

    /**
     * The function checks if the user is authenticated
     * @param model
     * @param session
     * @return AuthenticationResponse that helps us know if the user is already logged in
     */
    private ObjectResponse checkAuth(Model model, HttpSession session) {
        ObjectResponse res = new ObjectResponse();
        AuthenticationResponse auth = new AuthController(this.context).isAuthenticated(model, session);
        if(!auth.getIsAuthenticated()) {
            res.isError = true;
            res.errorMessage = Constants.NotAuthenticatedMsg;
        }
        return res;
    }

    /**
     * The function checks if the param is valid
     * @param result
     * @return a response by the result that we got
     */
    private ObjectResponse checkBindingResult(BindingResult result) {
        ObjectResponse res = new ObjectResponse();
        if (result.hasErrors()) {
            res.isError = true;
            res.errorMessage = Constants.InvalidInputMsg;
            return res;
        }
        return res;
    }

    /**
     * The function checks if string is not empty or null
     * @param str
     * @return check if a chat message is not empty
     */
    private ObjectResponse checkNotEmpty(String str) {
        ObjectResponse res = new ObjectResponse();
        if(str == null || str.trim().equals("")) {
            res.isError = true;
            res.errorMessage = Constants.InvalidInputMsg;
            return res;
        }
        return res;
    }

    /**
     * Returns ChatMessagesRepository from the bean
     * @return return repository
     */
    private ChatMessagesRepository getRepo() {
        return this.context.getBean(ChatMessagesRepository.class);
    }

    /**
     * Returns OnlineUsers repo form the bean
     * @return online users
     */
    private OnlineUsers getOnlineUsers() { return this.context.getBean(OnlineUsers.class); }

    /**
     * The function checks if the message is valid and insert it to the database
     * If the user is authenticated
     * @param message
     * @param result
     * @param model
     * @param session
     * @return A response with a json file contains the last message
     */
    @PostMapping("/api/sendMessage")
    public @ResponseBody
    ObjectResponse sendMessage(@Valid ChatMessage message, BindingResult result, Model model, HttpSession session) {
        ObjectResponse res = new ObjectResponse();

        do {
            res = checkAuth(model, session);
            if(res.isError) {
                break;
            }

            res = checkBindingResult(result);
            if(res.isError) {
                break;
            }

            String userName = (String) session.getAttribute(Constants.AuthSessionAttribute);
            message.setUserName(userName);
            ChatMessagesRepository repo = getRepo();
            repo.save(message);

            long messageId = (long) session.getAttribute(Constants.MessageIdSessionAttribute);
            res.object = getRepo().findByIdGreaterThanEqualOrderByIdAsc(messageId);
        } while(false);
        return res;
    }

    /**
     * The function returns all the message from the message id in the session
     * If the user is authenticated
     * @param model
     * @param session
     * @return all messages
     */
    @GetMapping("/api/getMessages")
    public @ResponseBody
    ObjectResponse getMessages(Model model, HttpSession session) {
        ObjectResponse res = new ObjectResponse();

        do {
            res = checkAuth(model, session);
            if(res.isError) {
                break;
            }

            long messageId = (long) session.getAttribute(Constants.MessageIdSessionAttribute);
            res.object = getRepo().findByIdGreaterThanEqualOrderByIdAsc(messageId);
        } while(false);

        return res;
    }

    /**
     * The function returns the list of the messages that sent by the user from the request param
     * If the user is authenticated
     * @param userName
     * @param model
     * @param session
     * @return Message that was writen by the user name
     */
    @GetMapping("/api/searchByUserName")
    public @ResponseBody
    ObjectResponse searchByUserName(@RequestParam("userName") String userName, Model model, HttpSession session) {
        ObjectResponse res = new ObjectResponse();

        do {
            res = checkAuth(model, session);
            if(res.isError) {
                break;
            }

            res = checkNotEmpty(userName);
            if(res.isError) {
                break;
            }

            res.object = getRepo().findByUserNameOrderByIdAsc(userName);
        } while(false);

        return res;
    }

    /**
     * The function returns the list of the messages that contains the message form the request param
     * If the user is authenticated
     * @param message
     * @param model
     * @param session
     * @return messages that contain text from the message param.
     */
    @GetMapping("/api/searchByMessage")
    public @ResponseBody
    ObjectResponse searchByMessage(@RequestParam("message") String message, Model model, HttpSession session) {
        ObjectResponse res = new ObjectResponse();

        do {
            res = checkAuth(model, session);
            if(res.isError) {
                break;
            }

            res = checkNotEmpty(message);
            if(res.isError) {
                break;
            }

            res.object = getRepo().findByMessageContainingIgnoreCaseOrderByIdAsc(message);
        } while(false);

        return res;
    }

    /**
     * The function returns the list of the online users
     * If the user is authenticated
     * @param model
     * @param session
     * @return online users
     */
    @GetMapping("/api/getOnlineUsers")
    public @ResponseBody
    ObjectResponse getOnlineUsers(Model model, HttpSession session) {
        ObjectResponse res = new ObjectResponse();

        do {
            res = checkAuth(model, session);
            if(res.isError) {
                break;
            }

            String userName = (String) session.getAttribute(Constants.AuthSessionAttribute);
            this.getOnlineUsers().onlinePing(session.getId(), userName);
            res.object = this.getOnlineUsers().getList();
        } while(false);
        return res;
    }
}
