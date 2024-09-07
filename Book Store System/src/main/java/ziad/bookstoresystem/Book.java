package ziad.bookstoresystem;

public class Book {
    String title;
    String author;
    String publisher;
    String ISBN;
    String image;
    String prevLink;
    public Book(String title, String author, String publisher, String ISBN, String image , String prevLink) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.ISBN = ISBN;
        this.image = image;
    }

    public void setPrevLink(String prevLink) {
        this.prevLink = prevLink;
    }

    public String getTitle() {
        return title;
    }

    public String getPrevLink() {
        return prevLink;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
