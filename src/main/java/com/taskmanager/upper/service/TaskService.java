package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.Important;
import com.taskmanager.upper.entity.TaskType;
import com.taskmanager.upper.entity.Tasks;
import com.taskmanager.upper.entity.Urgency;
import com.taskmanager.upper.repository.ImportantRepository;
import com.taskmanager.upper.repository.TaskTypeRepository;
import com.taskmanager.upper.repository.UrgencyRepo;
import com.taskmanager.upper.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UrgencyRepo urgencyRepo;

    private final TaskTypeRepository taskTypeRepository;

    private final ImportantRepository importantRepository;

    public List<Tasks> getAllTasks(){
        List<Tasks> tasks = taskRepository.findAllByOrderByDuedateDesc();

        for(Tasks tsk: tasks){
            LocalDate today = LocalDate.now();
            Period period = Period.between(today, tsk.getDuedate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            int diff = period.getDays();

            if(diff<=0){
                tsk.setIscomplete("Due date passed!");
            }else if (diff==1){
                tsk.setIscomplete("Hurry up, only one day left");
            }else{
                tsk.setIscomplete(diff+" days left");
            }
        }
        return tasks;
    }

    public Tasks addTask(Tasks task){

        taskRepository.save(task);

        return task;

    }

    public Tasks taskDetail(Long id){
        Tasks task = taskRepository.findById(id).orElse(new Tasks());


        LocalDate today = LocalDate.now();
        Period period = Period.between(today, task.getDuedate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
        int diff = period.getDays();

        if(diff<=0){
            task.setIscomplete("Due date passed!");
        }else{
            task.setIscomplete(diff+" days left");
        }
        return task;
    }

    public void deleteTask(Long id) {
        Tasks task = taskRepository.findById(id).orElse(null);
        if (task != null){
            taskRepository.delete(task);
        }
    }

    public List<Urgency> getAllurgency(){
       return urgencyRepo.findAll();
    }

    public List<Important> getAllImportants(){
        return importantRepository.findAll();
    }

    public Important getImportant(Long id){
        return importantRepository.findById(id).orElse(null);
    }

    public Tasks findById(Long id){
        Tasks task = taskRepository.findById(id).orElse(null);
        return task;
    }

    public Urgency getUrgency(Long id){
       Urgency urgency = urgencyRepo.findById(id).orElse(null);
       return urgency;
    }

    public List<Tasks> getAllUrgentTasks(Long id){
        List<Tasks> tasks = taskRepository.findAllUrgentTasks(id);

        System.out.println(tasks);
        for(Tasks tsk: tasks){
            LocalDate today = LocalDate.now();
            Period period = Period.between(today, tsk.getDuedate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            int diff = period.getDays();

            if(diff<=0){
                tsk.setIscomplete("Due date passed!");
            }else if (diff==1){
                tsk.setIscomplete("Hurry up, only one day left");
            }else{
                tsk.setIscomplete(diff+" days left");
            }
        }
        return tasks;
    }

    public List<Tasks> getAllNotUrgentTasks(Long id){
        List<Tasks> tasks = taskRepository.findAllNotUrgentTasks(id);

        System.out.println(tasks);
        for(Tasks tsk: tasks){
            LocalDate today = LocalDate.now();
            Period period = Period.between(today, tsk.getDuedate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            int diff = period.getDays();

            if(diff<=0){
                tsk.setIscomplete("Due date passed!");
            }else if (diff==1){
                tsk.setIscomplete("Hurry up, only one day left");
            }else{
                tsk.setIscomplete(diff+" days left");
            }
        }
        return tasks;
    }

    public List<Tasks> getAllImportantTasksOnly(Long id){
        List<Tasks> tasks = taskRepository.findAllImportantTasks(id);

        System.out.println(tasks);
        for(Tasks tsk: tasks){
            LocalDate today = LocalDate.now();
            Period period = Period.between(today, tsk.getDuedate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            int diff = period.getDays();

            if(diff<=0){
                tsk.setIscomplete("Due date passed!");
            }else if (diff==1){
                tsk.setIscomplete("Hurry up, only one day left");
            }else{
                tsk.setIscomplete(diff+" days left");
            }
        }
        return tasks;
    }

    public List<Tasks> getAllNotImportantTasksOnly(Long id){
        List<Tasks> tasks = taskRepository.findAllNotImportantTasks(id);

        for(Tasks tsk: tasks){
            LocalDate today = LocalDate.now();
            Period period = Period.between(today, tsk.getDuedate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            int diff = period.getDays();

            if(diff<=0){
                tsk.setIscomplete("Due date passed!");
            }else if (diff==1){
                tsk.setIscomplete("Hurry up, only one day left");
            }else{
                tsk.setIscomplete(diff+" days left");
            }
        }
        return tasks;
    }

    public List<Tasks> getImportantAndUrgentTasks(Long id){
        List<Tasks> tasks = taskRepository.findAllNotImportantAndUrgentTasks(id);

        for(Tasks tsk: tasks){
            LocalDate today = LocalDate.now();
            Period period = Period.between(today, tsk.getDuedate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            int diff = period.getDays();

            if(diff<=0){
                tsk.setIscomplete("Due date passed!");
            }else if (diff==1){
                tsk.setIscomplete("Hurry up, only one day left");
            }else{
                tsk.setIscomplete(diff+" days left");
            }
        }
        return tasks;
    }


    public List<TaskType> gettAllTaskTypes(){
        return taskTypeRepository.findAll();
    }


    public TaskType getTaskType(Long id){
        TaskType type = taskTypeRepository.findById(id).orElse(null);
        return type;
    }


    public List<Tasks> getTasksByUser(Long id){
        return taskRepository.findAllByUserId(id);
    }
    public List<Tasks> getAcceptedTasksByUser(Long id){
        return taskRepository.findAllByUserIdAndResponseAccepted(id);
    }

    public List<Tasks> getAllSubmittedTasks(){
        return taskRepository.findAllSubmittedTasks();
    }
    public List<Tasks> getAllRespondedTasks(){
        return taskRepository.findAllRespndedTasks();
    }
    public List<Tasks> getAllPendingTasks(){
        return taskRepository.findAllPendingTasks();
    }
    public List<Tasks> getAllRejectedTasks(){
        return taskRepository.findAllRejectedTasks();
    }
    public List<Tasks> getAllUnassignedTasks(){
        return taskRepository.findAllUnassignedTasks();
    }
}
