package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


import static org.springframework.data.jpa.domain.Specification.where;

import java.util.Date;
import java.util.List;

@Component
public class PlayerSpecification {

    private final PlayerRepository playerRepository;

    public PlayerSpecification(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Page<Player> getQueryResult(List<SearchCriteria> filters, Pageable pageable) {
        if (filters.size() > 0) {
            return playerRepository.findAll(getSpecificationFromFilters(filters), pageable);
        } else {
            return playerRepository.findAll(pageable);
        }
    }

    private Specification<Player> getSpecificationFromFilters(List<SearchCriteria> filter) {
        Specification<Player> specification = where(createSpecification(filter.remove(0)));
        for (SearchCriteria input : filter) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private Specification<Player> createSpecification(SearchCriteria filter) {
        switch (filter.getSearchOperation()) {
            case EQUALITY:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(filter.getKey()),
                        castToRequiredType(root.get(filter.getKey()).getJavaType(), filter.getValue()));
            case GREATER_THAN:
                return (root, query, criteriaBuilder) -> criteriaBuilder.gt(root.get(filter.getKey()),
                        (Number) castToRequiredType(root.get(filter.getKey()).getJavaType(), filter.getValue()));
            case LESS_THAN:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lt(root.get(filter.getKey()),
                        (Number) castToRequiredType(root.get(filter.getKey()).getJavaType(), filter.getValue()));
            case GREATER_THAN_DATE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getKey()),
                        (Date) castToRequiredType(root.get(filter.getKey()).getJavaType(), filter.getValue()));
            case LESS_THAN_DATE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(filter.getKey()),
                        (Date)  castToRequiredType(root.get(filter.getKey()).getJavaType(), filter.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(filter.getKey()),
                        "%" +
                                castToRequiredType(root.get(filter.getKey()).getJavaType(), filter.getValue()) + "%");

            default:
                throw new RuntimeException();
        }
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value);
        } else if (fieldType.isAssignableFrom(String.class)) {
            return String.valueOf(value);
        } else if (fieldType.isAssignableFrom(Boolean.class)) {
            return Boolean.valueOf(value);
        } else if (fieldType.isAssignableFrom(Date.class)) {
            return new Date(Long.parseLong(value));
        }
        return null;
    }

}