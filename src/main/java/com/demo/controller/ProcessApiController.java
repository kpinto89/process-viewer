package com.demo.controller;

import com.demo.model.ProcessQuery;
import com.demo.model.ProcessResult;
import com.demo.service.ProcessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessApiController {

    private final ProcessService processService;

    public ProcessApiController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/api/processes")
    public ProcessResult processes(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "pid") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(defaultValue = "5") Integer refresh
    ) {
        return processService.getProcesses(new ProcessQuery(q, sort, dir, refresh));
    }
}

