package com.taskmanager.upper.controller;

import com.taskmanager.upper.auth.AuthUserConfig;
import com.taskmanager.upper.entity.*;
import com.taskmanager.upper.service.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UserController extends AuthUserConfig {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SubmitService submitService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private MailService mailService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @Value("${file.submittedFile.uploadPath}")
    private String uploadFilePath;

    @Value("${file.submittedFile.viewPath}")
    private String viewSubmittedFilePath;


    public static String uploadDirectory = System.getProperty("user.dir")+"/static/avatars/";

    @GetMapping("/")
    public String userIndex(Model model) {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        model.addAttribute("announcements", announcements);
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/userIndex";
    }

    @GetMapping("/announcement/readAnnouncement/{id}")
    public String readAnnouncement(Model model, @PathVariable(name = "id") Long id) {
        Announcement announcement = announcementService.getAnnouncement(id);
        model.addAttribute("announcement", announcement);
        model.addAttribute("currentUser", getCurrentUser());
        return "announcement/readAnnouncement";
    }

    @GetMapping("/admin/userList")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getUserList(Model model) {
        List<AuthUsers> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        return "users/userList";
    }

    @GetMapping("/user/userListSearch")
    @PreAuthorize("isAuthenticated()")
    @Query(
            value = "SELECT u.email, u.full_name FROM users u WHERE u.email " +
                    "LIKE CONCAT('%', :keyword, '%') ",
            nativeQuery = true)
    public String getUserListByFilter(@RequestParam(name="keyword") String keyword, Model model) {
        List<AuthUsers> users = userService.getAllUsersByFilter(keyword);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        return "users/filteredUserList";
    }

    @GetMapping("/user/signIn")
    public String signIn(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/signIn";
    }

    @GetMapping("/user/profile")
    @PreAuthorize("isAuthenticated()")
    public String getProfile(Model model) {
        List<Tasks> tasks = taskService.getTasksByUser(getCurrentUser().getId());
        List<Tasks> tasksAccepted = taskService.getAcceptedTasksByUser(getCurrentUser().getId());
        int taskNum = 0;
        if (tasks != null) {
            taskNum = tasks.size();
            model.addAttribute("taskNum", taskNum);
            model.addAttribute("tasks", tasks);
            model.addAttribute("tasksAccepted", tasksAccepted);
        }
        model.addAttribute("currentUser", getCurrentUser());
        return "users/profile";
    }

    @GetMapping("/user/editProfile")
    @PreAuthorize("isAuthenticated()")
    public String getEditProfile(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "users/editProfile";
    }

    @PostMapping(value = "/user/deleteUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteUser(@RequestParam(name = "id") Long id, Model model) {
        userService.deleteUser(id);
        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/admin/userList";
    }

    @GetMapping("/user/notification")
    @PreAuthorize("isAuthenticated()")
    public String displayNotification(Model model) {
        List<Tasks> tasks = taskService.getTasksByUser(getCurrentUser().getId());
        int taskNum = 0;
        if (tasks != null) {
            taskNum = tasks.size();
            model.addAttribute("taskNum", taskNum);
        }
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/notification";
    }

    @GetMapping("/admin/adminPanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public String adminPanel(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/adminPanel";
    }

    @GetMapping("/moderator/moderatorPanel")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_USER')")
    public String moderatorPanel(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/moderatorPanel";
    }

    @GetMapping("/user/userPanel")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String userPanel(Model model) {
        List<Tasks> tasks = taskService.getTasksByUser(getCurrentUser().getId());
        List<Tasks> tasksAccepted = taskService.getAcceptedTasksByUser(getCurrentUser().getId());
        int taskNum = 0;
        if (tasks != null) {
            taskNum = tasks.size();
            model.addAttribute("taskNum", taskNum);
            model.addAttribute("tasks", tasks);
            model.addAttribute("tasksAccepted", tasksAccepted);
        }
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/userPanel";
    }


    @GetMapping("/accessError")
    public String accessError(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/accessDenied";
    }

    @PostMapping("/user/addUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addUser(@RequestParam(name = "user_email") String email,
                          @RequestParam(name = "user_password") String password,
                          @RequestParam(name = "user_re_password") String re_password,
                          @RequestParam(name = "fullName") String fullName, Model model) {
        if (password.equals(re_password)) {
            AuthUsers user = new AuthUsers();
            user.setEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setFullName(fullName);
            if (userService.saveUser(user) != null) {
                return "redirect:/user/addUser?success";
            }

        }
        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/user/addUser?error";
    }

    @GetMapping("/user/addUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String displayUser(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "users/addUser";
    }


    @PostMapping("/user/updateProfile")
    @PreAuthorize("isAuthenticated()")
    public String updateUserProfile(@RequestParam(name = "fullName") String fullName,
                                    @RequestParam(name = "oldPassword") String oldPassword,
                                    @RequestParam(name = "newPassword") String newPassword,
                                    @RequestParam(name = "reNewPassword") String reNewPassword, Model model) {
        if (newPassword.equals(reNewPassword)) {
            AuthUsers currentUser = getCurrentUser();
            if (bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
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
    public String addUser(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "/users/addUser";
    }


    @GetMapping("/admin/user/userDetail/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String userDetail(@PathVariable(name = "ids") Long id, Model model) {
        AuthUsers user = userService.findUser(id);
        model.addAttribute("user", user);
        List<AuthRoles> roles = userService.getAllRoles();
        model.addAttribute("roleList", roles);
        model.addAttribute("currentUser", getCurrentUser());
        return "users/userDetail";

    }

    @GetMapping("/user/userProfileCheck/{ids}")
    @PreAuthorize("isAuthenticated()")
    public String userProfileCheck(@PathVariable(name = "ids") Long id, Model model) {
        AuthUsers user = userService.findUser(id);
        model.addAttribute("user", user);
        List<AuthRoles> roles = userService.getAllRoles();
        model.addAttribute("roleList", roles);
        model.addAttribute("currentUser", getCurrentUser());
        return "users/userProfileCheck";

    }

    @PostMapping("/admin/user/userDetail")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<String> updateUser(@RequestParam(name = "ids") Long id,
                                             @RequestParam(name="user_name") String userName,
                                             @RequestParam(name="user_email") String email,
                                             @RequestParam(name="roles") Long roleId, Model model) {
        AuthUsers user = userService.findUser(id);
        if(user!=null){
            AuthRoles role = roleService.findRolesById(roleId);
            if(role!=null){
                List<AuthRoles> authRoles = user.getRole();
                if(authRoles==null){
                    authRoles=new ArrayList<>();
                }
                authRoles.add(role);
                user.setFullName(userName);
                user.setEmail(email);
                user.setRole(authRoles);
            }
        }

        model.addAttribute("currentUser", getCurrentUser());
        if(user!=null){
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("News Not Found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/user/updateUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateTass(@RequestParam(name = "id") Long id,
                             @RequestParam(name = "uder_email") String email,
                             @RequestParam(name = "user_password") String password,
                             @RequestParam(name = "user_re_password") String re_password,
                             @RequestParam(name = "fullName") String fullName, Model model) {
        AuthUsers user = userService.findUser(id);
        if (user != null) {
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);
        }
        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/";
    }


    @PostMapping("/user/userEdit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignRole(@RequestParam(name = "id") Long id, Model model) {
        AuthUsers user = userService.findUser(id);
        System.out.println("you are assiging to: " + user);
        userService.assignRole(user);
        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/admin/userList";
    }

    @GetMapping("/user/acceptTask/{ids}")
    @PreAuthorize("isAuthenticated()")
    public String acceptTask(@PathVariable(name = "ids") Long id, Model model) {
        Tasks task = taskService.taskDetail(id);
        List<Urgency> urgencies = taskService.getAllurgency();
        List<TaskType> taskTypes = taskService.gettAllTaskTypes();
        List<AuthUsers> users = userService.getAllUsers();
        if (task != null && task.getTaskType() != null) {
            taskTypes.removeAll(task.getTaskType());
        }
        model.addAttribute("urgencies", urgencies);
        model.addAttribute("task", task);
        model.addAttribute("taskTypes", taskTypes);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        return "users/acceptTask";

    }

    @GetMapping("/user/rejectTask/{ids}")
    @PreAuthorize("isAuthenticated()")
    public String rejectTask(@PathVariable(name = "ids") Long id, Model model) {
        Tasks task = taskService.taskDetail(id);
        List<Urgency> urgencies = taskService.getAllurgency();
        List<TaskType> taskTypes = taskService.gettAllTaskTypes();
        List<AuthUsers> users = userService.getAllUsers();
        if (task != null && task.getTaskType() != null) {
            taskTypes.removeAll(task.getTaskType());
        }
        model.addAttribute("urgencies", urgencies);
        model.addAttribute("task", task);
        model.addAttribute("taskTypes", taskTypes);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        return "users/rejectTask";

    }

    @GetMapping("/user/submitTask/{ids}")
    @PreAuthorize("isAuthenticated()")
    public String submitTask(@PathVariable(name = "ids") Long id, Model model) {
        Tasks task = taskService.taskDetail(id);
        List<Urgency> urgencies = taskService.getAllurgency();
        List<TaskType> taskTypes = taskService.gettAllTaskTypes();
        List<AuthUsers> users = userService.getAllUsers();
        if (task != null && task.getTaskType() != null) {
            taskTypes.removeAll(task.getTaskType());
        }
        model.addAttribute("urgencies", urgencies);
        model.addAttribute("task", task);
        model.addAttribute("taskTypes", taskTypes);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        return "users/submitTask";

    }

    @PostMapping("/user/addUserPhoto")
    @PreAuthorize("isAuthenticated()")
    public String addUserPhoto(@RequestParam(name="inputFileName") MultipartFile file){
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")){
            try {
                AuthUsers user = getCurrentUser();
                String picName = DigestUtils.sha1Hex("avatar_"+user.getId()+"_!Picture");
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName+".jpg");
                Files.write(path, bytes);

                user.setUserPhoto(picName);
                userService.addUser(user);

                return "redirect:/user/profile?success";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";

    }


    @PostMapping("/user/submitTask")
    @PreAuthorize("isAuthenticated()")
    public String submitTask(@RequestParam(name="comment") String comment,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam(name="taskId") Long taskId, RedirectAttributes ra) throws IOException {

        Tasks task = taskService.findById(taskId);

        if(task!=null){
            AuthUsers authUsers = getCurrentUser();
            Submit submit = new Submit();

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());


            submit.setFileName(fileName);
            submit.setComment(comment);
            submit.setSubmittedDate(new Date());
            submit.setTasks(task);
            submit.setAuthor(authUsers);
            submit.setSize(file.getSize());
            submit.setContent(file.getBytes());

            if(submitService.addSubmit(submit)!=null){
                task.setSubmit(submit);
                taskService.addTask(task);
                ra.addFlashAttribute("message", "The task has been submitted successfully!");
                return "redirect:/user/userPanel";
            }
        }
        return "redirect:/user/userPanel/?err";

    }

    @GetMapping(value = "/user/viewUserPhoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody  byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException{
        String pictureUrl = viewPath+defaultPicture;
        if(url!=null){
            pictureUrl = viewPath + url+ ".jpg";
        }
        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(pictureUrl);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath+defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);

    }

    @GetMapping("/mail/composeMail")
    @PreAuthorize("isAuthenticated()")
    public String composeMail( Model model) {
        model.addAttribute("currentUser", getCurrentUser());

        return "/email/composeEmail";
    }


    @PostMapping("/mail/composeMail")
    @PreAuthorize("isAuthenticated()")
    public String composeMail(@RequestParam(name="subject") String subject,
                @RequestParam(name="senderId") AuthUsers senderId,
                @RequestParam(name="ids") AuthUsers receiverId,
                @RequestParam(name="mail") String body) {
        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.setMail(body);
        mail.setSendDate(new Date());
        mail.setReceiver(senderId);
        mail.setSender(receiverId);
        Mail newMail = mailService.addMail(mail);
        if (newMail != null) {
            return "redirect:/user/userProfileCheck?success";
        }
        return "redirect:/user/userProfileCheck?success";
    }

}
