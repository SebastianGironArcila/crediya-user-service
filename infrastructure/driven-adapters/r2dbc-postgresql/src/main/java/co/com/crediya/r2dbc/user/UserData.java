package co.com.crediya.r2dbc.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserData {

    @Id
    @Column("id")
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("email")
    private String email;

    @Column("identity_document")
    private String identityDocument;

    @Column("phone")
    private String phone;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("base_salary")
    private BigDecimal baseSalary;

    @Column("address")
    private String address;

    @Column("role_id")
    private Integer roleId;
}
