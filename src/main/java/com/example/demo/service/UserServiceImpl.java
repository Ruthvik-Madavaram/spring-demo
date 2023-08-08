package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.UserException;
import com.example.demo.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public User createUser(UserDTO user) {
        validateUser(user);
        User newUser = modelMapper.map(user, User.class);
        return userRepository.save(newUser);
    }

    private void validateUser(UserDTO user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        if(!violations.isEmpty()){
            List<String> errors = new ArrayList<>();
            for(ConstraintViolation<UserDTO> violation: violations){
                errors.add(violation.getMessage());
            }
            throw new UserException("Validation Error", errors);
        }
    }

    @Override
    public User updateUser(Integer id, UserDTO user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            modelMapper.map(user, existingUser);
            return userRepository.save(existingUser);
        }
        List<String> errors = new ArrayList<>();
        errors.add("User not found with id: " + id);
        throw new UserException("Not found Error", errors);
    }
}
