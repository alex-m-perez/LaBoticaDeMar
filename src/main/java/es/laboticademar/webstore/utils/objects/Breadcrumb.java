package es.laboticademar.webstore.utils.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Breadcrumb {
  private String label;
  private String href;
}
