package xyz.ludwicz.librarysystem.data;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    private Integer bookId;

    @ManyToOne
    @JoinColumn(name = "bookInfoId", nullable = false)
    private BookInfo bookInfo;

    @Formula("(SELECT COUNT(*) > 0 FROM Checkout c WHERE c.bookId = bookId AND c.returnDate IS NULL)")
    private boolean isCheckedOut;

    @Formula("(SELECT COUNT(*) > 0 FROM Checkout c WHERE c.bookId = bookId AND c.dueDate < CURDATE() AND c.returnDate IS NULL)")
    private boolean isOverdue;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public boolean isOverdue() {
        return isOverdue;
    }
}
