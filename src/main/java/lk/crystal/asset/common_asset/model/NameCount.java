package lk.crystal.asset.common_asset.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NameCount {
    private String name;
    private String nameOne;
    private Integer count;

    private List< ParameterCount > parameterCounts;
}
