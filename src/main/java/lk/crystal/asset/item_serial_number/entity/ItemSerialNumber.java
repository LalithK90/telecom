package lk.crystal.asset.item_serial_number.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.ledger.entity.Ledger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "ItemSerialNumber" )
public class ItemSerialNumber {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  @Size( min = 2, message = "This number can not be accepted" )
  private String serialNumber;

  @ManyToOne
  private Ledger ledger;


}
