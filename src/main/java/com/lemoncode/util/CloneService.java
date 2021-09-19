//package com.lemoncode.util;
//
//import com.metrobank.core.system.common.service.exception.IllegalStateException;
//import com.metrobank.core.system.transactions.api.repository.model.TransactionCloneable;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.Hibernate;
//import org.springframework.stereotype.Service;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import java.lang.reflect.Array;
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//public class CloneService {
//
//    private static final List<Class<?>> IMMUTABLE_CLASSES = Arrays.asList(String.class, Boolean.class, LocalDate.class, LocalDateTime.class);
//
//    public <T> T deepClone(final T object) {
//        return deepClone(object, null);
//    }
//
//    private <T> T deepClone(final T object, String ignoreField) {
//        try {
//
//            Class<?> objectClazz = object.getClass();
//            log.info("Cloning object class: {}", objectClazz.getName());
//
//            T clone = (T) objectClazz.getDeclaredConstructor().newInstance();
//
//            for (Field field : object.getClass().getDeclaredFields()) {
//                field.setAccessible(true);
//
//                if (field.getName().equals(ignoreField)) {
//                    log.info("Ignoring field {}", ignoreField);
//                    continue;
//                }
//
//                if (field.get(object) == null || Modifier.isFinal(field.getModifiers()))
//                    continue;
//
//                //TODO: is this safe to ignore
//                if (field.getName().equals("$$_hibernate_interceptor")) continue;
//
//                //don't copy ID of classes annotated with @TransactionCloneable
//                if (objectClazz.isAnnotationPresent(TransactionCloneable.class) && field.isAnnotationPresent(Id.class)) {
//                    log.info("Setting Id field: {} of class {} to null", field.getName(), objectClazz.getCanonicalName());
//                    field.set(clone, null);
//                    continue;
//                }
//
//                //prevent infinite recursion by ignoring mapped by field
//                String mappedBy = null;
//                if (field.isAnnotationPresent(OneToMany.class)) {
//                    OneToMany oneToMany = field.getAnnotation(OneToMany.class);
//                    mappedBy = oneToMany == null ? null : oneToMany.mappedBy();
//                }
//
//                if (field.getType().isPrimitive()
//                        || (field.getType().getSuperclass() != null && field.getType().getSuperclass().equals(Number.class))
//                        || IMMUTABLE_CLASSES.contains(field.getType())) {
//                    field.set(clone, field.get(object));
//                } else {
//                    Object fieldChildObject = field.get(object);
//                    Object childObj = Hibernate.unproxy(fieldChildObject);
//                    if (childObj == object) {
//                        field.set(clone, clone);
//                    } else {
//                        log.info("cloning field: {} of class: {}", field.getName(), objectClazz.getSimpleName());
//                        if (childObj.getClass().isAnnotationPresent(Entity.class) && !childObj.getClass().isAnnotationPresent(TransactionCloneable.class)) {
//                            field.set(clone, cloneIdOnly(childObj));
//                        } else if (childObj instanceof Map<?, ?>) {
//                            field.set(clone, deepCloneMap((Map<?, ?>) childObj));
//                        } else if (childObj instanceof Collection<?>) {
//                            field.set(clone, deepCloneCollection((Collection<?>) childObj, mappedBy));
//                        } else if (childObj instanceof Object[]) {
//                            field.set(clone, deepCloneObjectArray((Object[]) childObj));
//                        } else if (childObj.getClass().isArray()) {
//                            field.set(clone, clonePrimitiveArray(childObj));
//                        } else {
//                            field.set(clone, deepClone(childObj));
//                        }
//                    }
//                }
//            }
//            return clone;
//
//        } catch (IllegalAccessException | InstantiationException e) {
//            throw IllegalStateException.withMessageAndPath(e.getMessage(), "data");
//        } catch (Exception e) {
//            String message = "There was an error cloning entity/ies: " + e.getMessage() + "\n";
//            throw IllegalStateException.withMessageAndPath(message, "data");
//        }
//
//    }
//
//    private <T> Object cloneIdOnly(T object) {
//        try {
//            Class<?> clazz = object.getClass();
//            T newEntity = (T) object.getClass().getDeclaredConstructor().newInstance();
//            Set<Field> idFields = Arrays.stream(clazz.getDeclaredFields()).filter(x -> x.isAnnotationPresent(Id.class)).collect(Collectors.toSet());
//
//            for (Field f : idFields) {
//                f.setAccessible(true);
//                Object id = f.get(object);
//                f.set(newEntity, id);
//            }
//            return newEntity;
//        } catch (Exception e) {
//            throw new java.lang.IllegalStateException("Error cloning id for entity");
//        }
//    }
//
//    private Object clonePrimitiveArray(final Object input) {
//        final int length = Array.getLength(input);
//        final Object output = Array.newInstance(input.getClass().getComponentType(), length);
//        System.arraycopy(input, 0, output, 0, length);
//        return output;
//    }
//
//    private <E> E[] deepCloneObjectArray(final E[] input) {
//        final E[] clone = (E[]) Array.newInstance(input.getClass().getComponentType(), input.length);
//        for (int i = 0; i < input.length; i++) {
//            clone[i] = deepClone(input[i]);
//        }
//
//        return clone;
//    }
//
//    private <E> Collection<E> deepCloneCollection(final Collection<E> input, String ignoreField) {
//
//        Collection<E> clone;
//        if (input instanceof LinkedList<?>) {
//            clone = new LinkedList<>();
//        } else if (input instanceof SortedSet<?>) {
//            clone = new TreeSet<>();
//        } else if (input instanceof Set) {
//            clone = new HashSet<>();
//        } else {
//            clone = new ArrayList<>();
//        }
//
//        for (E item : input) {
//            E cloned = deepClone(item, ignoreField);
//            clone.add(cloned);
//        }
//
//        return clone;
//    }
//
//    private <K, V> Map<K, V> deepCloneMap(final Map<K, V> map) {
//        Map<K, V> clone;
//        if (map instanceof LinkedHashMap<?, ?>) {
//            clone = new LinkedHashMap<>();
//        } else if (map instanceof TreeMap<?, ?>) {
//            clone = new TreeMap<>();
//        } else {
//            clone = new HashMap<>();
//        }
//
//        for (Map.Entry<K, V> entry : map.entrySet()) {
//            clone.put(deepClone(entry.getKey()), deepClone(entry.getValue()));
//        }
//
//        return clone;
//    }
//}
