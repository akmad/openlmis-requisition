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

package org.openlmis.requisition;

import java.io.IOException;
import org.openlmis.requisition.utils.Resource2Db;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("performance-data")
@Order(5)
public class TestDataInitializer implements CommandLineRunner {
  private static final XLogger XLOGGER = XLoggerFactory.getXLogger(TestDataInitializer.class);
  private static final String PERF_DATA_PATH = "classpath:db/performance-data/";
  private static final String DB_MIGRATION_PATH = "classpath:db/migration/";

  @Value(value = PERF_DATA_PATH + "Requisitions5k.csv")
  private Resource requisitions;

  @Value(value = PERF_DATA_PATH + "RequisitionLineItems100k.csv")
  private Resource requisitionLineItems;

  @Value(value = PERF_DATA_PATH + "StockAdjustments300k.csv")
  private Resource stockAdjustments;
  
  @Value(value = PERF_DATA_PATH + "StockAdjustmentReasons.csv")
  private Resource stockAdjustmentReasons;

  @Value(value = DB_MIGRATION_PATH
      + "20170822230153657__generate_requisition_permission_strings.sql")
  private Resource generateRequisitionPermissionStrings;

  @Autowired
  private JdbcTemplate template;

  /**
   * Initializes test data.in
   * @param args command line arguments
   */
  public void run(String... args) throws IOException {
    XLOGGER.entry();

    Resource2Db r2db = new Resource2Db(template);

    r2db.insertToDbFromCsv("requisition.requisitions", requisitions);
    r2db.insertToDbFromCsv("requisition.requisition_line_items", requisitionLineItems);
    r2db.insertToDbFromCsv("requisition.stock_adjustments", stockAdjustments);
    r2db.insertToDbFromCsv("requisition.stock_adjustment_reasons", stockAdjustmentReasons);

    template.update("DELETE FROM requisition.requisition_permission_strings;");
    r2db.updateDbFromSqlSingle(generateRequisitionPermissionStrings);

    XLOGGER.exit();
  }
}