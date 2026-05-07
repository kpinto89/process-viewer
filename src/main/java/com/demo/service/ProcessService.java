package com.demo.service;

import com.demo.model.ChartData;
import com.demo.model.ProcessInfo;
import com.demo.model.ProcessQuery;
import com.demo.model.ProcessResult;
import com.demo.model.ProcessSummary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public ProcessResult getProcesses(ProcessQuery query) {
        List<ProcessInfo> allProcesses = ProcessHandle.allProcesses()
                .map(this::toProcessInfo)
                .toList();

        List<ProcessInfo> filtered = filterProcesses(allProcesses, query.getQ());
        filtered.sort(resolveComparator(query));

        long aliveCount = filtered.stream().filter(ProcessInfo::alive).count();
        ProcessSummary summary = new ProcessSummary(
                allProcesses.size(),
                filtered.size(),
                aliveCount,
                filtered.size() - aliveCount
        );

        ChartData chartData = buildChartData(allProcesses);

        return new ProcessResult(query, summary, filtered, chartData);
    }

    public List<ProcessInfo> listProcesses() {
        return getProcesses(new ProcessQuery("", "pid", "asc", 5)).processes();
    }

    private ProcessInfo toProcessInfo(ProcessHandle processHandle) {
        ProcessHandle.Info info = processHandle.info();

        String user = info.user().orElse("-");
        String command = info.command().orElse("-");
        String arguments = info.arguments().map(args -> String.join(" ", args)).orElse("");
        long startEpochMillis = info.startInstant()
                .map(Instant::toEpochMilli)
                .orElse(-1L);
        String startTime = info.startInstant()
                .map(this::formatInstant)
                .orElse("-");

        return new ProcessInfo(
                processHandle.pid(),
                user,
                command,
                arguments,
                startTime,
                startEpochMillis,
                processHandle.isAlive()
        );
    }

    private List<ProcessInfo> filterProcesses(List<ProcessInfo> source, String search) {
        if (search == null || search.isBlank()) {
            return new ArrayList<>(source);
        }

        String needle = search.toLowerCase(Locale.ROOT);
        return source.stream()
                .filter(process -> matches(process, needle))
                .toList();
    }

    private boolean matches(ProcessInfo process, String needle) {
        return String.valueOf(process.pid()).contains(needle)
                || process.user().toLowerCase(Locale.ROOT).contains(needle)
                || process.command().toLowerCase(Locale.ROOT).contains(needle)
                || process.arguments().toLowerCase(Locale.ROOT).contains(needle);
    }

    private Comparator<ProcessInfo> resolveComparator(ProcessQuery query) {
        Comparator<ProcessInfo> comparator = switch (query.getSort()) {
            case "user" -> Comparator.comparing(ProcessInfo::user, String.CASE_INSENSITIVE_ORDER)
                    .thenComparingLong(ProcessInfo::pid);
            case "command" -> Comparator.comparing(ProcessInfo::command, String.CASE_INSENSITIVE_ORDER)
                    .thenComparingLong(ProcessInfo::pid);
            case "start" -> Comparator.comparingLong(ProcessInfo::startEpochMillis)
                    .thenComparingLong(ProcessInfo::pid);
            case "alive" -> Comparator.comparing(ProcessInfo::alive)
                    .thenComparingLong(ProcessInfo::pid);
            default -> Comparator.comparingLong(ProcessInfo::pid);
        };

        if ("desc".equals(query.getDir())) {
            return comparator.reversed();
        }
        return comparator;
    }

    private ChartData buildChartData(List<ProcessInfo> all) {
        // Alive vs Not Alive
        long alive = all.stream().filter(ProcessInfo::alive).count();
        long dead = all.size() - alive;

        // Top 10 users by process count
        Map<String, Long> byUser = all.stream()
                .collect(Collectors.groupingBy(ProcessInfo::user, Collectors.counting()));
        Map<String, Long> topUsers = byUser.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        // Top 10 commands by process count (filename only)
        Map<String, Long> byCommand = all.stream()
                .collect(Collectors.groupingBy(p -> shortCommand(p.command()), Collectors.counting()));
        Map<String, Long> topCommands = byCommand.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        return new ChartData(
                List.of("Alive", "Not Alive"),
                List.of(alive, dead),
                new ArrayList<>(topUsers.keySet()),
                new ArrayList<>(topUsers.values()),
                new ArrayList<>(topCommands.keySet()),
                new ArrayList<>(topCommands.values())
        );
    }

    private String shortCommand(String command) {
        if (command == null || command.equals("-")) return "-";
        int slash = command.lastIndexOf('/');
        int backslash = command.lastIndexOf('\\');
        int idx = Math.max(slash, backslash);
        return idx >= 0 ? command.substring(idx + 1) : command;
    }

    private String formatInstant(Instant instant) {
        return DATE_TIME_FORMATTER.format(instant);
    }
}
