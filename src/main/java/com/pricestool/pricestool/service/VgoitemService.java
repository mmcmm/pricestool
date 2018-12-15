package com.pricestool.pricestool.service;

import com.pricestool.pricestool.domain.Vgoitem;
import com.pricestool.pricestool.repository.VgoitemRepository;
import com.pricestool.pricestool.repository.search.VgoitemSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Vgoitem.
 */
@Service
@Transactional
public class VgoitemService {

    private final Logger log = LoggerFactory.getLogger(VgoitemService.class);

    private final VgoitemRepository vgoitemRepository;

    private final VgoitemSearchRepository vgoitemSearchRepository;

    public VgoitemService(VgoitemRepository vgoitemRepository, VgoitemSearchRepository vgoitemSearchRepository) {
        this.vgoitemRepository = vgoitemRepository;
        this.vgoitemSearchRepository = vgoitemSearchRepository;
    }

    /**
     * Save a vgoitem.
     *
     * @param vgoitem the entity to save
     * @return the persisted entity
     */
    public Vgoitem save(Vgoitem vgoitem) {
        log.debug("Request to save Vgoitem : {}", vgoitem);
        Vgoitem result = vgoitemRepository.save(vgoitem);
        vgoitemSearchRepository.save(result);
        return result;
    }

     /**
     * Delete all vgoitems.
     *
     */
    public void deleteAll() {
        log.debug("Request to delete all : {}");
        vgoitemRepository.deleteAll();
        vgoitemSearchRepository.deleteAll();
    }

    /**
     * Get all the vgoitems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Vgoitem> findAll(Pageable pageable) {
        log.debug("Request to get all Vgoitems");
        return vgoitemRepository.findAll(pageable);
    }


    /**
     * Get one vgoitem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Vgoitem> findOne(Long id) {
        log.debug("Request to get Vgoitem : {}", id);
        return vgoitemRepository.findById(id);
    }

    /**
     * Delete the vgoitem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Vgoitem : {}", id);
        vgoitemRepository.deleteById(id);
        vgoitemSearchRepository.deleteById(id);
    }

    /**
     * Search for the vgoitem corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Vgoitem> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Vgoitems for query {}", query);
        return vgoitemSearchRepository.search(queryStringQuery(query), pageable);    }
}
