package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.repository.JournalRepository;
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

    public boolean deleteEntryById(ObjectId id) {
        Optional<JournalEntry> journalEntry = journalRepository.findById(id);
        if(journalEntry.isPresent()) {
            journalRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
