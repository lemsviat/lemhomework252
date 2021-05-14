package com.lemsviat.lemhomework252.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "files")
@Data
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_status")
    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return fileName.equals(file.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }
}
