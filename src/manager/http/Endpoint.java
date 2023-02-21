package manager.http;

import com.sun.net.httpserver.HttpExchange;

enum Endpoint {
    GET_TASKS,
    GET_EPICS,
    GET_SUBTASKS,
    GET_TASK,
    GET_SUBTASK,
    GET_EPIC,
    POST_TASK,
    POST_EPIC,
    POST_SUBTASK,
    DELETE_TASK,
    DELETE_SUBTASK,
    DELETE_EPIC,
    DELETE_ALL_TASKS,
    DELETE_ALL_SUBTASKS,
    DELETE_ALL_EPICS,
    GET_EPIC_SUBTASKS,
    GET_HISTORY,
    GET_PRIORITIZED_TASKS,
    UNKNOWN;


    public static Endpoint getEndpoint(HttpExchange exchange) {

        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        String[] pathSplitted = requestPath.split("/");

        int length = pathSplitted.length;
        if (pathSplitted[length - 1].isBlank()) length--;
        if (length == 1) {
            return Endpoint.UNKNOWN;
        } else if (length == 2) {
            if (pathSplitted[1].equals("tasks") && requestMethod.equals("GET")) return Endpoint.GET_PRIORITIZED_TASKS;
        } else {
            String taskType = pathSplitted[2];
            switch (taskType) {
                case "task":
                    if (length == 3 && query == null) {
                        if (requestMethod.equals("GET")) return Endpoint.GET_TASKS;
                        if (requestMethod.equals("DELETE")) return Endpoint.DELETE_ALL_TASKS;
                        if (requestMethod.equals("POST")) return Endpoint.POST_TASK;
                    }
                    if (length == 3 && query != null) {
                        if (query.startsWith("id=")) {
                            if (requestMethod.equals("GET")) return Endpoint.GET_TASK;
                            if (requestMethod.equals("DELETE")) return Endpoint.DELETE_TASK;
                        }
                    }
                    return Endpoint.UNKNOWN;
                case "subtask":
                    if (length == 3 && query == null) {
                        if (requestMethod.equals("GET")) return Endpoint.GET_SUBTASKS;
                        if (requestMethod.equals("DELETE")) return Endpoint.DELETE_ALL_SUBTASKS;
                        if (requestMethod.equals("POST")) return Endpoint.POST_SUBTASK;
                    }
                    if (length == 3 && query != null) {
                        if (query.startsWith("id=")) {
                            if (requestMethod.equals("GET")) return Endpoint.GET_SUBTASK;
                            if (requestMethod.equals("DELETE")) return Endpoint.DELETE_SUBTASK;
                        }
                    }

                    if (length == 4 && requestMethod.equals("GET") && query != null) {
                        if (pathSplitted[3].equals("epic") &&
                                query.startsWith("id=")) return Endpoint.GET_EPIC_SUBTASKS;
                    }
                    return Endpoint.UNKNOWN;
                case "epic":
                    if (length == 3 && query == null) {
                        if (requestMethod.equals("GET")) return Endpoint.GET_EPICS;
                        if (requestMethod.equals("DELETE")) return Endpoint.DELETE_ALL_EPICS;
                        if (requestMethod.equals("POST")) return Endpoint.POST_EPIC;
                    }
                    if (length == 3 && query != null) {
                        if (query.startsWith("id=")) {
                            if (requestMethod.equals("GET")) return Endpoint.GET_EPIC;
                            if (requestMethod.equals("DELETE")) return Endpoint.DELETE_EPIC;
                        }
                    }
                    return Endpoint.UNKNOWN;
                case "history":
                    if (requestMethod.equals("GET")) return Endpoint.GET_HISTORY;
                    return Endpoint.UNKNOWN;
                default:
                    return Endpoint.UNKNOWN;
            }
        }
        return Endpoint.UNKNOWN;
    }

    }