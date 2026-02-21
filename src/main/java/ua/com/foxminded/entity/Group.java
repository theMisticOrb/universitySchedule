package ua.com.foxminded.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "academic_groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_id_gen")
    @SequenceGenerator(name = "group_id_gen", sequenceName = "group_id_seq", allocationSize = 30)
    @Column(name = "group_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @Embedded
    private YearOfUniv yearOfUniv;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "group_name")
    private String name;

    @OneToMany(mappedBy = "group")
    private Set<Student> students = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "groups_subjects", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects = new HashSet<>();

    public Group() {
    }

    public Group(Integer id, Specialization specialization, YearOfUniv yearOfUniv, LocalDate startDate, String name,
            Set<Student> students, Set<Subject> subjects) {
        this.id = id;
        this.specialization = specialization;
        this.yearOfUniv = yearOfUniv;
        this.startDate = startDate;
        this.name = name;
        this.students = students;
        this.subjects = subjects;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public YearOfUniv getYearOfUniv() {
        return yearOfUniv;
    }

    public void setYearOfUniv(YearOfUniv yearOfUniv) {
        this.yearOfUniv = yearOfUniv;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

}
