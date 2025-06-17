package com.sunghyun.football.global.utils;

import jakarta.persistence.OneToMany;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EntityDtoConverter {

    public static <E, D> List<D> convertListFromEntityToDto(List<E> entities, Class<D> dtoClass) {
        return entities.stream()
                .map(e -> convertEntity(e, dtoClass))
                .toList();
    }

    private static <E, D> D convertEntity(E entity, Class<D> dtoClass) {
        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance();

            copyOneToManyCollections(entity, dto);
            BeanUtils.copyProperties(entity, dto);  // 나머지 필드 복사

            return dto;
        } catch (Exception ex) {
            throw new RuntimeException("Entity→DTO 변환 실패", ex);
        }
    }

    /** E의 @OneToMany 필드를 찾아 DTO에 List<ChildDto>로 채워 넣는다 */
    private static <E, D> void copyOneToManyCollections(E entity, D dto) throws Exception {
        Class<?> entityClass = entity.getClass();
        Class<?> dtoClass    = dto.getClass();

        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(OneToMany.class) == null) continue;

            String name       = field.getName();
            Method getter     = entityClass.getMethod("get" + capitalize(name));
            List<Object> children = (List<Object>) getter.invoke(entity);

            Hibernate.initialize(children);

            Field dtoField = dtoClass.getDeclaredField(name);
            dtoField.setAccessible(true);

            Class<?> childDtoType = extractChildDtoType(dtoField);
            List<Object> childDtos = mapChildEntitiesToDtos(children, childDtoType);

            dtoField.set(dto, childDtos);
        }
    }

    /** dtoField의 제네릭 타입 파라미터(ChildDto 클래스)를 꺼낸다 */
    private static Class<?> extractChildDtoType(Field dtoField) {
        ParameterizedType pt = (ParameterizedType) dtoField.getGenericType();
        return (Class<?>) pt.getActualTypeArguments()[0];
    }

    /** childEntity 리스트를 childDto 인스턴스로 복사해서 반환 */
    private static List<Object> mapChildEntitiesToDtos(
            List<Object> childEntities, Class<?> childDtoType) throws Exception {
        List<Object> childDtos = new ArrayList<>(childEntities.size());
        for (Object childEntity : childEntities) {
            Object childDto = childDtoType.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(childEntity, childDto);
            childDtos.add(childDto);
        }
        return childDtos;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
