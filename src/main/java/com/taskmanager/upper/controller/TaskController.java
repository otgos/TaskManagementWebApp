package com.taskmanager.upper.controller;

import com.taskmanager.upper.auth.AuthUserConfig;
import com.taskmanager.upper.entity.*;
import com.taskmanager.upper.service.AnnouncementService;
import com.taskmanager.upper.service.SubmitService;
import com.taskmanager.upper.service.TaskService;
import com.taskmanager.upper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;


@Controller
public class TaskController extends AuthUserConfig {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubmitService submitService;

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/tasks/completedTasks")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getMainPage(Model model) {
        List<Tasks> tasks = taskService.getAllSubmittedTasks();
        model.addAttribute("tasks", tasks);
        List<Urgency> urgency = taskService.getAllurgency();
        model.addAttribute("urgency", urgency);
        List<Important> importants = taskService.getAllImportants();
        model.addAttribute("importants", importants);
        List<AuthUsers> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());


        return "tasks/completedTasks";
    }

    @GetMapping("/tasks/plannedTasks")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getPlannedTask(Model model) {
        List<Tasks> tasks = taskService.getAllRespondedTasks();
        model.addAttribute("tasks", tasks);
        List<Urgency> urgency = taskService.getAllurgency();
        model.addAttribute("urgency", urgency);
        List<Important> importants = taskService.getAllImportants();
        model.addAttribute("importants", importants);
        List<AuthUsers> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());


        return "tasks/plannedTasks";
    }

    @GetMapping("/tasks/unassignedTasks")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getUnassignedTask(Model model) {
        List<Tasks> tasks = taskService.getAllUnassignedTasks();
        model.addAttribute("tasks", tasks);
        List<Urgency> urgency = taskService.getAllurgency();
        model.addAttribute("urgency", urgency);
        List<Important> importants = taskService.getAllImportants();
        model.addAttribute("importants", importants);
        List<AuthUsers> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());


        return "tasks/unassignedTasks";
    }

    @GetMapping("/tasks/pendingTasks")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getPendingTask(Model model) {
        List<Tasks> tasks = taskService.getAllPendingTasks();
        model.addAttribute("tasks", tasks);
        List<Urgency> urgency = taskService.getAllurgency();
        model.addAttribute("urgency", urgency);
        List<Important> importants = taskService.getAllImportants();
        model.addAttribute("importants", importants);
        List<AuthUsers> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        return "tasks/pendingTasks";
    }

    @GetMapping("/tasks/rejectedTasks")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getRejectedTask(Model model) {
        List<Tasks> tasks = taskService.getAllRejectedTasks();
        model.addAttribute("tasks", tasks);
        List<Urgency> urgency = taskService.getAllurgency();
        model.addAttribute("urgency", urgency);
        List<Important> importants = taskService.getAllImportants();
        model.addAttribute("importants", importants);
        List<AuthUsers> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        return "tasks/rejectedTasks";
    }

    @GetMapping("/tasks/submittedTaskDetail/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String submittedTaskDetail(@PathVariable(name = "ids") Long id,
                                        Model model){

        Tasks task = taskService.taskDetail(id);
//        Submit submit=submitService.getSubmitById(submitId);
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
//        model.addAttribute("submits", submit);
        return "tasks/submittedTaskDetail";

    }
    @GetMapping("/task/downloadSubmitFile")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void downloadSubmitFile(@Param("submitId") Long id, HttpServletResponse response) throws Exception {
        Optional<Submit> result = Optional.ofNullable(submitService.getSubmitById(id));
        if(!result.isPresent()){
            throw new Exception("Could not found the file with ID: "+id);
        }
        Submit file = result.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+file.getFileName();
        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(file.getContent());
        outputStream.close();

    }



}
