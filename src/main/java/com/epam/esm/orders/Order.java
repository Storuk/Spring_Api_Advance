package com.epam.esm.orders;

import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

/**
 * @author Vlad Storoshchuk
 * */
@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    private GiftCertificate giftCertificate;
    @Column(nullable = false)
    private int cost;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @CreatedDate
    private Date purchaseDate;
}