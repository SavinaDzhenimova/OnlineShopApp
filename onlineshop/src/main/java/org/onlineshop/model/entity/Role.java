package org.onlineshop.model.entity;

import jakarta.persistence.*;
import org.onlineshop.model.enums.RoleName;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    public Role() {
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
