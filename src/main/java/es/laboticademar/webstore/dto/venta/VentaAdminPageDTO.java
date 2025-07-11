package es.laboticademar.webstore.dto.venta;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaAdminPageDTO {
    private Page<VentaAdminResumenDTO> salesPage;
}