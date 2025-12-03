package csendes.david.ser.service.dto;

import lombok.*;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SerDto {
    private Long id;
    private String name;
    private String type;
    private String size;
    private String manufacturer;
}
