package es.laboticademar.webstore.dto.producto;

import java.util.List;

import org.springframework.data.domain.Page;

import es.laboticademar.webstore.dto.BreadcrumbDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDTO {
    private Page<ProductoDTO> pageData;
    private List<BreadcrumbDTO> breadcrumbs;
}