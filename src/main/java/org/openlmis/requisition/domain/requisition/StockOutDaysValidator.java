/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.requisition.domain.requisition;

import static org.openlmis.requisition.domain.requisition.Requisition.REQUISITION_LINE_ITEMS;
import static org.openlmis.requisition.i18n.MessageKeys.ERROR_STOCKOUT_DAYS_CANT_BE_GREATER_THAN_LENGTH_OF_PERIOD;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.AllArgsConstructor;
import org.openlmis.requisition.domain.DomainValidator;
import org.openlmis.requisition.domain.requisition.Requisition;
import org.openlmis.requisition.domain.requisition.RequisitionLineItem;
import org.openlmis.requisition.utils.Message;
import java.util.Map;

@AllArgsConstructor
class StockOutDaysValidator implements DomainValidator {

  private static final int DAYS_IN_MONTH = 30;

  private final Requisition requisition;
  private final Integer numberOfMonthsInPeriod;

  @Override
  public void validate(Map<String, Message> errors) {
    if (!isEmpty(requisition.getNonSkippedFullSupplyRequisitionLineItems())) {
      requisition.getNonSkippedFullSupplyRequisitionLineItems()
          .forEach(i -> rejectIfTotalStockOutDaysIsGreaterThanLengthOfPeriod(
              errors, i));
    }
  }

  private void rejectIfTotalStockOutDaysIsGreaterThanLengthOfPeriod(
      Map<String, Message> errors, RequisitionLineItem requisitionLineItem) {
    if (requisitionLineItem.getTotalStockoutDays() == null) {
      return;
    }
    if (requisitionLineItem.getTotalStockoutDays() > numberOfMonthsInPeriod * DAYS_IN_MONTH) {
      errors.put(REQUISITION_LINE_ITEMS,
          new Message(ERROR_STOCKOUT_DAYS_CANT_BE_GREATER_THAN_LENGTH_OF_PERIOD));
    }
  }

}