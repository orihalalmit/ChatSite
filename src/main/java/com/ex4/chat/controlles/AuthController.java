package com.ex4.chat.controlles;

import com.ex4.chat.models.AuthenticationResponse;
import com.ex4.chat.Constants;
import com.ex4.chat.models.ChatMessage;
import com.ex4.chat.models.ChatMessagesRepository;
import com.ex4.chat.models.OnlineUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;


/**
 * AuthController is a class that checks if the user is logged in are already logged in.
 */
@Controller
public class AuthController {

    // the application context is injected via ctor
    private ApplicationContext context;

    /**
     * Contructor that gets the context
     * @param c context
     */
    @Autowired
    public AuthController(ApplicationContext c) {
        this.context = c;
    }

    /**
     * Returns ChatMessagesRepository from the bean
     * @return the repository
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
     * Returns authResponse that indicates if the user is authenticated
     * @param model
     * @param session
     * @return AuthenticationResponse that helps us know if the user is already logged in
     */
    @GetMapping("/api/isAuthenticated")
    public @ResponseBody
    AuthenticationResponse isAuthenticated(Model model, HttpSession session) {
        AuthenticationResponse auth = new AuthenticationResponse();
        String userName = (String) session.getAttribute(Constants.AuthSessionAttribute);

        if(userName != null) {
            auth.setUserName(userName);
            auth.setIsAuthenticated(true);
        }

        return auth;
    }

    /**
     * The function gets the username checks if the username is valid if do returns AuthenticationResponse with true
     * isAutenticated else returns false
     * The function save the username in the session also save the third massage is from the end
     * @param userName
     * @param model
     * @param session
     * @return AuthenticationResponse after loggin in the user
     */
    @PostMapping("/api/login")
    public @ResponseBody
    AuthenticationResponse login(@RequestParam("userName") String userName, Model model, HttpSession session) {
        AuthenticationResponse auth = new AuthenticationResponse();

        if(userName != null && !userName.trim().equals("")) {
            auth.setUserName(userName);
            auth.setIsAuthenticated(true);
            session.setAttribute(Constants.AuthSessionAttribute, userName);

            List<ChatMessage> cm = getRepo().findFirst3ByOrderByIdDesc();
            long messageId = 0;
            if(cm.size() > 0) {
                Collections.reverse(cm);
                messageId = cm.get(0).getId();
            }
            session.setAttribute(Constants.MessageIdSessionAttribute, messageId);
        }

        return auth;
    }

    /**
     * The function invalidate the session and remove the user from the online users
     * @param model
     * @param session
     * @return an empty AuthenticationResponse that helps us know that the user was logged out
     */
    @GetMapping("/api/logout")
    public @ResponseBody
    AuthenticationResponse logout(Model model, HttpSession session) {
        this.getOnlineUsers().remove(session.getId());
        session.invalidate();
        return new AuthenticationResponse();
    }
}
