package com.example.demo.specification;

import com.example.demo.entity.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    // Specification to filter by age greater than a specified value
    public static Specification<Student> hasAgeGreaterThanTen(int age) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("age"), age);
    }

    // Specification to filter by prefix
    public static Specification<Student> startWithPrefix(String prefix) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), prefix + "%");
    }

    // Specification to filter by end with
    public static Specification<Student> endWithKey(String endWith) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + endWith);
    }

    // Specification to filter by both prefix and endWith
    public static Specification<Student> startWithPrefixAndEndWithKey(String prefix, String endWith) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), prefix + "%" + endWith + "%");
    }
}
