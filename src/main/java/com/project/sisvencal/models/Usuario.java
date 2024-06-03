package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String pass;
    
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
    
    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private Tipo tipo;
    
    @ManyToOne
    @JoinColumn(name = "id_genero")
    private Genero genero;
    
    public void setPass(String pass){
        pass = String.valueOf(pass.hashCode());
        this.pass = pass;
    }
    
    public void setEmail(String email){
        this.email = email.toLowerCase();
    }
}
