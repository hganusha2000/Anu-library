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
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.PublisherDetails;
import Hutechlibrary.Anu.Library.entity.Publisher;
import Hutechlibrary.Anu.Library.service.PublisherService;

@RestController
@RequestMapping("/api")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;
    
    
    @PostMapping("/librarian/publishers")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createPublisher(@RequestBody Publisher publisher) {
        DataResponse responseData = publisherService.createPublisher(publisher);

        ApiResponse response = new ApiResponse(responseData);

        if (responseData.getStatus() == HttpStatus.CONFLICT.value()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/user/publishers")
    public ResponseEntity<ApiResponse> getAllPublishers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<Publisher> publisherPage = publisherService.getAllPublishers(PageRequest.of(page, size));

        List<DataResponse> publisherDataList = publisherPage.getContent().stream()
                .map(p -> new DataResponse(HttpStatus.OK.value(), "Publisher data", p))
                .collect(Collectors.toList());

        PublisherDetails details = new PublisherDetails();
        details.setData(publisherDataList);
        details.setTotalPages(publisherPage.getTotalPages());
        details.setTotalElements(publisherPage.getTotalElements());

        DataResponse wrapper = new DataResponse(HttpStatus.OK.value(), "Publishers fetched successfully", details);
        ApiResponse response = new ApiResponse(wrapper);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/publishers/{id}")
    public ResponseEntity<ApiResponse> getPublisherById(@PathVariable("id") Long id) {
        Publisher publisher = publisherService.getPublisherById(id);

        DataResponse data = new DataResponse(HttpStatus.OK.value(), "Publisher fetched successfully", publisher);
        ApiResponse response = new ApiResponse(data);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/librarian/publishers/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updatePublisher(@PathVariable("id") Long id, @RequestBody Publisher publisherDetails) {
        Publisher updated = publisherService.updatePublisher(id, publisherDetails);

        DataResponse data = new DataResponse(HttpStatus.OK.value(), "Publisher updated successfully", updated);
        ApiResponse response = new ApiResponse(data);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/publishers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePublisher(@PathVariable("id") Long id) {
        publisherService.deletePublisher(id);

        DataResponse data = new DataResponse(HttpStatus.OK.value(), "Publisher deleted successfully", null);
        ApiResponse response = new ApiResponse(data);

        return ResponseEntity.ok(response);
    }
}