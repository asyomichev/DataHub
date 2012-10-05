package ia.datahub.odata;

import org.odata4j.jersey.producer.resources.ODataProducerProvider;
import org.odata4j.producer.resources.EntitiesRequestResource;
import org.odata4j.producer.resources.EntityRequestResource;
import org.odata4j.producer.resources.MetadataResource;
import org.odata4j.producer.resources.ODataBatchProvider;
import org.odata4j.producer.resources.ServiceDocumentResource;

import com.sun.jersey.api.core.DefaultResourceConfig;

public class ODataResourceConfig extends DefaultResourceConfig {

    public ODataResourceConfig() {
        super(EntitiesRequestResource.class, EntityRequestResource.class, MetadataResource.class,
                ServiceDocumentResource.class, ODataProducerProvider.class, ODataBatchProvider.class);
    }

}
