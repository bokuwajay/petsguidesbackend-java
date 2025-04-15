package live.codeland.petsguidesbackend.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


@Document(collection = "book")
public class Book {

    @Id
    private String id;

    @Field
    @NotNull
    @NotBlank
    private String title;

    @Field
    @NotNull
    @NotBlank
    private String author;

    @Field
    private boolean published;

    @Field
    private LocalDateTime createdAt;

    @Field
    private LocalDateTime updatedAt;

    @Field
    private Boolean isDeleted = false;

    @Field
    private LocalDateTime deletedAt;


    public Book(String id, String title, String author, boolean published) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.deletedAt = null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isPublished() {
        return published;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

}