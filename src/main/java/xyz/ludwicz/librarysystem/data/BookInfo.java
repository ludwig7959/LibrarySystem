package xyz.ludwicz.librarysystem.data;

import javax.persistence.*;

@Entity
@Table(name = "BookInfo")
public class BookInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookInfoId")
    private Integer bookInfoId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false, length = 32)
    private String author;

    @ManyToOne
    @JoinColumn(name = "publisherId", nullable = false)
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public Integer getBookInfoId() {
        return bookInfoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
