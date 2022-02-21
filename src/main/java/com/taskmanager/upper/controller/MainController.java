package com.taskmanager.upper.controller;

import com.taskmanager.upper.entity.*;
import com.taskmanager.upper.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class MainController {

    @Autowired
    private TaskService taskService;



    @GetMapping("/")
    public String getMainPage(Model model){
        List<Tasks> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        List<Urgency> urgency = taskService.getAllurgency();
        model.addAttribute("urgency", urgency);
        List<Important> importants=taskService.getAllImportants();
        model.addAttribute("importants", importants);

        return "main";
    }
    @PostMapping("/addTask")
    public String addTask(@RequestParam(name = "taskName") String name,
                          @RequestParam(name = "descrption") String description,
                          @RequestParam(name = "duedate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date duedate,
                          @RequestParam(name = "urgency") Long urgency,
                          @RequestParam(name="importance") Long importance){
        Urgency urg = taskService.getUrgency(urgency);
        Important imp = taskService.getImportant(importance);
        if(urg!=null){
            Tasks task = new Tasks();
            task.setName(name);
            task.setDescription(description);
            task.setDuedate(duedate);
            task.setUrgency(urg);
            task.setImportant(imp);
            taskService.addTask(task);
        }


        return "redirect:/";
    }
    @GetMapping("/addTask")
    public String displayTask(Model model){
        List<Urgency> urgencies = taskService.getAllurgency();
        List<Important> importants = taskService.getAllImportants();
        model.addAttribute("urgencies", urgencies);
        model.addAttribute("importance", importants);
        return "addTask";
    }

    @GetMapping("/taskDetail/{ids}")
    public String taskDetail(@PathVariable(name = "ids") Long id, Model model){

        Tasks task= taskService.taskDetail(id);
        List<Urgency> urgencies = taskService.getAllurgency();
        List<TaskType> taskTypes = taskService.gettAllTaskTypes();
        if(task!=null && task.getTaskType()!=null ){
            taskTypes.removeAll(task.getTaskType());
        }
        model.addAttribute("urgencies", urgencies);
        model.addAttribute("task", task);
        model.addAttribute("taskTypes", taskTypes);

        return "taskDetail";

    }
    @PostMapping(value = "/deleteTask")
    public String deleteTask(@RequestParam(name = "id") Long id){
        taskService.deleteTask(id);
        return "redirect:/";
    }


    @PostMapping("/updateTask")
    public String updateTass(@RequestParam(name = "id") Long id,
                            @RequestParam(name = "taskName") String name,
                          @RequestParam(name = "description") String description,
                          @RequestParam(name = "duedate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date duedate){

        Tasks task = taskService.findById(id);

        if (task!=null){

            task.setName(name);
            task.setDescription(description);
            task.setDuedate(duedate);
            taskService.addTask(task);
        }


        return "redirect:/";
    }

    @PostMapping("/completedTask")
    public String passedTask(@RequestParam(name="taskId") Long id, Model model){

        Tasks completedTask = taskService.findById(id);

        model.addAttribute("completedTasks", completedTask);

        return "main";
    }

    @GetMapping("/urgentTasks")
    public String getUrgentTasks(Model model){
        List<Tasks> tasks = taskService.getAllUrgentTasks();

        System.out.println("the urgent tasks are: "+tasks);
        List<Urgency> urgency = taskService.getAllurgency();
        model.addAttribute("tasks", tasks);
        model.addAttribute("urgency", urgency);
        return "main";

    }

    @GetMapping("/notUrgentTasks")
    public String getNotUrgentTasks(Model model){
        List<Tasks> tasks = taskService.getAllNotUrgentTasks();

        System.out.println("the urgent tasks are: "+tasks);
        List<Urgency> urgency = taskService.getAllurgency();
        model.addAttribute("tasks", tasks);
        model.addAttribute("urgency", urgency);
        return "main";

    }

    @GetMapping("/importantTasks")
    public String getImportantTasks(Model model){
        List<Tasks> tasks = taskService.getAllNotUrgentTasks();

        System.out.println("the urgent tasks are: "+tasks);
        List<Tasks> importants = taskService.getAllImportantTasksOnly();
        model.addAttribute("tasks", tasks);
        model.addAttribute("important", importants);
        return "main";

    }

    @GetMapping("/notImportantTasks")
    public String getNotImportantTasks(Model model){
        List<Tasks> tasks = taskService.getAllNotImportantTasksOnly();

        System.out.println("the urgent tasks are: "+tasks);
        List<Tasks> importants = taskService.getAllImportantTasksOnly();
        model.addAttribute("tasks", tasks);
        model.addAttribute("important", importants);
        return "main";

    }

    @GetMapping("/importantAndUrgentTasks")
    public String getImportantAndUrgentTasks(Model model){
        List<Tasks> tasks = taskService.getImportantAndUrgentTasks();

        System.out.println("the urgent tasks are: "+tasks);
        List<Tasks> importants = taskService.getAllImportantTasksOnly();
        model.addAttribute("tasks", tasks);
        model.addAttribute("important", importants);
        return "main";

    }

    @PostMapping("/assignType")
    public String assignType(@RequestParam (name="taskType_id") Long taskTypeId,
                             @RequestParam(name="tasks_id") Long tasksId){

        Tasks task = taskService.findById(tasksId);
        if(task!=null){
            TaskType taskType = taskService.getTaskType(taskTypeId);
            if(taskType!=null){
                List<TaskType> taskTypeList = task.getTaskType();
                if(taskTypeList==null){
                    taskTypeList=new ArrayList<>();
                }
                taskTypeList.add(taskType);
                task.setTaskType(taskTypeList);

                taskService.addTask(task);
                return "redirect:/taskDetail/"+task.getId();
            }
        }

        return "redirect:/";
    }

    @PostMapping("/unassignType")
    public String unassignType(@RequestParam (name="taskType_id") Long taskTypeId,
                             @RequestParam(name="tasks_id") Long tasksId){

        Tasks task = taskService.findById(tasksId);
        if(task!=null){
            TaskType taskType = taskService.getTaskType(taskTypeId);
            if(taskType!=null){
                List<TaskType> taskTypeList = task.getTaskType();
                if(taskTypeList==null){
                    taskTypeList=new ArrayList<>();
                }
                taskTypeList.remove(taskType);
                task.setTaskType(taskTypeList);

                taskService.addTask(task);
                return "redirect:/taskDetail/"+task.getId();
            }
        }

        return "redirect:/";
    }

}
