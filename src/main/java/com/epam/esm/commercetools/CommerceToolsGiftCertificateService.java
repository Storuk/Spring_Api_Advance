package com.epam.esm.commercetools;

import com.epam.esm.commercetools.model.CommerceToolsGiftCertificate;
import com.epam.esm.commercetools.model.SortingTypesRequest;
import com.epam.esm.commercetools.repository.CommerceToolsGiftCertificateRepo;
import com.epam.esm.giftcertificate.GiftCertificateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@EnableTransactionManagement
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommerceToolsGiftCertificateService {
    private final CommerceToolsGiftCertificateRepo commerceToolsGiftCertificateRepo;

    @Transactional
    public CommerceToolsGiftCertificate createGiftCertificate(GiftCertificateDTO giftCertificate) {
        return commerceToolsGiftCertificateRepo.createGiftCertificate(giftCertificate);
    }

    public CommerceToolsGiftCertificate getGiftCertificateById(String id) {
        return commerceToolsGiftCertificateRepo.getGiftCertificateById(id);
    }

    public Page<CommerceToolsGiftCertificate> getGiftCertificatesByTags(Set<String> stringSet, int page, int size) {
        return new PageImpl<>(commerceToolsGiftCertificateRepo.getGiftCertificatesByTagNames(stringSet,
                PageRequest.of(page, size)));
    }

    public Page<CommerceToolsGiftCertificate> getAllGiftCertificates(int page, int size) {
        return new PageImpl<>(commerceToolsGiftCertificateRepo.getGiftCertificates(PageRequest.of(page, size)));
    }

    public void deleteGiftCertificate(String id) {
        commerceToolsGiftCertificateRepo.unPublishProduct(id, commerceToolsGiftCertificateRepo.getCommerceToolsProductVersion(id));
        commerceToolsGiftCertificateRepo.deleteGiftCertificate(id, commerceToolsGiftCertificateRepo.getCommerceToolsProductVersion(id));
    }

    public Page<CommerceToolsGiftCertificate> getGiftCertificateByPartOfDescription(String partOfDescription, int page, int size) {
        return new PageImpl<>(commerceToolsGiftCertificateRepo
                .getGiftCertificatesByPartOfDescription(partOfDescription, PageRequest.of(page, size)));
    }

    public Page<CommerceToolsGiftCertificate> getGiftCertificatesSortedByName(String sortDirection, int page, int size) {
        return new PageImpl<>(commerceToolsGiftCertificateRepo.getGiftCertificatesSortedByName(sortDirection, PageRequest.of(page, size)));
    }

    public Page<CommerceToolsGiftCertificate> getGiftCertificatesSortedByNameByDate(SortingTypesRequest sortingTypes, int page, int size) {
        return new PageImpl<>(commerceToolsGiftCertificateRepo
                .getGiftCertificatesSortedByNameByDate(sortingTypes, PageRequest.of(page, size)));
    }

    public CommerceToolsGiftCertificate updateGiftCertificate(String id, GiftCertificateDTO giftCertificateDTO) {
        long version = commerceToolsGiftCertificateRepo.getCommerceToolsProductVersion(String.valueOf(id));
        return commerceToolsGiftCertificateRepo.updateGiftCertificate(id, version, giftCertificateDTO);
    }

    public Page<CommerceToolsGiftCertificate> getGiftCertificatesByTagsName(Set<String> name, int page, int size, String operator) {
        return new PageImpl<>(commerceToolsGiftCertificateRepo.getGiftCertificatesByTagsName(name, PageRequest.of(page, size), operator));
    }
}