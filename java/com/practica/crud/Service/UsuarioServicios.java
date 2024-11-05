package com.practica.crud.Service;

import com.practica.crud.Model.Usuarios;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioServicios {
    private ArrayList<Usuarios> users =new ArrayList<>();
    private long nextid=1l;
    public ArrayList<Usuarios> getUser(){
        return users;
    }

    public Optional<Usuarios> getUserById(Long id) {
        return users.stream().filter(e->e.getId().equals(id)).findFirst();
    }
    public Usuarios createUser(Usuarios user){
         user.setId(nextid++);
         this.users.add(user);
         return user;
    }
    public Optional<Usuarios> updateUser(Long id, Usuarios user) {
        Optional<Usuarios> query = this.getUserById(id);
        if (query.isEmpty()){
            return query;
        }
        Usuarios existente = query.get();
        existente.setNombre(user.getNombre());
        existente.setApellido(user.getApellido());
        existente.setEdad(user.getEdad());
        return query;
    }
    public boolean deleteuser(Long id){
        return users.removeIf(e->e.getId().equals(id));
    }
}
