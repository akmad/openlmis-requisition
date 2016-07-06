package org.openlmis.referencedata.web;

import org.openlmis.referencedata.domain.Period;
import org.openlmis.referencedata.exception.EmptyObjectException;
import org.openlmis.referencedata.i18n.ExposedMessageSource;
import org.openlmis.referencedata.repository.PeriodRepository;
import org.openlmis.referencedata.validate.PeriodValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RepositoryRestController
public class PeriodController {
  private Logger logger = LoggerFactory.getLogger(PeriodController.class);

  @Autowired @Qualifier("beforeSavePeriodValidator")
  private PeriodValidator validator;

  @Autowired
  private PeriodRepository periodRepository;

  @Autowired
  private ExposedMessageSource messageSource;

  @RequestMapping(value = "/periods", method = RequestMethod.POST)
  public ResponseEntity<?> createPeriod(@RequestBody Period period,
                                        BindingResult bindingResult, SessionStatus status) {
    if (period == null) {
      throw new EmptyObjectException("null.period.error");
    } else {
      logger.debug("Creating new period");
      validator.validate(period, bindingResult);
      if (bindingResult.getErrorCount() == 0) {
        Period newPeriod = periodRepository.save(period);
        return new ResponseEntity<Period>(newPeriod, HttpStatus.CREATED);
      } else {
        return new ResponseEntity(getPeriodErrors(bindingResult), HttpStatus.BAD_REQUEST);
      }
    }
  }

  private Map<String, String> getPeriodErrors(final BindingResult bindingResult) {
    return new HashMap<String, String>() {
      {
        for (FieldError error : bindingResult.getFieldErrors()) {
          put(error.getField(), error.getDefaultMessage());
        }
      }
    };
  }

  @RequestMapping(value = "/periods/{id}/difference", method = RequestMethod.GET)
  @ResponseBody
  public String getTotalDifference(@PathVariable("id") UUID periodId) {
    Period period = periodRepository.findOne(periodId);

    java.time.Period total = java.time.Period.between(period.getStartDate(), period.getEndDate());
    String months = Integer.toString(total.getMonths());
    String days = Integer.toString(total.getDays() + 1);

    String[] msgArgs = {months, days};
    logger.debug("Returning total days and months of schedule periods");

    return messageSource.getMessage("requisition.message.totalPeriod", msgArgs, LocaleContextHolder
            .getLocale());
  }
}