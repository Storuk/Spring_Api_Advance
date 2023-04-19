package com.epam.esm.commercetools.mapper;

import com.commercetools.api.models.product.Product;
import com.epam.esm.commercetools.model.CommerceToolsGiftCertificate;

import java.util.List;

public interface CommerceToolsProductToGiftCertificateMapper {
    CommerceToolsGiftCertificate convertCommerceToolsProductToCommerceToolsGiftCertificate(Product product);
    List<CommerceToolsGiftCertificate> convertListOfCommerceToolsProductToListOfCommerceToolsGiftCertificates(List<Product> products);
}
