package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Publisher;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.PublisherRepository;

@Service

public class PublisherServiceIMPL implements PublisherService{
	
	@Autowired
    private PublisherRepository publisherRepository;
	

    @Override
    public DataResponse createPublisher(Publisher publisher) {
        if (publisherRepository.existsByName(publisher.getName())) {
            return new DataResponse(HttpStatus.CONFLICT.value(), "Publisher already exists", null);
        }

        Publisher saved = publisherRepository.save(publisher);
        return new DataResponse(HttpStatus.CREATED.value(), "Publisher created successfully", saved);
    }

    @Override
    public Page<Publisher> getAllPublishers(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }

    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
    }
    
    public Page<Publisher> searchPublishers(String keyword, Pageable pageable) {
        return publisherRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(keyword, keyword, pageable);
    }


 
    public Publisher updatePublisher(Long id, Publisher publisherDetails) {
        Publisher publisher = getPublisherById(id);
        publisher.setName(publisherDetails.getName());
        publisher.setAddress(publisherDetails.getAddress());
        return publisherRepository.save(publisher);
    }

    public void deletePublisher(Long id) {
        Publisher publisher = getPublisherById(id);
        publisherRepository.delete(publisher);
    }
}

