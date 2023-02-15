package com.epam.esm.tag;


import com.epam.esm.utils.SqlQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Vlad Storoshchuk
 * */
@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {
    /**
     * A repository method for getting Tag id by Tag name
     * calling a query from {@link SqlQueries} class
     * @return tag id
     * */
    @Query(SqlQueries.TagsQueries.GET_TAG_ID_BY_TAG_NAME)
    long getTagIdByTagName(String tagName);

    /**
     * A repository method for checking if tag exists in database
     * calling a query from {@link SqlQueries} class
     * @return boolean
     * */
    @Query(SqlQueries.TagsQueries.IS_TAG_EXISTS)
    boolean tagExists(String tagName);

    /**
     * A repository method for getting mostly used tag of user orders with highest sum of orders
     * calling a query from {@link SqlQueries} class
     * @return Tag
     * */
    @Query(value = SqlQueries.TagsQueries.GET_MOSTLY_USED_TAG, nativeQuery = true)
    Tag getTheMostlyUsedTag();
}