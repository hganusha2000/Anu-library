package Hutechlibrary.Anu.Library.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;
    
    @ManyToOne
    @JoinColumn(name = "library_id")
    @JsonBackReference(value = "library-book")
    private Library library;


    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference(value = "author-book")
    private Author author;


    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @JsonBackReference(value = "publisher-book")
    private Publisher publisher;

    private int publicationYear;
	private Integer totalCopies = 1;
	private Integer availableCopies = 1;
    	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
}
