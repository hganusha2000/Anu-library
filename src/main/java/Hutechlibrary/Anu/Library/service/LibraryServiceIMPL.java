package Hutechlibrary.Anu.Library.service;

import Hutechlibrary.Anu.Library.dto.*;
import Hutechlibrary.Anu.Library.entity.Book;
import Hutechlibrary.Anu.Library.entity.Borrow;
import Hutechlibrary.Anu.Library.entity.Library;
import Hutechlibrary.Anu.Library.entity.User;
import Hutechlibrary.Anu.Library.repository.BookRepository;
import Hutechlibrary.Anu.Library.repository.BorrowRepository;
import Hutechlibrary.Anu.Library.repository.LibraryRepository;
import Hutechlibrary.Anu.Library.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryServiceIMPL implements LibraryService {

    @Autowired private LibraryRepository libRepo;

    @Override
    public Library createLibrary(LibraryRequestDTO req) {
        Library lib = new Library();
        lib.setName(req.getName());
        lib.setAddress(req.getAddress());
        return libRepo.save(lib);
    }

    @Override
    public Page<Library> listLibraries(Pageable pageable) {
        return libRepo.findAll(pageable);
    }

    @Override
    public Library getLibrary(Long id) {
        return libRepo.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Library not found with id: " + id));
    }

    @Override
    public Library updateLibrary(Long id, LibraryRequestDTO req) {
        Library lib = getLibrary(id);
        lib.setName(req.getName());
        lib.setAddress(req.getAddress());
        return libRepo.save(lib);
    }

    @Override
    public void deleteLibrary(Long id) {
        Library lib = getLibrary(id);
        libRepo.delete(lib);
    }

    @Override
    public LibraryResponseDTO toResponseDTO(Library lib) {
        List<String> bookNames = lib.getBooks().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

        List<String> userNames = lib.getUsers().stream()
                .map(User::getUsername) // or getName() depending on your User entity
                .collect(Collectors.toList());

        List<String> borrowSummaries = lib.getBorrows().stream()
                .map(borrow -> {
                    String userName = borrow.getUser() != null ? borrow.getUser().getUsername() : "Unknown User";
                    String bookTitle = borrow.getBook() != null ? borrow.getBook().getTitle() : "Unknown Book";
                    return userName + " borrowed " + bookTitle;
                })
                .collect(Collectors.toList());

        return new LibraryResponseDTO(
                lib.getId(),
                lib.getName(),
                lib.getAddress(),
                bookNames,
                userNames,
                borrowSummaries
        );
    }
}