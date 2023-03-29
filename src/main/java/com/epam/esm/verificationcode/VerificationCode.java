package com.epam.esm.verificationcode;

import com.epam.esm.ExcludeCoverage;
import com.epam.esm.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
@ExcludeCoverage
public class VerificationCode extends RepresentationModel<VerificationCode> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String verificationCode;
    @OneToOne(cascade = CascadeType.MERGE)
    private User user;
    @CreatedDate
    private Date createDate;
}