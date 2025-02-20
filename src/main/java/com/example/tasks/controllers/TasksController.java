package com.example.tasks.controllers;

import com.example.tasks.domain.dto.TaskDto;
import com.example.tasks.domain.dto.TaskListDto;
import com.example.tasks.domain.entities.Task;
import com.example.tasks.mappers.TaskMapper;
import com.example.tasks.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/task-lists/{task_list_id}/tasks")
public class TasksController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TasksController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public List<TaskDto> listTasks(@PathVariable("task_list_id") UUID taskListId) {
        return taskService.listTasks(taskListId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @PostMapping
    public TaskDto createTask(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TaskDto taskDto) {

        Task createdTask = taskService.createTask(
                taskListId,
                taskMapper.fromDto(taskDto)
        );

        return taskMapper.toDto(createdTask);
    }

    @GetMapping("/{task_id}")
    public Optional<TaskDto> getTask(
            @PathVariable("task_id") UUID taskId,
            @PathVariable("task_list_id") UUID taskListId
    ) {
        return taskService.getTask(taskListId, taskId).map(taskMapper::toDto);
    }

    @PutMapping(path = "/{task_id}")
    public TaskDto updateTask(
            @PathVariable("task_id") UUID taskId,
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TaskDto taskDto
    ) {
        Task updatedTask = taskService.updateTask(
                taskListId,
                taskId,
                taskMapper.fromDto(taskDto)
        );
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping(path = "/{task_id}")
    public void deleteTask(
            @PathVariable("task_id") UUID taskId,
            @PathVariable("task_list_id") UUID taskListId
    ) {
        taskService.deleteTask(taskListId, taskId);
    }
}
