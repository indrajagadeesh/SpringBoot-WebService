package com.indrajagadeesh.webgradle.controller;

import com.indrajagadeesh.webgradle.model.User;
import com.indrajagadeesh.webgradle.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * controller to manage all UI endpoints.
 */
@Log
@Controller
public class UIController {

    @Autowired
    private UserService userService;

    /**
     * checks for the user session
     * if not present loads login page again
     * else redirects to home page
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/login")
    public void welcome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = null;
        if(request.getSession().getAttribute("username") == null){
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }else{
            response.sendRedirect(request.getContextPath()+"/");
        }
    }

    /**
     * verification user credentials with database
     * if valid redirects to home page
     * else reloads login page again
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @PostMapping("/loginSubmit")
    void authenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Optional<User> user = userService.getUserById(username);
        if(user.isPresent() && user.get().getPassword().equals(password)){
            //Invalidating session if any
            log.info("User:"+username+" logged in successfully");
            request.getSession().invalidate();
            HttpSession newSession = request.getSession(true);
            newSession.setMaxInactiveInterval(300);
            // adding username to session
            newSession.setAttribute("username", username);
            // adding user role to session
            newSession.setAttribute("role", user.get().getRole());
            response.sendRedirect(request.getContextPath()+"/");
        }else{
//            request.setAttribute("message", "Your username and password are invalid"); :TODO
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }

    /**
     * loads home page
     * this jsp page will check for sessionId, username in session
     * if not present, it will redirect to login page.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/")
    void getHome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/Home.jsp").forward(request, response);
    }

    /**
     * invalidates the session and redirects to login page
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/logout")
    void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        request.getSession().invalidate();
        log.info("User:"+username+" logged out successfully");
        response.sendRedirect(request.getContextPath()+"/login");
    }

    /**
     * only accessible to admin user role
     * provides list of user and there roles
     * admin can add, edit and delete user
     * @param request
     * @param response
     * @param model
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/admin")
    String usersPage(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        if(request.getSession().getAttribute("role") != null && Integer.parseInt(request.getSession().getAttribute("role").toString()) == 1){
            List<User> userList = userService.getAllUsers();
            model.addAttribute("userList", userList);
            return "admin_page";
        }else{
            return "redirect:/login";
        }
    }

    /**
     * user update page
     * checks for username in request
     * if present sets the existing user details
     * else provides empty input form for user
     * @param request
     * @param response
     * @param model
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/edit_user")
    String editUser(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        String username = request.getParameter("username");
        Optional<User> user = Optional.empty();
        if(username != null)
            user = userService.getUserById(username);
        if(user.isPresent()){
            model.addAttribute("user",user.get());
            return "user_update";
        }else {
            User user1 = new User("","",0);
            model.addAttribute("user",user1);
            return "user_update";
        }
    }

    /**
     * admin action to add or update user
     * @param request
     * @param response
     * @param model
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @PostMapping("/add_user")
    String addUser(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int role = Integer.parseInt(request.getParameter("role"));
        log.info("user: "+username+" added to database with role: "+(role==1?"Admin":"user"));
        User user = new User(username,password,role);
        userService.saveUser(user);
        log.info("user: "+username+" added/updated");
        return "redirect:/admin";
    }

    /**
     * admin action to delete user
     * @param request
     * @param response
     * @param model
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/delete_user")
    String deleteUser(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        String username = request.getParameter("username");
        Optional<User> user = Optional.empty();
        if(username != null){
            userService.deleteUserbyId(username);
            log.info("user: "+username+" deleted");
        }
        return "redirect:/admin";
    }
}
