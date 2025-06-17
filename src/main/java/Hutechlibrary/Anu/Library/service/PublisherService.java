package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.entity.Publisher;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.PublisherRepository;

@Service
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
    }

    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
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
