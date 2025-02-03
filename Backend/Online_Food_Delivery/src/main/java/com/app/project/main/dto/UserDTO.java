package com.app.project.main.dto;

import com.app.project.main.entities.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

	private String name;
	private String email;
	private String mobileNo;
	private String password;
	private Role role;
	private int addressId; // ID of the related Address entity

}
