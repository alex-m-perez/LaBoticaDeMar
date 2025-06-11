package es.laboticademar.webstore.mappers;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import es.laboticademar.webstore.services.interfaces.TipoService;
import es.laboticademar.webstore.services.interfaces.LaboratorioService;

@Component
public class ExcelToProductMapper {

    private final DataFormatter fmt = new DataFormatter();

    private final FamiliaService familiaService;
    private final CategoriaService categoriaService;
    private final SubcategoriaService subcategoriaService;
    private final TipoService tipoService;
    private final LaboratorioService laboratorioService;

    @Autowired
    public ExcelToProductMapper(
        FamiliaService familiaService,
        CategoriaService categoriaService,
        SubcategoriaService subcategoriaService,
        TipoService tipoService,
        LaboratorioService laboratorioService) {
        this.familiaService = familiaService;
        this.categoriaService = categoriaService;
        this.subcategoriaService = subcategoriaService;
        this.tipoService = tipoService;
        this.laboratorioService = laboratorioService;
    }

    /**
     * Mapea una fila del Excel a un objeto Producto,
     * asumiendo que es una fila válida (no cabecera).
     */
    public Producto map(Row row) {
        Producto prod = new Producto();

        // --- 1) ID ---
        String codigoTxt = fmt.formatCellValue(row.getCell(0)).trim();
        prod.setId(new BigDecimal(codigoTxt));

        // --- 2) Nombre ---
        prod.setNombre(fmt.formatCellValue(row.getCell(1)).trim());

        // --- 3) Familia ---
        String famTxt = fmt.formatCellValue(row.getCell(2)).trim();
        Optional.of(famTxt)
                .filter(s -> !s.isEmpty())
                .flatMap(familiaService::findByNombre)
                .ifPresent(prod::setFamilia);

        // --- 4) Categoría ---
        String catTxt = fmt.formatCellValue(row.getCell(3)).trim();
            Optional.of(catTxt)
                .filter(s -> !s.isEmpty())
                .flatMap(categoriaService::findByNombre)
                .ifPresent(prod::setCategoria);

        // --- 5) Subcategoría ---
        String subTxt = fmt.formatCellValue(row.getCell(4)).trim();
        Optional.of(subTxt)
                .filter(s -> !s.isEmpty())
                .flatMap(subcategoriaService::findByNombre)
                .ifPresent(prod::setSubCategoria);

        // --- 6) Tipo ---
        String tipoTxt = fmt.formatCellValue(row.getCell(5)).trim();
        Optional.of(tipoTxt)
                .filter(s -> !s.isEmpty())
                .flatMap(tipoService::findByNombre)
                .ifPresent(prod::setTipo);

        // --- 7) Stock actual ---
        String stockTxt = fmt.formatCellValue(row.getCell(6)).trim();
        prod.setStock(stockTxt.isEmpty() ? 0 : Integer.parseInt(stockTxt));

        // --- 8) Activo (siempre true) ---
        prod.setActivo(true);

        // --- 9) Laboratorio ---
        String labTxt = fmt.formatCellValue(row.getCell(27)).trim();
        Optional.of(labTxt)
                .filter(s -> !s.isEmpty())
                .flatMap(laboratorioService::findByNombre)
                .ifPresent(prod::setLaboratorio);

        // --- 10) Precio (P.V.P.) ---
        String precioTxt = fmt.formatCellValue(row.getCell(9))
                             .replace("€", "")
                             .replace(".", "")
                             .replace(",", ".")
                             .trim();
        prod.setPrice(Float.valueOf(precioTxt));

        return prod;
    }

    /**
     * Extrae sólo el ID de la fila, para chequear duplicados sin construir todo el objeto.
     */
    public BigDecimal extractId(Row row) {
        String codigoTxt = fmt.formatCellValue(row.getCell(0)).trim();
        return new BigDecimal(codigoTxt);
    }
}
