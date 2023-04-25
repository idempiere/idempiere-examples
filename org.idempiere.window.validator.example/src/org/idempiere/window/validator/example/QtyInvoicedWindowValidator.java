/***********************************************************************
 * This file is part of iDempiere ERP Open Source                      *
 * http://www.idempiere.org                                            *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 *                                                                     *
 * Contributors:                                                       *
 * - hengsin                         								   *
 **********************************************************************/
package org.idempiere.window.validator.example;

import java.math.BigDecimal;

import org.adempiere.util.Callback;
import org.adempiere.webui.adwindow.ADWindow;
import org.adempiere.webui.adwindow.validator.WindowValidator;
import org.adempiere.webui.adwindow.validator.WindowValidatorEvent;
import org.adempiere.webui.adwindow.validator.WindowValidatorEventType;
import org.adempiere.webui.window.Dialog;
import org.compiere.model.GridTab;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrderLine;
import org.compiere.process.DocAction;
import org.compiere.util.Env;
import org.osgi.service.component.annotations.Component;

/**
 * @author hengsin
 */
@Component(service = WindowValidator.class, immediate = true, property = {"AD_Window_UU=c91d7690-9abe-45d2-a271-0f907c1b4329", "events=beforeDocAction"})
public class QtyInvoicedWindowValidator implements WindowValidator {

	/**
	 * Default Constructor
	 */
	public QtyInvoicedWindowValidator() {
	}

	/* (non-Javadoc)
	 * @see org.adempiere.webui.adwindow.validator.WindowValidator#onWindowEvent(org.adempiere.webui.adwindow.validator.WindowValidatorEvent, org.adempiere.util.Callback)
	 */
	@Override
	public void onWindowEvent(WindowValidatorEvent event, Callback<Boolean> callback) {
		if (event.getName().equals(WindowValidatorEventType.BEFORE_DOC_ACTION.getName()) ) {
			ADWindow adwindow = event.getWindow();
			GridTab gridTab = adwindow.getADWindowContent().getActiveGridTab();
			if (gridTab != null && gridTab.getAD_Table_ID()==MInvoice.Table_ID) {
				int invoiceId = ((Number)gridTab.getValue("C_Invoice_ID")).intValue();
				MInvoice invoice = new MInvoice(Env.getCtx(), invoiceId, null);
				Object docAction = gridTab.getValue("DocAction");
				if (invoice.isSOTrx() && !invoice.isReversal() && invoice.getReversal_ID()==0 && DocAction.ACTION_Complete.equals(docAction)) {
					MInvoiceLine[] lines = invoice.getLines(true);
					boolean overInvoice = false;
					for(MInvoiceLine line : lines) {
						if (line.getC_OrderLine_ID() > 0 && 
							(line.getM_Product_ID() > 0 || line.getC_Charge_ID() > 0) && line.getQtyInvoiced().signum() > 0) {
							MOrderLine orderLine = new MOrderLine(invoice.getCtx(), line.getC_OrderLine_ID(), invoice.get_TrxName());
							BigDecimal toInvoice = orderLine.getQtyOrdered().subtract(orderLine.getQtyInvoiced());
							if (toInvoice.signum() < 0)
								toInvoice = BigDecimal.ZERO;
							if (toInvoice.compareTo(line.getQtyInvoiced()) < 0) {
								overInvoice = true;
								break;
							}
						}
					}
					if (overInvoice) {
						Dialog.ask("Invoice " + invoice.getDocumentNo(), 0, null, 
								"You are preparing to Complete an invoice that exceeds the non-invoiced amount of the sales order. Do you want to continue?", 
								new Callback<Boolean>() {										
									@Override
									public void onCallback(Boolean result) {
										callback.onCallback(result);
									}
								});		
						return;
					}
				}
			}
		}
		callback.onCallback(Boolean.TRUE);
	}

}
