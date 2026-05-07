package com.demo.model;

public class ProcessQuery {

    private final String q;
    private final String sort;
    private final String dir;
    private final int refresh;

    public ProcessQuery(String q, String sort, String dir, Integer refresh) {
        this.q = q == null ? "" : q.trim();
        this.sort = normalizeSort(sort);
        this.dir = "desc".equalsIgnoreCase(dir) ? "desc" : "asc";
        this.refresh = normalizeRefresh(refresh);
    }

    public String getQ() {
        return q;
    }

    public String getSort() {
        return sort;
    }

    public String getDir() {
        return dir;
    }

    public int getRefresh() {
        return refresh;
    }

    public String nextDir(String field) {
        if (isSort(field) && "asc".equals(dir)) {
            return "desc";
        }
        return "asc";
    }

    public boolean isSort(String field) {
        return sort.equalsIgnoreCase(field);
    }

    private String normalizeSort(String requestedSort) {
        if (requestedSort == null) {
            return "pid";
        }
        return switch (requestedSort.toLowerCase()) {
            case "pid", "user", "command", "start", "alive" -> requestedSort.toLowerCase();
            default -> "pid";
        };
    }

    private int normalizeRefresh(Integer requestedRefresh) {
        if (requestedRefresh == null) {
            return 5;
        }
        if (requestedRefresh < 0) {
            return 0;
        }
        return Math.min(requestedRefresh, 60);
    }
}

