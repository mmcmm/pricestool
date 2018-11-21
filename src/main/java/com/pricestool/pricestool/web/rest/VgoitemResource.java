package com.pricestool.pricestool.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pricestool.pricestool.domain.Vgoitem;
import com.pricestool.pricestool.service.VgoitemService;
import com.pricestool.pricestool.web.rest.errors.BadRequestAlertException;
import com.pricestool.pricestool.web.rest.util.HeaderUtil;
import com.pricestool.pricestool.web.rest.util.PaginationUtil;
import com.pricestool.pricestool.service.dto.VgoitemCriteria;
import com.pricestool.pricestool.service.VgoitemQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Vgoitem.
 */
@RestController
@RequestMapping("/api")
public class VgoitemResource {

    private final Logger log = LoggerFactory.getLogger(VgoitemResource.class);

    private static final String ENTITY_NAME = "vgoitem";

    private final VgoitemService vgoitemService;

    private final VgoitemQueryService vgoitemQueryService;

    public VgoitemResource(VgoitemService vgoitemService, VgoitemQueryService vgoitemQueryService) {
        this.vgoitemService = vgoitemService;
        this.vgoitemQueryService = vgoitemQueryService;
    }

    /**
     * POST  /vgoitems : Create a new vgoitem.
     *
     * @param vgoitem the vgoitem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vgoitem, or with status 400 (Bad Request) if the vgoitem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    // @PostMapping("/vgoitems")
    // @Timed
    // public ResponseEntity<Vgoitem> createVgoitem(@Valid @RequestBody Vgoitem vgoitem) throws URISyntaxException {
    //     log.debug("REST request to save Vgoitem : {}", vgoitem);
    //     if (vgoitem.getId() != null) {
    //         throw new BadRequestAlertException("A new vgoitem cannot already have an ID", ENTITY_NAME, "idexists");
    //     }
    //     Vgoitem result = vgoitemService.save(vgoitem);
    //     return ResponseEntity.created(new URI("/api/vgoitems/" + result.getId()))
    //         .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
    //         .body(result);
    // }

    /**
     * PUT  /vgoitems : Updates an existing vgoitem.
     *
     * @param vgoitem the vgoitem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vgoitem,
     * or with status 400 (Bad Request) if the vgoitem is not valid,
     * or with status 500 (Internal Server Error) if the vgoitem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    // @PutMapping("/vgoitems")
    // @Timed
    // public ResponseEntity<Vgoitem> updateVgoitem(@Valid @RequestBody Vgoitem vgoitem) throws URISyntaxException {
    //     log.debug("REST request to update Vgoitem : {}", vgoitem);
    //     if (vgoitem.getId() == null) {
    //         throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    //     }
    //     Vgoitem result = vgoitemService.save(vgoitem);
    //     return ResponseEntity.ok()
    //         .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vgoitem.getId().toString()))
    //         .body(result);
    // }

    /**
     * GET  /vgoitems : get all the vgoitems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of vgoitems in body
     */
    @GetMapping("/vgoitems")
    @Timed
    public ResponseEntity<List<Vgoitem>> getAllVgoitems(VgoitemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vgoitems by criteria: {}", criteria);
        Page<Vgoitem> page = vgoitemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vgoitems");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /vgoitems/count : count all the vgoitems.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/vgoitems/count")
    @Timed
    public ResponseEntity<Long> countVgoitems(VgoitemCriteria criteria) {
        log.debug("REST request to count Vgoitems by criteria: {}", criteria);
        return ResponseEntity.ok().body(vgoitemQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /vgoitems/:id : get the "id" vgoitem.
     *
     * @param id the id of the vgoitem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vgoitem, or with status 404 (Not Found)
     */
    @GetMapping("/vgoitems/{id}")
    @Timed
    public ResponseEntity<Vgoitem> getVgoitem(@PathVariable Long id) {
        log.debug("REST request to get Vgoitem : {}", id);
        Optional<Vgoitem> vgoitem = vgoitemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vgoitem);
    }

    /**
     * DELETE  /vgoitems/:id : delete the "id" vgoitem.
     *
     * @param id the id of the vgoitem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    // @DeleteMapping("/vgoitems/{id}")
    // @Timed
    // public ResponseEntity<Void> deleteVgoitem(@PathVariable Long id) {
    //     log.debug("REST request to delete Vgoitem : {}", id);
    //     vgoitemService.delete(id);
    //     return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    // }

    /**
     * SEARCH  /_search/vgoitems?query=:query : search for the vgoitem corresponding
     * to the query.
     *
     * @param query the query of the vgoitem search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/vgoitems")
    @Timed
    public ResponseEntity<List<Vgoitem>> searchVgoitems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Vgoitems for query {}", query);
        Page<Vgoitem> page = vgoitemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/vgoitems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
