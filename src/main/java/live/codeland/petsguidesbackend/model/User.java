package live.codeland.petsguidesbackend.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Document(collection = "user")
public class User implements UserDetails {
	// fields
	@Id
	private String id;
	private String displayName;
	@Field
	@NotNull
	@Pattern(regexp = "^(?![0-9]+$)(?!(false|true)$)[a-zA-Z]+$", message = "Invalid first name format")
	private String firstName;
	@Field
	@NotNull
	@Pattern(regexp = "^(?![0-9]+$)(?!(false|true)$)[a-zA-Z]+$", message = "Invalid last name format")
	private String lastName;

	@Field
	@NotNull
	@Indexed(unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", message = "Invalid e-mail address")
	private String email;

	@Field
	@NotNull
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+", message = "Please include at least 1 uppercase, 1 lowercase and 1 digit.")
	@Size(min = 12, message = "Password must be at least 12 characters long.")
	private String password;

	@Field
	@NotNull
	@Indexed(unique = true)
	@Pattern(regexp = "^(?:\\+852-?)?[456789]\\d{3}-?\\d{4}$", message = "Invalid phone number")
	private String phone;


	@Field
	private Boolean verified;

	@Field
	private String avatar;

	@Field
	@Enumerated(EnumType.STRING)
	private Role role;


	@Field
	private LocalDateTime createdAt;

	@Field
	private LocalDateTime updatedAt;

	@Field
	private Boolean deleted = false;

	@Field
	private LocalDateTime deletedAt;



	// constructor
	public User(String id, String firstName, String lastName, String email, String password, String phone, String avatar) {
		this.id = id;
		this.displayName = firstName + " " + lastName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.verified = false;
		this.avatar = avatar;
		this.role = Role.USER;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.deleted = false;
		this.deletedAt = null;
	}



	// getter
	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public Boolean getVerified() {
		return verified;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getRoles() { return role.name(); }

	public LocalDateTime getCreatedAt() { return createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }

	public Boolean getDeleted() {
		return deleted;
	}

	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}






	// setter
	public void setId(String id) {
		this.id = id;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}



	// below is UserDetails-related information
	// These fields are part of the UserDetails contract, and Spring Security manages them internally.
	// When Spring Security loads a user from the database, it uses the information stored in the User entity
	// and combines it with below UserDetails-related information. (we cannot see them in MongoCompass, but can see it when call get user API)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return email;
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
