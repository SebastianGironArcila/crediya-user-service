package co.com.crediya.r2dbc.role;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("role")
public class RoleData {

    @Id
    @Column("id")
    private Integer id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}
