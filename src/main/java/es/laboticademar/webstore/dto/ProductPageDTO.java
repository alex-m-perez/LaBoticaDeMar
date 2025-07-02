package es.laboticademar.webstore.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import es.laboticademar.webstore.utils.objects.Breadcrumb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDTO {
    private Page<ProductoDTO> pageData;
    private List<Breadcrumb> breadcrumbs;
}