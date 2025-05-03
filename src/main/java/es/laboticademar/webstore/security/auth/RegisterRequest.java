package es.laboticademar.webstore.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String password;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String correo;
	private String direccionPostal;
	private Integer telefono;
}
