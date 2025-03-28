package com.gtel.api.specifications;

import com.gtel.api.domains.models.postgres.Test;
import com.gtel.api.utils.TextUtil;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.util.TextUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDtoSpecification {

    private static final String FIELD_ID = "id";
    private static final String FIELD_IS_DELETE = "isDelete";
    private static final String FIELD_STATUS = "status";

    private final List<Specification<Test>> specifications = new ArrayList<>();

    public static TestDtoSpecification builder() {
        return new TestDtoSpecification();
    }

    public void withDeletionStatus(final boolean deletionStatus) {
        specifications.add((root, query, builder) ->
                deletionStatus ? builder.isNotNull(root.get(FIELD_IS_DELETE)) : builder.isNull(root.get(FIELD_IS_DELETE))
        );
    }

    public void withStatus(final Integer status) {
        if (Objects.isNull(status)) {
            return;
        }
        specifications.add((root, query, builder) -> builder.equal(root.get(FIELD_STATUS), status));
    }

    /**
     * @param input          chuoi tu khoa tim kiem
     * @param field          ten truong tim kiem
     * @param specifications dieu kiem tim kiem
     */
    private static void searchByField(String input,
                                      String field,
                                      List<Specification<Test>> specifications
    ) {
        if (TextUtils.isBlank(input)) {
            return;
        }
        List<String> q = TextUtil.handleSearchText(input);

        specifications.add(
                (root, query, builder) -> builder.or(
                        q.stream()
                                .map(s -> {
                                    Expression<String> record;
                                    record = builder.function("remove_accents", String.class, root.get(field));
                                    record = builder.function("strip_html_tags", String.class, record);
                                    record = builder.upper(record);
                                    return builder.like(record, "%" + s.toUpperCase() + "%");
                                })
                                .toArray(Predicate[]::new)
                )
        );
    }

    public Specification<Test> build() {
        return (root, query, builder) -> builder.and(
                specifications.stream()
                        .filter(Objects::nonNull)
                        .map(spec -> spec.toPredicate(root, query, builder))
                        .toArray(Predicate[]::new)
        );
    }
}
