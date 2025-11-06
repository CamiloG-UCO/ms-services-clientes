package co.edu.hotel.cleinteservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClientResponse {
    @JsonProperty("code") String clientCode;
    String fullName;
    String email;
    String phone;

    public String code() { return clientCode; }
}
