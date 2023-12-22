package live.codeland.petsguidesbackend.model;

import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "user")
public class User {
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
	@Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", message = "Invalid e-mail address")
	private String email;

	@Field
	@NotNull
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+", message = "Please include at least 1 uppercase, 1 lowercase and 1 digit.")
	@Size(min = 12, message = "Password must be at least 12 characters long.")
	private String password;

	@NotNull
	@Pattern(regexp = "^(?:\\+852-?)?[456789]\\d{3}-?\\d{4}$", message = "Invalid phone number")
	private String phone;


	private Boolean verified = false;
	private String avatar;
	private List roles;
	private Boolean active = true;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;
	private Boolean deleted = false;
	private LocalDateTime deletedAt;

	public User(String id, String displayName, String firstName, String lastName, String email, String password, String phone, Boolean verified, String avatar, List roles, Boolean active,LocalDateTime createdAt, LocalDateTime updatedAt, Boolean deleted, LocalDateTime deletedAt) {
		this.id = id;
		this.displayName = displayName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.verified = verified;
		this.avatar = avatar;
		this.roles = roles;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt =updatedAt;
		this.deleted = deleted;
		this.deletedAt = deletedAt;
	}


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

	public List getRoles() {
		return roles;
	}

	public Boolean getActive() {
		return active;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}

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

	public void setRoles(List roles) {
		this.roles = roles;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
}
