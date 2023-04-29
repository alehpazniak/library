package pl.aleh.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Book")
public class Book {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotEmpty(message = "Book title cannot be empty")
  @Size(min = 2, max = 30, message = "Book title must be between 2 and 30 characters")
  private String title;

  @NotEmpty(message = "Author cannot be empty")
  @Size(min = 2, max = 30, message = "The author's name must be between 2 and 30 characters")
  private String author;

  @Min(value = 1500, message = "Year must be greater than 1500")
  private int year;

  @ManyToOne
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private Person owner;


  @Column(name = "taken_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date takenAt;

  @Transient
  private boolean expired;

}
