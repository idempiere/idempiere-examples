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
 **********************************************************************/
package org.idempiere.zul.form.example;

import static org.adempiere.webui.ClientInfo.SMALL_WIDTH;
import static org.adempiere.webui.ClientInfo.maxWidth;

import java.util.HashMap;
import java.util.Map;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.panel.CustomForm;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Hlayout;

public class AllocationForm extends CustomForm {

	private static final long serialVersionUID = 8445397333275085272L;

	@Wire("#parameterLayoutPanel")
	protected Panel parameterLayoutPanel;
	@Wire("#parameterLayout")
	protected Grid parameterLayout;
	@Wire("#allocationLayoutPanel")
	protected Panel allocationLayoutPanel;
	@Wire("#allocationLayout")
	protected Grid allocationLayout;
	@Wire("#statusBar")
	protected Hlayout statusBar;
	/** Auto write off parameter */
	@Wire("#autoWriteOff")
	protected Checkbox autoWriteOff;
	@Wire("#invoiceInfo")
	protected Label invoiceInfo;
	@Wire("#paymentInfo")
	protected Label paymentInfo;
	@Wire("#differenceField")
	protected Textbox differenceField;
	@Wire("#allocateButton")
	protected Button allocateButton;
	@Wire("#refreshButton")
	protected Button refreshButton;
	/** Multi currency parameter */
	@Wire("#multiCurrency")
	protected Checkbox multiCurrency;
	@Wire("#paymentTable")
	protected WListbox paymentTable;
	@Wire("#invoiceTable")
	protected WListbox invoiceTable;
	@Wire("#allocCurrencyLabel")
	protected Label allocCurrencyLabel;
	
	@Wire("#bpartnerSearchCell")
	protected Cell bpartnerSearchCell;
	@Wire("#dateFieldCell")	
	protected Cell dateFieldCell;
	@Wire("#organizationPickCell")
	protected Cell organizationPickCell;
	@Wire("#currencyPickCell")
	protected Cell currencyPickCell;
	@Wire("#chargePickCell")
	protected Cell chargePickCell;
	@Wire("#docTypePickCell")
	protected Cell docTypePickCell;
	
	protected Map<String, String> arguments;

	protected Component parameterLayoutComponent;

	protected Component allocationLayoutComponent;
	
	public AllocationForm() {
		arguments = new HashMap<>();
		arguments.put("dateLabel", Msg.getMsg(Env.getCtx(), "Date"));
		arguments.put("autoWriteOff", Msg.getMsg(Env.getCtx(), "AutoWriteOff", true));
		arguments.put("autoWriteOff_tooltiptext", Msg.getMsg(Env.getCtx(), "AutoWriteOff", false));
		arguments.put("bpartnerLabel", Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		arguments.put("paymentLabel", " " + Msg.translate(Env.getCtx(), "C_Payment_ID"));
		arguments.put("invoiceLabel", " " + Msg.translate(Env.getCtx(), "C_Invoice_ID"));
		arguments.put("chargeLabel"," " + Msg.translate(Env.getCtx(), "C_Charge_ID"));
		arguments.put("docTypeLabel", " " + Msg.translate(Env.getCtx(), "C_DocType_ID"));	
		arguments.put("differenceLabel", Msg.getMsg(Env.getCtx(), "Difference"));
		arguments.put("allocateButton", Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Process")));
		arguments.put("refreshButton", Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Refresh")));
		arguments.put("currencyLabel", Msg.translate(Env.getCtx(), "C_Currency_ID"));
		arguments.put("multiCurrency", Msg.getMsg(Env.getCtx(), "MultiCurrency"));
		arguments.put("organizationLabel", Msg.translate(Env.getCtx(), "AD_Org_ID"));
		
		Component form = null;
		parameterLayoutComponent = null;
		allocationLayoutComponent = null;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
			form = Executions.createComponents("~./form.zul", this, arguments);
			Component parameterLayoutPanel = form.getFellow("parameterLayoutPanel");
			parameterLayoutComponent = Executions.createComponents("~./parameter-layout.zul", parameterLayoutPanel, arguments);
			Component allocationLayoutPanel = form.getFellow("allocationLayoutPanel");
			if (maxWidth(SMALL_WIDTH-1)) {
				allocationLayoutComponent = Executions.createComponents("~./allocation-layout-sw.zul", allocationLayoutPanel, arguments);
			} else {
				allocationLayoutComponent = Executions.createComponents("~./allocation-layout.zul", allocationLayoutPanel, arguments);
			}
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
		
		Selectors.wireComponents(form, this, false);
		Selectors.wireComponents(parameterLayoutComponent, this, true);
		Selectors.wireComponents(allocationLayoutComponent, this, true);
	}

}
