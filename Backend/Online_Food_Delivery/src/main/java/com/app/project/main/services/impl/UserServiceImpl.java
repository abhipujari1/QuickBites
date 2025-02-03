package com.app.project.main.services.impl;

import com.app.project.main.dto.UserDTO;
import com.app.project.main.entities.Address;
import com.app.project.main.entities.User;
import com.app.project.main.entities.enums.Role;
import com.app.project.main.repositories.AddressRepository;
import com.app.project.main.repositories.UserRepository;
import com.app.project.main.services.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ModelMapper modelMapper;

	private UserDTO convertToDTO(User user) {
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		if (user.getAddress() != null) {
			userDTO.setAddressId(user.getAddress().getAddressId());
		}
		return userDTO;
	}

	private User convertToEntity(UserDTO userDTO) {
		User user = modelMapper.map(userDTO, User.class);
		if (userDTO.getAddressId() != 0) {
			Address address = addressRepository.findById(userDTO.getAddressId()).orElse(null);
			user.setAddress(address);
		}
		return user;
	}

	@Override
	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<UserDTO> getUserById(int id) {
		return userRepository.findById(id).map(this::convertToDTO);
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserDTO createUser(UserDTO userDTO) {
		System.out.println(userDTO.toString());

		// Find the address using addressId
		Address address = addressRepository.findById(userDTO.getAddressId())
				.orElseThrow(() -> new RuntimeException("Address not found for ID: " + userDTO.getAddressId()));
		System.out.println("User role: " + userDTO.getRole());
		// Convert UserDTO to User entity
		User user = modelMapper.map(userDTO, User.class);
		System.out.println("User role: " + userDTO.getRole());
		// Encode password before saving
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		 user.setRole(userDTO.getRole());
		// Set the address for the user entity
		user.setAddress(address);

		// Save the user
		User savedUser = userRepository.save(user);

		// Convert saved user to UserDTO and return
		return modelMapper.map(savedUser, UserDTO.class);
	}

	@Override
	public Optional<UserDTO> updateUser(int id, UserDTO userDTO) {
		return userRepository.findById(id).map(existingUser -> {
			User user = convertToEntity(userDTO);
			user.setUserId(id); // Ensure the correct ID is set
			User updatedUser = userRepository.save(user);
			return convertToDTO(updatedUser);
		});
	}

	@Override
	public boolean deleteUser(int id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
