package com.pricestool.pricestool.repository;

import com.pricestool.pricestool.domain.Vgoitem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Vgoitem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VgoitemRepository extends JpaRepository<Vgoitem, Long>, JpaSpecificationExecutor<Vgoitem> {

}
