package net.engineeringdigest.journalApp.entity;

import lombok.Data;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class JournalEntry {
    @Id
    private ObjectId id;
    private String title;
    private LocalDateTime date;
    private String content;
}
