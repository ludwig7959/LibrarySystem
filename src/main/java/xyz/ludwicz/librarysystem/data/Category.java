package xyz.ludwicz.librarysystem.data;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Integer categoryId;

    @Column(name = "name", nullable = false, unique = true, length = 16)
    private String name;

    @Formula("(SELECT COUNT(*) FROM Book b JOIN BookInfo bi ON b.bookInfoId = bi.bookInfoId WHERE bi.categoryId = categoryId)")
    private Long bookCount;

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBookCount() {
        return bookCount;
    }
}