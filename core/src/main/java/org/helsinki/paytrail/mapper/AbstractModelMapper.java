package org.helsinki.paytrail.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Supplier;

public abstract class AbstractModelMapper<Request, Response> {

    protected final ObjectMapper mapper;
    private final Request genericModelTypeObject;
    private final Response genericResponseTypeObject;

    public AbstractModelMapper(
            ObjectMapper mapper,
            Supplier<Request> modelSupplier,
            Supplier<Response> dtoSupplier
    ) {
        this.mapper = mapper;
        this.genericModelTypeObject = modelSupplier.get();
        this.genericResponseTypeObject = dtoSupplier.get();
    }

    /**
     * Convert the given DTO type of {@code <Response>} to a Model object type of {@code <Request>}.
     *
     * @param response The DTO object to be converted to a model
     * @return <Request> A model object type
     */
    public Request from(Response response) {
        return (Request) mapper.convertValue(response, genericModelTypeObject.getClass());
    }

    /**
     * Convert the Model object type of {@code <Request>} to a Response object type of {@code <Response>}.
     *
     * @param request The request object to be converted to Response
     * @return A model object
     */
    public Response to(Request request) {
        return (Response) mapper.convertValue(request, genericResponseTypeObject.getClass());
    }

    public Request updateFromRequestToResponse(Request request, Response response) throws JsonProcessingException {
        return mapper.readerForUpdating(request).readValue(mapper.writeValueAsString(response));
    }

}
