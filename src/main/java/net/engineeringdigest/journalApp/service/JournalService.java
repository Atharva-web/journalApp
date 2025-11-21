package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {
    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private UserRepository userRepository;

    public List<JournalEntry> getAllEntries() {
        return journalRepository.findAll();
    }

    public Optional<JournalEntry> getEntryById(ObjectId id) {
        return journalRepository.findById(id);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        journalRepository.save(journalEntry);
    }

    public void updateEntry(JournalEntry newEntry) {
        journalRepository.save(newEntry);
    }

    public void deleteEntryById(String username, ObjectId id) throws Exception {
        try {
            journalRepository.deleteById(id);
            User user = userRepository.findByUsername(username);
        }
        catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
