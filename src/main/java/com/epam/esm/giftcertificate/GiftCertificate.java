package com.epam.esm.giftcertificate;

import com.epam.esm.tag.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Set;

/**
 * @author Vlad Storoshchuk
 * */
@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class GiftCertificate extends RepresentationModel<GiftCertificate> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastUpdateDate;
    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Tag> tags;
}