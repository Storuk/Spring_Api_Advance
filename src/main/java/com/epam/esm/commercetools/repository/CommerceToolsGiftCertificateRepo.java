package com.epam.esm.commercetools.repository;

import com.epam.esm.commercetools.model.CommerceToolsGiftCertificate;
import com.epam.esm.commercetools.model.SortingTypesRequest;
import com.epam.esm.giftcertificate.GiftCertificateDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

public interface CommerceToolsGiftCertificateRepo {
    List<CommerceToolsGiftCertificate> getGiftCertificatesByTagsName(Set<String> tagName, PageRequest pageRequest, String operator);
    CommerceToolsGiftCertificate createGiftCertificate(GiftCertificateDTO giftCertificate);
    List<CommerceToolsGiftCertificate> getGiftCertificates(PageRequest pageRequest);
    CommerceToolsGiftCertificate getGiftCertificateById(String id);
    long getCommerceToolsProductVersion(String id);
    void deleteGiftCertificate(String id, long version);
    void unPublishProduct(String id, long version);
    List<CommerceToolsGiftCertificate> getGiftCertificatesByPartOfDescription(String name, PageRequest pageRequest);
    List<CommerceToolsGiftCertificate> getGiftCertificatesSortedByName(String sortDirection, PageRequest pageRequest);
    List<CommerceToolsGiftCertificate> getGiftCertificatesSortedByNameByDate(SortingTypesRequest sortingTypes, PageRequest pageRequest);
    List<CommerceToolsGiftCertificate> getGiftCertificatesByTagNames(Set<String> tagNameSet, PageRequest pageRequest);
    CommerceToolsGiftCertificate updateGiftCertificate(String id, long version, GiftCertificateDTO giftCertificateDTO);
}
