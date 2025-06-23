package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.PublisherResponseDTO;
import Hutechlibrary.Anu.Library.entity.Publisher;

public interface PublisherService {
	Publisher createPublisherEntity(Publisher publisher);

    Publisher getPublisherById(Long id);

    Publisher updatePublisher(Long id, Publisher publisherDetails);

    void deletePublisher(Long id);

    Page<Publisher> getAllPublishers(Pageable pageable);

    Page<Publisher> searchPublishers(String keyword, Pageable pageable);

    PublisherResponseDTO convertToDTO(Publisher publisher);
}
