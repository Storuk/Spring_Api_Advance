package com.epam.esm.giftcertificate;

import com.epam.esm.utils.SqlQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Vlad Storoshchuk
 */
@Repository
public interface GiftCertificateRepo extends JpaRepository<GiftCertificate, Long> {
    /**
     * A repository method for checking if GiftCertificate exists in database
     * calling a query from {@link SqlQueries} class
     *
     * @return boolean
     */
    @Query(SqlQueries.GiftCertificatesQueries.IS_GIFT_CERTIFICATE_EXIST)
    boolean giftCertificateExists(@Param("name") String name);

    /**
     * A repository method for getting all GiftCertificates by Tag Name
     * calling a query from {@link SqlQueries} class
     *
     * @return List of GiftCertificates
     */
    @Query(value = SqlQueries.GiftCertificatesQueries.GET_GIFT_CERTIFICATE_BY_TAG_NAME, nativeQuery = true)
    List<GiftCertificate> getGiftCertificatesByTagName(String tagName, int begin, int end);

    /**
     * A repository method for getting all GiftCertificates by part of Description
     * calling a query from {@link SqlQueries} class
     *
     * @return List of GiftCertificates
     */
    @Query(value = SqlQueries.GiftCertificatesQueries.GET_GIFT_CERTIFICATE_BY_PART_OF_DESCRIPTION, nativeQuery = true)
    List<GiftCertificate> getGiftCertificateByPartOfDescription(String partOfDescription, int begin, int end);

    /**
     * A repository method for getting all GiftCertificates by Tags Name
     * calling a query from {@link SqlQueries} class
     *
     * @return List of GiftCertificates
     */
    @Query(value = SqlQueries.GiftCertificatesQueries.GET_ALL_GIFT_CERTIFICATES_BY_TAGS, nativeQuery = true)
    List<GiftCertificate> getByTags(Set<String> tagSet, int size, int begin, int end);
}