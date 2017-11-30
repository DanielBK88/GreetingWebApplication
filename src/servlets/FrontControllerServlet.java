package servlets;

import entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;


/**
 * The front controller servlet of the application.
 * All user requests are passed to this servlet to be redirected to one of the 3 JSPs.
 */

@WebServlet(name = "FrontControllerServlet", urlPatterns = { "/Start" })
public class FrontControllerServlet extends HttpServlet {

    private FrontControllerHelper helper = FrontControllerHelper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        response.setCharacterEncoding("UTF-8");

        // Look up the request parameters
        String signUp = request.getParameter("signUp");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String newUserName = request.getParameter("newUserName");
        String newPassword = request.getParameter("newPassword");
        String newPasswordConfirmation = request.getParameter("newPasswordConfirmation");
        String logout = request.getParameter("logout");

        // If the request parameters userName and password exist, check for correctness and either redirect to SignIn with the request attribute "signinError"
        // or set the session attribute "user" and redirect to welcome page.
        if((userName != null) && (password != null)){
            System.out.println("The request has the userName and password parameters. Checking for correctness ...");
            User user = helper.checkSignInData(userName, password);
            if(user == null){
                System.out.println("UserName and/or password are wrong. Redirecting to sign-in page with the request attribute signinError");
                request.setAttribute("signinError", "Имя пользователя и пароль не подходят!");
                request.getRequestDispatcher("SignIn.jsp").forward(request, response);
            }
            else{
                System.out.println("Username and password are correct. Setting the session user-attribute and redirecting to welcome page");
                session.setAttribute("user", user);
                request.setAttribute("priorIpToDisplay", user.getLastLoginIp());
                request.setAttribute("viewsCount", helper.increaseAndGetViewsCount());
                user.setLastLoginIp(request.getRemoteAddr());
                helper.updateUser(user);
                request.getRequestDispatcher("Greeting.jsp").forward(request, response);
            }
        }

        /**
         * If the request parameters newUserName and newPassword exist:
         * First check if userName and password are ok according to the requirements to a valid username and password.
         * If no, set the requestAttribute "signup_error" to the value of a String telling what exactly is not ok and redirect to signUp again.
         * If yes, check if a user with this username already exists in the database.
         * If no, create a new user, set the session's user attribute and redirect to welcome page.
         * If yes, set the request attribute "usernameAlreadyExists" and redirect to signUp page.
         */
        else if((newUserName != null) && (newPassword != null) && (newPasswordConfirmation != null)){
            System.out.println("The request has the newUserName, newPassword and newPasswordConfirmation parameters. Checking if they are matching the rules.");
            String message = helper.userNameAndPasswordMatchTheRequirements(newUserName, newPassword, newPasswordConfirmation);
            if(message == null){
                System.out.println("Username and password do match the rules. Now checking if a user with this name already exists");
                User newUser = helper.createNewUser(newUserName, newPassword, request.getRemoteAddr());
                if(newUser == null){
                    System.out.println("A user with this name already exists. Redirecting to sign-up page with the request attribute usernameAlreadyExists");
                    request.setAttribute("signup_error", "Пользователь с таким именем уже зарегистрирован.");
                    request.getRequestDispatcher("SignUp.jsp").forward(request, response);
                }
                else{
                    System.out.println("There is no user with this name so far. Setting the session's user attribute to the new created user and redirecting to welcome page.");
                    session.setAttribute("user", newUser);
                    request.setAttribute("priorIpToDisplay", "Никакого, поскольку вы только что зарегистрировались :)");
                    request.setAttribute("viewsCount", helper.increaseAndGetViewsCount());
                    request.getRequestDispatcher("Greeting.jsp").forward(request, response);
                }
            }
            else{
                System.out.println("Username and password do NOT match the rules. Setting the request attribute signup_error and redirecting to SignIn");
                request.setAttribute("signup_error", message);
                request.getRequestDispatcher("SignUp.jsp").forward(request, response);
            }
        }

        // Check if the request parameter "signUp" exists.
        // If so, redirect to sign-up page
        else if(signUp != null){
            System.out.println("The request has the signUp parameter. Redirecting to sign-up page.");
            request.getRequestDispatcher("SignUp.jsp").forward(request, response);
        }

        // If The logout parameter exists, delete the session's user-attribute and redirect to sign in.
        else if(logout != null){
            System.out.println("The request has the logout parameter. Deleting the session's user attribute and redirecting to sign in.");
            session.removeAttribute("user");
            request.setAttribute("signinInfo", "Вы вышли из приложения.");
            request.getRequestDispatcher("SignIn.jsp").forward(request, response);
        }

        // Finally check if the session already has the user attribute.
        // In this case redirect to welcome page, otherwise redirect to sign in page.
        else{
            Object userAttribute = session.getAttribute("user");
            if(userAttribute != null){
                System.out.println("The session already has the user attribute. Redirecting to welcome page.");
                User user = (User) userAttribute;
                request.setAttribute("priorIpToDisplay", user.getLastLoginIp());
                request.setAttribute("viewsCount", helper.increaseAndGetViewsCount());
                user.setLastLoginIp(request.getRemoteAddr());
                helper.updateUser(user);
                request.getRequestDispatcher("Greeting.jsp").forward(request, response);
            }
            else{
                System.out.println("No session-user-attribute is defined and no request parameters exist. Redirecting to SignIn.");
                request.getRequestDispatcher("SignIn.jsp").forward(request, response);
            }
        }
    }


}
