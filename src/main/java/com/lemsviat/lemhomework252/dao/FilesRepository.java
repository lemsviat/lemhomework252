package com.lemsviat.lemhomework252.dao;

import com.lemsviat.lemhomework252.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<File,Integer>{
}
