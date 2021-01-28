package lk.crystal.configuration;


import lk.crystal.asset.user_management.user.entity.User;
import lk.crystal.asset.user_management.user.service.UserService;
import lk.crystal.asset.user_management.user_session_log.entity.UserSessionLog;
import lk.crystal.asset.user_management.user_session_log.entity.enums.UserSessionLogStatus;
import lk.crystal.asset.user_management.user_session_log.service.UserSessionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component( "customLogoutSuccessHandler" )
public class CustomLogoutSuccessHandler extends
        SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private UserSessionLogService userSessionLogService;
    @Autowired
    private UserService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        if ( authentication != null && authentication.getDetails() != null ) {
            try {
                //do some logic here if you want something to be done whenever
                User authUser =
                        userService.findByUserName(authentication.getName());
                UserSessionLog userSessionLog = new UserSessionLog();
                userSessionLog.setUser(authUser);
                userSessionLog.setUserSessionLogStatus(UserSessionLogStatus.LOGOUT);
                userSessionLog.setCreatedAt(LocalDateTime.now());
                userSessionLogService.persist(userSessionLog);


                request.getSession().invalidate();
                System.out.println("User Successfully Logout");
                //you can add more codes here when the user successfully logs out,
                //such as updating the database for last active.

            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        //redirect to login
        response.sendRedirect("/");
    }
}
