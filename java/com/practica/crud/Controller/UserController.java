package com.practica.crud.Controller;

import com.practica.crud.Model.InicioSession;
import com.practica.crud.Model.Usuarios;
import com.practica.crud.Service.UsuarioServicios;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/api/user")
public class UserController {
    private final UsuarioServicios usuarioServicios;

   @Autowired
    public UserController(UsuarioServicios usuarioServicios) {
        this.usuarioServicios = usuarioServicios;
    }
    @GetMapping
    public ArrayList<Usuarios> getAllUser(){
       return usuarioServicios.getUser();
    }
    @PostMapping
    public ResponseEntity<?> CreateUser(@Valid @RequestBody Usuarios user, BindingResult result) {
        if (result.hasFieldErrors("edad")) {

            return ResponseEntity.badRequest().body(result.getFieldError("edad").getDefaultMessage());
        }
        Usuarios createdUser = usuarioServicios.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> getUserByID (@PathVariable Long id){
        Optional<Usuarios> user=usuarioServicios.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @PutMapping ("/{id}")
    public ResponseEntity<Usuarios> UpdateUser(@PathVariable Long id, @RequestBody Usuarios UserDetalles){
       Optional<Usuarios>user =usuarioServicios.updateUser(id,UserDetalles);
       return user.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @PostMapping("delete/{id}")
    public Object DeleteUser(@PathVariable Long id, Model model) {
        if (usuarioServicios.deleteuser(id)) {
            ResponseEntity.ok().build();
            return "redirect:/api/user/list";

        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/list") // Nueva ruta para listar usuarios
    public String listUsers(Model model) {
        ArrayList<Usuarios> usuarios = usuarioServicios.getUser(); // Obtener la lista de usuarios
        model.addAttribute("usuarios", usuarios);// Agregar al modelo

        return "userList";
    }
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("usuario") Usuarios usuarioDetalles, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editUser";
        }
        usuarioServicios.updateUser(id, usuarioDetalles);
        return "redirect:/api/user/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        Optional<Usuarios> usuario = usuarioServicios.getUserById(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "editUser";
        } else {
            return "redirect:/api/user/list"; // Redirige a la lista si el usuario no existe
        }
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {

        model.addAttribute("usuario", new Usuarios());
        return "addUser";
    }
    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute Usuarios usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addUser";
        }
        usuarioServicios.createUser(usuario);
        return "redirect:/api/user/list";
    }
    @GetMapping("/inicio")
    public String showAddUserInicio(Model model){
       model.addAttribute("inicios",new InicioSession());
       return "inicio";
    }
    @PostMapping("/inicio")
    public String Inicio(@ModelAttribute InicioSession inicio, RedirectAttributes redirectAttributes) {
        String nombre = "andy"; // Nombre correcto
        String contraseña = "123"; // Contraseña correcta
        if (nombre.equals(inicio.getNombre()) && contraseña.equals(inicio.getContraseña())) {
            return "redirect:/api/user/list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Nombre o Contraseña Incorrecta");
            return "redirect:/api/user/inicio";
        }
    }

}




