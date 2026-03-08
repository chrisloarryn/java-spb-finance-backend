package personclient.configuration.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import personclient.utils.mappers.ModelMapperManager;
import personclient.utils.mappers.ModelMapperService;

@Configuration
public class ModelMapperConfig
{
    @Bean
    public ModelMapper getModelMapper()
    {
        return new ModelMapper();
    }

    @Bean
    public ModelMapperService getModelMapperService(ModelMapper mapper)
    {
        return new ModelMapperManager(mapper);
    }
}