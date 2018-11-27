package com.ibkglobal.common.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.common.validator.model.ValidResult;
import com.ibkglobal.common.validator.sttl.SttlFieldUtil;
import com.ibkglobal.message.common.normal.StandardTelegram;

@Component
public class ValidatorService {

  @Autowired
  Validator validator;

  /**
   * 표준전문 Validation
   * 
   * @param standardTelegram
   * @return
   */
  public ValidResult validationSttl(StandardTelegram standardTelegram) {
    ValidResult validResult = new ValidResult();

    validResult.setResult(true);
    validResult.setErrorList(new ArrayList<>());

    // 요청, 응답 타입
    String rqstRspnDcd = standardTelegram.getSttlSysCopt().getRqstRspnDcd();

    // base 검사(Send / Recv 확인)
    if (rqstRspnDcd == null || !SttlFieldUtil.fieldValueValid("RQST_RSPN_DCD", rqstRspnDcd)) {
      validResult.setResult(false);
      validResult.getErrorList().add("RQST_RSPN_DCD : Value Error");

      return validResult;
    }

    // 1차 검사(Null, Length, Default Value Check)
    Set<ConstraintViolation<StandardTelegram>> violation = validation(standardTelegram,
        SttlFieldUtil.getType(rqstRspnDcd));

    if (violation != null && violation.size() > 0) {
      violation.stream().forEach(f -> {
        validResult.getErrorList().add(f.getMessage());
      });
    }

    if (validResult.getErrorList().size() > 0) {
      validResult.setResult(false);

      return validResult;
    }

    // 2차 검사(Field 안의 데이터 유효성 검사)
    validResult.getErrorList().addAll(SttlFieldUtil.fieldValueDetailValid(rqstRspnDcd, standardTelegram));

    if (validResult.getErrorList().size() > 0) {
      validResult.setResult(false);

      return validResult;
    }

    return validResult;
  }

  /**
   * Validation 클래스 타입 추가
   * 
   * @param data
   * @param classType
   * @return
   */
  public <T> Set<ConstraintViolation<T>> validation(T data, Class<?> classType) {
    Set<ConstraintViolation<T>> violations = null;
    violations = validator.validate(data, classType);

    return violations;
  }

  /**
   * Validation 기본
   * 
   * @param data
   * @return
   */
  public <T> Set<ConstraintViolation<T>> validation(T data) {
    Set<ConstraintViolation<T>> violations = null;
    violations = validator.validate(data);

    return violations;
  }

  /**
   * ValidationCollection
   * 
   * @param data
   * @return
   */
  public List<Object> validationCollection(Collection<?> data) {
    List<Object> violations = new ArrayList<>();

    for (Object object : data) {
      if (object instanceof Collection) {
        List<Object> violationData = validationCollection((Collection<?>) object);

        violations.add(violationData);
      } else {
        violations.add(validation(object));
      }
    }

    return violations;
  }

  /**
   * Validation 결과 첫번째 값 리턴
   * 
   * @param data
   * @return
   */
  public <T> ConstraintViolation<T> findValidation(Set<ConstraintViolation<T>> data) {
    ConstraintViolation<T> violation = null;

    if (data.size() > 0) {
      violation = data.stream().findFirst().get();
    }

    return violation;
  }

  /**
   * Validation Collection 결과 첫번째 값 리턴
   * 
   * @param data
   * @return
   */
  @SuppressWarnings("unchecked")
  public Object findValidationCollection(List<Object> data) {
    Object violation = null;

    if (data.size() > 0) {
      violation = data.stream().findFirst().get();
    }

    if (violation instanceof Collection) {
      violation = findValidationCollection((List<Object>) violation);
    }

    return violation;
  }
}
