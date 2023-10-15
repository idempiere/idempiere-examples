/**
 * 
 */
package org.idempiere.callout.annotation.example;

import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.annotation.Callout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.I_C_BankTransfer;
import org.compiere.util.TimeUtil;

@Callout(tableName = I_C_BankTransfer.Table_Name, columnName = I_C_BankTransfer.COLUMNNAME_PayDate)
public class CalloutBankTransfer implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		Timestamp today = TimeUtil.getDay(null);
		mTab.setValue(I_C_BankTransfer.COLUMNNAME_DateAcct, today);
		return null;
	}
}
