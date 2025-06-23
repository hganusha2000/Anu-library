package Hutechlibrary.Anu.Library.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.ApiResponsePublisher;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.PublisherDetails;
import Hutechlibrary.Anu.Library.dto.PublisherResponseDTO;
import Hutechlibrary.Anu.Library.entity.Publisher;
import Hutechlibrary.Anu.Library.service.PublisherService;

@RestController
@RequestMapping("/api")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/librarian/publishers")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<PublisherDetails> createPublisher(@RequestBody Publisher publisher) {
        Publisher created = publisherService.createPublisherEntity(publisher);
        PublisherResponseDTO dto = publisherService.convertToDTO(created);
        PublisherDetails response = new PublisherDetails(HttpStatus.CREATED.value(), "Publisher created successfully", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/publishers")
    public ResponseEntity<ApiResponsePublisher> getAllPublishers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Publisher> publisherPage = publisherService.getAllPublishers(PageRequest.of(page, size));

        List<PublisherResponseDTO> dtoList = publisherPage.getContent().stream()
                .map(publisherService::convertToDTO)
                .collect(Collectors.toList());

        ApiResponsePublisher response = new ApiResponsePublisher(
                HttpStatus.OK.value(),
                "Publishers fetched successfully",
                dtoList,
                publisherPage.getTotalPages(),
                publisherPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/publishers/{id}")
    public ResponseEntity<PublisherDetails> getPublisherById(@PathVariable Long id) {
        Publisher pub = publisherService.getPublisherById(id);
        PublisherResponseDTO dto = publisherService.convertToDTO(pub);
        PublisherDetails response = new PublisherDetails(HttpStatus.OK.value(), "Publisher fetched successfully", dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/publishers/search")
    public ResponseEntity<ApiResponsePublisher> searchPublishers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Publisher> publisherPage = publisherService.searchPublishers(keyword, PageRequest.of(page, size));

        List<PublisherResponseDTO> dtoList = publisherPage.getContent().stream()
                .map(publisherService::convertToDTO)
                .collect(Collectors.toList());

        ApiResponsePublisher response = new ApiResponsePublisher(
                HttpStatus.OK.value(),
                "Filtered publishers",
                dtoList,
                publisherPage.getTotalPages(),
                publisherPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/librarian/publishers/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<PublisherDetails> updatePublisher(@PathVariable Long id, @RequestBody Publisher publisherDetails) {
        Publisher updated = publisherService.updatePublisher(id, publisherDetails);
        PublisherResponseDTO dto = publisherService.convertToDTO(updated);
        PublisherDetails response = new PublisherDetails(HttpStatus.OK.value(), "Publisher updated successfully", dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/publishers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherDetails> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        PublisherDetails response = new PublisherDetails(HttpStatus.OK.value(), "Publisher deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
