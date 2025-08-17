package com.sunghyun.football.global.utils;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.dto.exception.DtoConvertException;
import jakarta.persistence.OneToMany;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * EntityDtoConverter는 JPA 엔티티(E) 객체를 대응하는 DTO(D) 객체로 변환하는 범용 유틸 클래스입니다.
 * 특히 @OneToMany 연관관계를 맺은 컬렉션 필드를 강제 초기화(Hibernate.initialize)한 뒤 자식 DTO 리스트로 변환합니다.
 */
@Slf4j
public class EntityDtoConverter {

    /**
     * 엔티티 리스트를 DTO 리스트로 한 번에 변환한다.
     * 내부적으로 각 엔티티를 convertEntity로 변환합니다.
     *
     * @param entities  변환 대상 엔티티 리스트
     * @param dtoClass  결과 DTO 클래스 타입 토큰
     * @param <E>       엔티티 타입
     * @param <D>       DTO 타입
     * @return 변환된 DTO 객체 리스트
     */
    public static <E, D> List<D> convertListFromEntityToDto(List<E> entities, Class<D> dtoClass){
        List<D> result = new ArrayList<>();
        for (E entity : entities) {
            try {
                result.add(convertEntity(entity, dtoClass));
            } catch (DtoConvertException e) {
                log.warn("Read Error 발생되었으나 무시 = {}, 대상 item: {}",e.getErrorType().getMessage(),entity,e);
            }
        }
        return result;
    }

    /**
     * 단일 엔티티를 DTO로 변환한다.
     * 1) DTO 인스턴스 생성
     * 2) @OneToMany 컬렉션 필드 초기화 및 매핑
     * 3) 나머지 필드 일괄 복사
     */
    public static <E, D> D convertEntity(E entity, Class<D> dtoClass)  {
        try{
            D dto = dtoClass.getDeclaredConstructor().newInstance();

            // 2) @OneToMany 컬렉션 필드 처리
            copyOneToManyCollections(entity, dto);

            // 3) 나머지 스칼라·단일 연관관계 필드 복사
            BeanUtils.copyProperties(entity, dto);

            return dto;
        }catch (Exception e){
            throw new DtoConvertException(ErrorType.DTO_CONVERT_FAIL);
        }
        // 1) DTO 인스턴스 생성 (리플렉션 기반)
    }

    /**
     * 엔티티의 @OneToMany 필드를 찾아서
     * 1) 컬렉션 프록시 강제 초기화
     * 2) 자식 엔티티 → 자식 DTO 매핑
     * 3) DTO 필드에 세팅
     */
    @SuppressWarnings("unchecked")
    private static <E, D> void copyOneToManyCollections(E entity, D dto) throws Exception {
        Class<?> entityClass = entity.getClass();
        Class<?> dtoClass    = dto.getClass();

//        if(((FreeSubNotiEntity)entity).getNotiNo().equals(4L)){
//            throw new Exception();
//        }
        // 모든 필드 중 @OneToMany 붙은 필드만 처리
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(OneToMany.class) == null) continue;

            String name   = field.getName();
            // getter 호출로 컬렉션 획득
            Method getter = entityClass.getMethod("get" + capitalize(name));
            List<Object> children = (List<Object>) getter.invoke(entity);

            // 1) 지연 로딩 프록시 강제 초기화
            Hibernate.initialize(children);

            // 2) DTO의 동일 이름 필드에 매핑
            Field dtoField = dtoClass.getDeclaredField(name);
            dtoField.setAccessible(true);

            // 3) 필드의 제네릭 파라미터(ChildDto 타입) 추출
            Class<?> childDtoType = extractChildDtoType(dtoField);

            // 4) 자식 엔티티 리스트 → 자식 DTO 리스트
            List<Object> childDtos = mapChildEntitiesToDtos(children, childDtoType);

            // 5) DTO 필드에 List<ChildDto> 세팅
            dtoField.set(dto, childDtos);
        }
    }

    /**
     * DTO 필드의 제네릭 파라미터 타입(ChildDto 클래스)을 추출한다.
     */
    private static Class<?> extractChildDtoType(Field dtoField) {
        ParameterizedType pt = (ParameterizedType) dtoField.getGenericType();
        return (Class<?>) pt.getActualTypeArguments()[0];
    }

    /**
     * 자식 엔티티 리스트를 대응하는 자식 DTO 인스턴스로 복사해서 반환한다.
     * 스칼라 필드 및 단일 연관관계는 BeanUtils가 복사.
     */
    private static List<Object> mapChildEntitiesToDtos(
            List<Object> childEntities,
            Class<?> childDtoType) throws Exception {
        List<Object> childDtos = new ArrayList<>(childEntities.size());
        for (Object childEntity : childEntities) {
            // ChildDto 인스턴스 생성 및 필드 복사
            Object childDto = childDtoType.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(childEntity, childDto);
            childDtos.add(childDto);
        }
        return childDtos;
    }

    /**
     * 필드명을 메서드명으로 변환할 때 사용 (첫 글자 대문자)
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
