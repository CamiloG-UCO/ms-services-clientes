package co.edu.hotel.cleinteservice.steps;

import co.edu.hotel.cleinteservice.config.CucumberSpringConfiguration;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import co.edu.hotel.cleinteservice.dto.CreateClientRequest;
import co.edu.hotel.cleinteservice.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.es.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegisterNewClientStepDefinitions extends CucumberSpringConfiguration {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ClientRepository repo;

    private ResultActions actions;
    private String lastBody;

    @Dado("el formulario de registro de clientes")
    public void elFormularioDisponible() { /* no-op */ }

    @Dado("un servicio para el registro de clientes está en funcionamiento y no existe previamente un cliente con CC {int}")
    public void servicioOkYNoExiste(Integer docNumber) {
        assertThat(repo.findByDocumentNumber(docNumber.toString())).isEmpty();
    }

    @Cuando("el sistema cliente solicita la creación de un nuevo cliente con los siguientes datos:")
    public void solicitaCreacion(DataTable table) throws Exception {
        Map<String, String> row = table.asMaps().get(0);

        var req = new CreateClientRequest(
                DocumentType.valueOf(row.get("documentType")),
                row.get("documentNumber"),
                row.get("name"),
                row.get("lastNames"),
                row.get("email"),
                row.get("phone")
        );

        // 1) Disparamos la petición y obtenemos el MvcResult
        ResultActions initial = mockMvc.perform(
                post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        );
        MvcResult result = initial.andReturn();

        // 2) Si es async, hacemos asyncDispatch y guardamos en 'actions'; si no, usamos 'initial'
        if (result.getRequest().isAsyncStarted()) {
            this.actions = mockMvc.perform(asyncDispatch(result));
        } else {
            this.actions = initial;
        }

        // 3) Guardamos el body para verificaciones adicionales
        this.lastBody = this.actions.andReturn().getResponse().getContentAsString();
    }

    @Entonces("el registro del cliente debe ser exitoso \\(Código {int} Created)")
    public void respuestaCreated(int code) throws Exception {
        actions.andExpect(status().is(code));
    }

    @Y("el cliente recién creado debe tener el nombre completo {string}")
    public void validaNombreCompleto(String fullName) throws Exception {
        actions.andExpect(jsonPath("$.fullName").value(fullName));
    }

    @Y("la respuesta del sistema debe incluir un código de cliente que empiece por {string}")
    public void validaCodigoConPrefijo(String prefix) throws Exception {
        actions.andExpect(jsonPath("$.code").value(org.hamcrest.Matchers.startsWith(prefix)));
    }

    @Y("el cliente debe quedar almacenado en la base de datos")
    public void validaPersistencia() {
        assertThat(repo.count()).isEqualTo(1);
    }

    // -------- utils --------

    private static String extractJsonField(String json, String field) {
        if (json == null) return null;
        String key = "\"" + field + "\":\"";
        int i = json.indexOf(key);
        if (i < 0) return null;
        int s = i + key.length();
        int e = json.indexOf('"', s);
        return e > s ? json.substring(s, e) : null;
    }
}
