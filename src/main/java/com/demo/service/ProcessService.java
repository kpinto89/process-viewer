package com.demo.service;

import com.demo.model.ProcessInfo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class ProcessService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public List<ProcessInfo> listProcesses() {
        return ProcessHandle.allProcesses()
                .map(this::toProcessInfo)
                .sorted(Comparator.comparingLong(ProcessInfo::pid))
                .toList();
    }

    private ProcessInfo toProcessInfo(ProcessHandle processHandle) {
        ProcessHandle.Info info = processHandle.info();

        String user = info.user().orElse("-");
        String command = info.command().orElse("-");
        String arguments = info.arguments().map(args -> String.join(" ", args)).orElse("");
        String startTime = info.startInstant()
                .map(this::formatInstant)
                .orElse("-");

        return new ProcessInfo(
                processHandle.pid(),
                user,
                command,
                arguments,
                startTime,
                processHandle.isAlive()
        );
    }

    private String formatInstant(Instant instant) {
        return DATE_TIME_FORMATTER.format(instant);
    }
}

