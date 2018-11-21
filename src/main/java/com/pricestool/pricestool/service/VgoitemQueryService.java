package com.pricestool.pricestool.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.pricestool.pricestool.domain.Vgoitem;
import com.pricestool.pricestool.domain.*; // for static metamodels
import com.pricestool.pricestool.repository.VgoitemRepository;
import com.pricestool.pricestool.repository.search.VgoitemSearchRepository;
import com.pricestool.pricestool.service.dto.VgoitemCriteria;

/**
 * Service for executing complex queries for Vgoitem entities in the database.
 * The main input is a {@link VgoitemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Vgoitem} or a {@link Page} of {@link Vgoitem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VgoitemQueryService extends QueryService<Vgoitem> {

    private final Logger log = LoggerFactory.getLogger(VgoitemQueryService.class);

    private final VgoitemRepository vgoitemRepository;

    private final VgoitemSearchRepository vgoitemSearchRepository;

    public VgoitemQueryService(VgoitemRepository vgoitemRepository, VgoitemSearchRepository vgoitemSearchRepository) {
        this.vgoitemRepository = vgoitemRepository;
        this.vgoitemSearchRepository = vgoitemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Vgoitem} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Vgoitem> findByCriteria(VgoitemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vgoitem> specification = createSpecification(criteria);
        return vgoitemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Vgoitem} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Vgoitem> findByCriteria(VgoitemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vgoitem> specification = createSpecification(criteria);
        return vgoitemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VgoitemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vgoitem> specification = createSpecification(criteria);
        return vgoitemRepository.count(specification);
    }

    /**
     * Function to convert VgoitemCriteria to a {@link Specification}
     */
    private Specification<Vgoitem> createSpecification(VgoitemCriteria criteria) {
        Specification<Vgoitem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Vgoitem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Vgoitem_.name));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), Vgoitem_.category));
            }
            if (criteria.getRarity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRarity(), Vgoitem_.rarity));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Vgoitem_.type));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColor(), Vgoitem_.color));
            }
            if (criteria.getImage300px() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage300px(), Vgoitem_.image300px));
            }
            if (criteria.getImage600px() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage600px(), Vgoitem_.image600px));
            }
            if (criteria.getSuggestedPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSuggestedPrice(), Vgoitem_.suggestedPrice));
            }
            if (criteria.getSuggestedPrice7day() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSuggestedPrice7day(), Vgoitem_.suggestedPrice7day));
            }
            if (criteria.getSuggestedPrice30day() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSuggestedPrice30day(), Vgoitem_.suggestedPrice30day));
            }
        }
        return specification;
    }
}
