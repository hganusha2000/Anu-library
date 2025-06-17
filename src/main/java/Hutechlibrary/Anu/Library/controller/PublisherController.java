package Hutechlibrary.Anu.Library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Hutechlibrary.Anu.Library.entity.Publisher;
import Hutechlibrary.Anu.Library.service.PublisherService;

@RestController
@RequestMapping("/api")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @GetMapping("/user/publishers")
    public List<Publisher> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @GetMapping("/user/publishers/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable("id") Long id) {
        Publisher publisher = publisherService.getPublisherById(id);
        return ResponseEntity.ok(publisher);
    }

    @PostMapping("/librarian/publishers")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) {
        Publisher createdPublisher = publisherService.createPublisher(publisher);
        return ResponseEntity.ok(createdPublisher);
    }

    @PutMapping("/librarian/publishers/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable("id") Long id, @RequestBody Publisher publisherDetails) {
        Publisher updatedPublisher = publisherService.updatePublisher(id, publisherDetails);
        return ResponseEntity.ok(updatedPublisher);
    }

    @DeleteMapping("/admin/publishers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePublisher(@PathVariable("id") Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.ok("Publisher deleted successfully");
    }
}
