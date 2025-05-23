package com.ruan.todosimple.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruan.todosimple.models.Task;
import com.ruan.todosimple.models.User;
import com.ruan.todosimple.repositories.TaskRepository;
import com.ruan.todosimple.services.exceptions.DataBindingViolationException;

import jakarta.transaction.Transactional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById (Long id){
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(()-> new RuntimeException(
        "Tarefa não encontrado"));
    }

    public List<Task> FindAllByUserId(Long userId){
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
    }

    @Transactional
    public Task create(Task obj){
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }
    
    @Transactional
    public Task update(Task obj){
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException(
            "Não é possivel excluir o usuário, pois há entidades relacionadas");
        }
    }

}
