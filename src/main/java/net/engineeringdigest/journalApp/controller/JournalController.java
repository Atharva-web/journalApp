package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalController {
    @Autowired
    private JournalService journalService;
    @Autowired
    private UserService userService;

//    @GetMapping(value = {"/", ""})
//    public ResponseEntity<List<JournalEntry>> getAllEntries() {
//        return new ResponseEntity<>(journalService.getAllEntries(), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<?> getJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<JournalEntry> allJournals = userService.getAllJournals(authentication.getName());
        return new ResponseEntity<>(allJournals, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id) {
        if (journalService.getEntryById(id).isPresent()) {
            return new ResponseEntity<>(journalService.getEntryById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> saveEntryToUser(@RequestBody JournalEntry journalEntry) {
        journalService.saveEntry(journalEntry);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User entrySavedToUser = userService.setJournalEntry(journalEntry.getId(), authentication.getName());
        if (entrySavedToUser != null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @PutMapping("/id/{id}")
//    public ResponseEntity<?> updateById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry) {
//        JournalEntry oldEntry = journalService.getEntryById(id).orElse(null);
//        if(oldEntry != null) {
//            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
//            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
//            journalService.updateEntry(oldEntry);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    //    id is given by the frontend
    @PutMapping("/id/{oldJournalId}") // title, content -> cannot find journal
    public ResponseEntity<?> updateById(@PathVariable ObjectId oldJournalId, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<JournalEntry> oldJournalEntry = journalService.getEntryById(oldJournalId);
        if (oldJournalEntry.isPresent()) {
            userService.updateEntry(oldJournalEntry.get(), newEntry, authentication.getName());
        }
        return new ResponseEntity<>("journal id not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        boolean deleted = journalService.deleteEntryById(id);
        if (deleted) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}