package co.empathy.academy.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class QueriesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void givenQuery_whenQuery_thenQueryWithCluster() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/search?query=Movies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{'query':'Movies','clusterName':'docker-cluster'}"));
    }
}
