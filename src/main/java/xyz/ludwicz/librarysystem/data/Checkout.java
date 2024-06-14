package xyz.ludwicz.librarysystem.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Checkout")
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkoutId")
    private Integer checkoutId;

    @Column(name = "bookId", nullable = false)
    private Integer bookId;

    @Column(name = "memberId", nullable = false, length = 16)
    private String memberId;

    @Column(name = "librarianId", length = 32)
    private String librarianId;

    @Column(name = "dueDate", nullable = false)
    private Date dueDate;

    @Column(name = "returnDate")
    private Date returnDate;

    // Getters and Setters

    public Integer getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(Integer checkoutId) {
        this.checkoutId = checkoutId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(String librarianId) {
        this.librarianId = librarianId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
