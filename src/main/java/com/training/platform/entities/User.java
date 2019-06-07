package com.training.platform.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.training.platform.validators.Default;
import com.training.platform.validators.Extended;
import com.training.platform.validators.FieldsValueMatch;
import com.training.platform.validators.UniqueEmail;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude="shop")
@ToString(exclude = {"shop"})
@Entity
@Table(name = "users")
@FieldsValueMatch(field = "password",
        fieldMatch = "confirm_password",
        message = "The password fields must match")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name is a required field", groups={ Default.class })
    @Size(min=2,  message="Name should have atleast 2 characters", groups={ Default.class })
    @Size(max = 100,  message="Name should have not over 5 characters", groups={ Default.class })
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surname is a required field", groups={ Default.class })
    @Size(min=2,  message="Surname should have atleast 2 characters", groups={ Default.class })
    @Size(max = 100,  message="Surname should have not over 5 characters", groups={ Default.class })
    private String surname;

    @Column(name = "email")
    @NotEmpty(message = "Email is a required field", groups={ Extended.class })
    @Email(message = "Email is a email pattern", groups={ Extended.class })
    //Insert this line
    @UniqueEmail(message = "There is already user with this email!", groups={ Extended.class })
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password is a required field", groups={ Extended.class })
    private String password;

    @Transient
    @NotEmpty(message = "Confirm Password is a required field", groups={ Extended.class })
    private String confirm_password;

    @Column(name = "age")
    @Digits(integer=2, fraction=0, message = "Age is a only numeric and between 15 to 60 years old", groups={ Default.class })
    private Integer age;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    @NotEmpty(message = "City is a required field", groups={ Default.class })
    private String city;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "active")
    private Integer active;

    @Column(name = "api_token")
    private String api_token;

    @Column(name = "remember_token")
    private String rememberToken;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Shop shop;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        this.roles.stream().forEach((role) -> {
            list.add(new SimpleGrantedAuthority(role.getRole()));
        });

        return list;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}