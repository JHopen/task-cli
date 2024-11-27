import java.time.LocalDateTime;

public final class Task {
    private long id;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(long id, String description, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Builder {
        private long id;
        private String description;
        private TaskStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Task build() {
            return new Task(id, description, status, createdAt, updatedAt);
        }

        public boolean check() {
            return id > 0 && description != null && status != null && createdAt != null && updatedAt != null;
        }

        public void id(String id) {
            if (this.id != 0) {
                throw new IllegalStateException("id already defined, should not be redefined");
            }
            this.id = Long.parseLong(id);
        }

        public void description(String description) {
            if (this.description != null) {
                throw new IllegalStateException("description already defined, should not be redefined");
            }
            this.description = description;
        }

        public void status(String status) {
            if (this.status != null) {
                throw new IllegalStateException("status already defined, should not be redefined");
            }
            this.status = TaskStatus.valueOf(status);
        }

        public void createdAt(String createdAt) {
            if (this.createdAt != null) {
                throw new IllegalStateException("createdAt already defined, should not be redefined");
            }
            this.createdAt = LocalDateTime.parse(createdAt);
        }

        public void updatedAt(String updatedAt) {
            if (this.updatedAt != null) {
                throw new IllegalStateException("updatedAt already defined, should not be redefined");
            }
            this.updatedAt = LocalDateTime.parse(updatedAt);
        }
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %s | %s", id, description, status.getName(), createdAt, updatedAt);
    }

    public String toJson() {
        return String.format("""
                {
                 "id": %d,
                 "description": "%s",
                 "status": "%s",
                 "createdAt": "%s",
                 "updatedAt": "%s"
                }
                """, id, description, status.toString(), createdAt.toString(), updatedAt.toString());
    }
}
