package com.raquitich.talleres.repositories;

import com.raquitich.talleres.models.TallerExtracurricular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TallerRepository extends JpaRepository<TallerExtracurricular, Long> {
}
