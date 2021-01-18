//package com.lemoncode.manga;
//
//import org.hibernate.resource.transaction.backend.jta.internal.synchronization.ExceptionMapper;
//
//import javax.validation.ConstraintViolationException;
//import javax.validation.Path;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.ExceptionMapper;
//import javax.ws.rs.ext.Provider;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import static javax.ws.rs.core.Response.status;
//
//@Provider
//public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
//    @Override
//    public Response toResponse(ConstraintViolationException exception) {
//        Map<String, String> errors = new HashMap<>();
//        exception.getConstraintViolations()
//                .forEach(v -> errors.put(extractLastFieldName(v.getPropertyPath().iterator()), v.getMessage()));
//        return status(Response.Status.BAD_REQUEST).entity(errors).build();
//    }
//
//    private String extractLastFieldName(Iterator<Path.Node> nodes) {
//        Path.Node last = null;
//        while (nodes.hasNext()) {
//            last = nodes.next();
//        }
//        return last.getName();
//    }
//}
