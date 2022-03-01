package com.taskmanager.upper.controller;

import com.taskmanager.upper.entity.*;
import com.taskmanager.upper.service.AnnouncementService;
import com.taskmanager.upper.service.TaskService;
import com.taskmanager.upper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/")
    public String userIndex(Model model){
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        model.addAttribute("announcements", announcements);
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/userIndex";
    }

    @GetMapping("/announcement/readAnnouncement/{id}")
    public String readAnnouncement(Model model, @PathVariable(name="id") Long id){
        Announcement announcement = announcementService.getAnnouncement(id);
        model.addAttribute("announcement", announcement);
        model.addAttribute("currentUser", getCurrentUser());
        return "announcement/readAnnouncement";
    }

    @GetMapping("/admin/userList")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getUserList(Model model){

        List<AuthUsers> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());

        return "users/userList";
    }

    @GetMapping("/user/signIn")
    public String signIn(Model model){

        model.addAttribute("currentUser", getCurrentUser());
        return "/users/signIn";
    }
    @GetMapping("/user/profile")
    @PreAuthorize("isAuthenticated()")
    public String getProfile(Model model){

        model.addAttribute("currentUser", getCurrentUser());
        return "users/profile";
    }

    @GetMapping("/user/notification")
    @PreAuthorize("isAuthenticated()")
    public String displayNotification(Model model){

        List<Tasks> tasks = taskService.getTasksByUser(getCurrentUser().getId());

        int taskNum = 0;
        if(tasks!=null){
            taskNum = tasks.size();
            model.addAttribute("taskNum", taskNum);
        }
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/notification";
    }

    @GetMapping("/admin/adminPanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public String adminPanel(Model model){

        model.addAttribute("currentUser", getCurrentUser());
        return "/users/adminPanel";
    }

    @GetMapping("/moderator/moderatorPanel")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_USER')")
    public String moderatorPanel(Model model){

        model.addAttribute("currentUser", getCurrentUser());
        return "/users/moderatorPanel";
    }

    @GetMapping("/user/userPanel")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String userPanel(Model model){

        List<Tasks> tasks = taskService.getTasksByUser(getCurrentUser().getId());

        int taskNum = 0;
        if(tasks!=null){
            taskNum = tasks.size();
            model.addAttribute("taskNum", taskNum);

            model.addAttribute("tasks", tasks);
            System.out.println(tasks.size() + "your id is: "+getCurrentUser().getId());
        }

        model.addAttribute("currentUser", getCurrentUser());
        return "/users/userPanel";
    }



    @GetMapping("/accessError")
    public String accessError(Model model){

        model.addAttribute("currentUser", getCurrentUser());
        return "/users/accessDenied";
    }

    @PostMapping("/user/addUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addUser(@RequestParam(name="user_email") String email,
                          @RequestParam(name="user_password") String password,
                          @RequestParam(name="user_re_password") String re_password,
                          @RequestParam(name="fullName") String fullName, Model model){

        if(password.equals(re_password)) {
            AuthUsers user = new AuthUsers();
            user.setEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setFullName(fullName);

            if(userService.saveUser(user)!=null){
                return "redirect:/user/addUser?success";
            }

        }

        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/user/addUser?error";
    }


    @PostMapping("/user/updateProfile")
    @PreAuthorize("isAuthenticated()")
    public String updateUserProfile(@RequestParam(name="fullName") String fullName,
                                    @RequestParam(name="oldPassword") String oldPassword,
                                    @RequestParam(name="newPassword") String newPassword,
                                    @RequestParam(name="reNewPassword") String reNewPassword, Model model){

        if(newPassword.equals(reNewPassword)) {
            AuthUsers currentUser = getCurrentUser();
           if(bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())){
               currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
               currentUser.setFullName(fullName);
               userService.saveUser(currentUser);
           }
        }

        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/user/profile";
    }


    @GetMapping("/moderator/addUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addUser(Model model){

        model.addAttribute("currentUser", getCurrentUser());
        return "/users/addUser";
    }

    private AuthUsers getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            AuthUsers authUser = (AuthUsers) authentication.getPrincipal();
            return authUser;
        }

        return null;
    }

    @GetMapping("/admin/user/userDetail/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String userDetail(@PathVariable(name = "ids") Long id, Model model){

        AuthUsers user = userService.findUser(id);
        model.addAttribute("user", user);
        List<AuthRoles> roles = userService.getAllRoles();
        model.addAttribute("roleList", roles);
        model.addAttribute("currentUser", getCurrentUser());
        return "users/userDetail";

    }

    @PostMapping("/user/updateUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateTass(@RequestParam(name = "id") Long id,
                             @RequestParam(name = "uder_email") String email,
                             @RequestParam(name = "user_password") String password,
                             @RequestParam(name = "user_re_password") String re_password,
                             @RequestParam(name = "fullName") String fullName,Model model){

        AuthUsers user = userService.findUser(id);

        if (user!=null){

            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);
        }


        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/";
    }


    @PostMapping("/user/userEdit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignRole(@RequestParam(name = "id") Long id, Model model){

        AuthUsers user = userService.findUser(id);

        System.out.println("you are assiging to: "+ user);
        userService.assignRole(user);
        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/admin/userList";
    }

}
