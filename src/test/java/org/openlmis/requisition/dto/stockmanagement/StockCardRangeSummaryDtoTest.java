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


package org.openlmis.requisition.dto.stockmanagement;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.openlmis.requisition.dto.StockCardRangeSummaryDto;
import org.openlmis.requisition.testutils.StockCardRangeSummaryDtoDataBuilder;
import org.openlmis.requisition.testutils.ToStringTestUtils;

public class StockCardRangeSummaryDtoTest {

  private StockCardRangeSummaryDto dto;

  @Before
  public void setUp() {
    dto = new StockCardRangeSummaryDtoDataBuilder()
        .withTags(ImmutableMap.of("tag1", 10, "tag2", 20))
        .build();
  }

  @Test
  public void shouldCalculateTagAmount() {
    assertEquals(new Integer(10), dto.getTagAmount("tag1"));
  }

  @Test
  public void shouldReturnZeroIfTagIsNotFound() {
    assertEquals(new Integer(0), dto.getTagAmount("tag3"));
  }

  @Test
  public void shouldReturnZeroIfMapIsEmpty() {
    dto = new StockCardRangeSummaryDtoDataBuilder()
        .withTags(Collections.EMPTY_MAP)
        .build();

    assertEquals(new Integer(0), dto.getTagAmount("tag1"));
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(StockCardRangeSummaryDto.class)
        .suppress(Warning.NONFINAL_FIELDS) // fields cannot be final
        .verify();
  }

  @Test
  public void shouldImplementToString() {
    ToStringTestUtils.verify(StockCardRangeSummaryDto.class, dto);
  }

}
