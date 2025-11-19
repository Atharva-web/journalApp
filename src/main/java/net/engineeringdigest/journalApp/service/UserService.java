package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JournalService journalService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public void updateUser(User newUser) {
        userRepository.save(newUser);
    }

    public boolean updateEntry(JournalEntry oldJournal, JournalEntry newEntry, String username) {
        User user = getUserByUsername(username);
        for (ObjectId journalId : user.getJournalEntries()) {
            if (journalId == oldJournal.getId()) {
//                this journal belongs to this user only.
                oldJournal.setTitle(
                        newEntry.getTitle() == null || newEntry.getTitle().isEmpty() ?
                                oldJournal.getTitle() : newEntry.getTitle());
                oldJournal.setContent(newEntry.getContent() == null || newEntry.getContent().isEmpty() ?
                        oldJournal.getContent() : newEntry.getContent());
                journalService.saveEntry(oldJournal);
                return true;
            }
        }
        return false;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

//    Relation

    public User setJournalEntry(ObjectId id, String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            user.getJournalEntries().add(id);
//            user.setUsername(null);
            updateUser(user);
            return user;
        }
        return null;
    }

    public List<JournalEntry> getAllJournals(String username) {
        User user = getUserByUsername(username);
        List<JournalEntry> journalEntriesList = new ArrayList<>();
        for (ObjectId id : user.getJournalEntries()) {
            journalEntriesList.add(journalService.getEntryById(id).get());
        }
        return journalEntriesList;
    }
}