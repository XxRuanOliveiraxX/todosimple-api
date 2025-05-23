package com.ruan.todosimple.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruan.todosimple.models.User;
import com.ruan.todosimple.repositories.UserRepository;
import com.ruan.todosimple.services.exceptions.DataBindingViolationException;
import com.ruan.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User findById(long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException(
            "Usuário não encontrado"));
    }

    public List<User> findAll(){
    List<User> user = this.userRepository.findAll();
    return user;
    }

    @Transactional
    public User create(User obj){
        obj.setId(null);
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj){
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try {
            this.userRepository.deleteById (id);
        } catch (Exception e) {
            throw new DataBindingViolationException(
                "Não é possivel excluir o usuário, pois há tasks relacionadas");
        }
    }
}
